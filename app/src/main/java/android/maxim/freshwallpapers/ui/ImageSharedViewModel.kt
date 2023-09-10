package android.maxim.freshwallpapers.ui

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.data.models.LikedImageMap
import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import android.maxim.freshwallpapers.di.LikedImagesPrefs
import android.maxim.freshwallpapers.utils.LIKED
import android.maxim.freshwallpapers.utils.LIKED_IMAGE_MAP
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ImageSharedViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    @Inject
    lateinit var repository: WallpapersRepository
    @Inject
    @LikedImagesPrefs
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var gson: Gson
    private lateinit var retrievedImageMap: LikedImageMap
    private val _imageList = MutableLiveData<List<Image>>()
    val imageList: LiveData<List<Image>> = _imageList
    private val _imageMap = MutableLiveData<LikedImageMap>()
    val imageMap: LiveData<LikedImageMap> = _imageMap
    val errorLiveData = MutableLiveData<Exception>()

    suspend fun getCollectionsList() = liveData {
        try {
            repository.getCollectionsList().collect { value ->
                emit(value)
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    fun getImageList(category: String) {
        if (category == LIKED) {
            _imageList.value = getLikedImageList()
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                var response: List<Image>? = null
                try {
                    response = repository.getImageList(category).body()?.imageList
                } catch (exception: Exception) {
                    errorLiveData.postValue(exception)
                }
                withContext(Dispatchers.Main) {
                    if (response != null) {
                        _imageList.value = response!!
                    }
                }
            }
        }
    }

    private fun getLikedImageList(): List<Image> {
        return getLikedImageMap()
            .likedImageMap
            .values
            .toList()
    }

    fun addImageToLiked(image: Image) {
        getLikedImageMap().likedImageMap.put(image.id, image)
        val imageListGson = gson.toJson(retrievedImageMap)
        sharedPreferences
            .edit()
            .putString(LIKED_IMAGE_MAP, imageListGson)
            .apply()
    }

    fun removeImageFromLiked(image: Image) {
        getLikedImageMap().likedImageMap.remove(image.id)
        val imageListGson = gson.toJson(retrievedImageMap)
        sharedPreferences
            .edit()
            .putString(LIKED_IMAGE_MAP, imageListGson)
            .apply()
    }

    fun getLikedImageMap(): LikedImageMap {
        if (!sharedPreferences.contains(LIKED_IMAGE_MAP)) {
            val likedImageMap = LikedImageMap(mutableMapOf())
            val likedImageMapGson = gson.toJson(likedImageMap)
            sharedPreferences
                .edit()
                .putString(LIKED_IMAGE_MAP, likedImageMapGson)
                .apply()
        }
        val serializedImageMap = sharedPreferences.getString(LIKED_IMAGE_MAP, null)
        retrievedImageMap = if (serializedImageMap != null) {
            gson.fromJson(serializedImageMap, LikedImageMap::class.java)
        } else {
            //emergency value, liked image list will be empty
            LikedImageMap(mutableMapOf())
        }
        _imageMap.value = retrievedImageMap
        return retrievedImageMap
    }

    fun saveBitmapToExternalStorage(bitmap: Bitmap, imageName: String) {
        val imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = File(imageDir, "JPEG_$imageName.jpg")
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveBitmapToMediaStore(context: Context, image: Bitmap, fileName: String) {
        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val imageFileName = "JPEG_$fileName.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val contentResolver = context.contentResolver
        val imageUri = contentResolver.insert(collection, values)

        imageUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }
    }
}