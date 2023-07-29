package android.maxim.freshwallpapers.data.repository

import android.content.Context
import android.maxim.freshwallpapers.data.models.ImageList
import android.maxim.freshwallpapers.data.models.WallpapersCollection
import android.maxim.freshwallpapers.di.WallpapersRepositoryEntryPoint
import android.maxim.freshwallpapers.utils.Constants
import android.maxim.freshwallpapers.utils.Constants.TAG
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.EntryPoints
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WallpapersRepository @Inject constructor(@ApplicationContext context: Context) {

    private val hiltEntryPoint = EntryPoints.get(
        context.applicationContext,
        WallpapersRepositoryEntryPoint::class.java)
    private val wallpapersApi = hiltEntryPoint.wallpapersApi()

    private val firestoreDb = Firebase.firestore
    private val firestoreReference = firestoreDb.collection("collections")

    fun getCollectionsList(): Flow<List<WallpapersCollection>> = flow {
       val collectionsList = suspendCoroutine<List<WallpapersCollection>> { continuation ->
           firestoreReference
               .get()
               .addOnSuccessListener { result ->
                   val list = mutableListOf<WallpapersCollection>()
                   for (document in result) {
                       val collection = WallpapersCollection(
                           document.getString("title").toString(),
                           document.getString("imageUrl").toString()
                       )
                       list.add(collection)
                   }
                   continuation.resume(list)
               }
               .addOnFailureListener { exception ->
                   //TODO handle the exception
               }
       }
        emit(collectionsList)
    }.flowOn(Dispatchers.IO)

    suspend fun getImageList(collection: String): Response<ImageList> {
        Log.d(TAG, "WallpapersRepository.getImageList() with parameter $collection")
        val response = wallpapersApi.getImageList(
            Constants.PIXABAY_API_KEY,
            200,
            "background",
            "vertical",
            "photo",
            true,
            collection)
        if (response.isSuccessful) {
            Log.d(TAG, "WallpapersRepository.getImageList() coroutine response")
        } else {
            //TODO handle the exception
        }
        return response
    }
}
