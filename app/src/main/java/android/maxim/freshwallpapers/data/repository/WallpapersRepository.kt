package android.maxim.freshwallpapers.data.repository

import android.content.Context
import android.maxim.freshwallpapers.BuildConfig
import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.data.models.ImageList
import android.maxim.freshwallpapers.data.models.WallpapersCollection
import android.maxim.freshwallpapers.di.WallpapersRepositoryEntryPoint
import android.maxim.freshwallpapers.utils.*
import dagger.hilt.EntryPoints
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WallpapersRepository @Inject constructor(@ApplicationContext context: Context) {

    private val hiltEntryPoint = EntryPoints.get(
        context.applicationContext,
        WallpapersRepositoryEntryPoint::class.java)
    private val wallpapersApi = hiltEntryPoint.wallpapersApi()
    private val firestoreDb = hiltEntryPoint.firestoreDb()
    private val firestoreReference = firestoreDb.collection("collections")
    private val errorMessage = context.resources.getString(R.string.network_error_message)

    fun getCollectionsList(errorCallback: ErrorCallback): Flow<List<WallpapersCollection>> = flow {
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
                   val errorMessage = "Request failed: ${exception.message}"
                   errorCallback.onError(errorMessage)
               }
       }
        emit(collectionsList)
    }.flowOn(Dispatchers.IO)

    suspend fun getImageList(collection: String, errorCallback: ErrorCallback): Response<ImageList> {
        return try {
            val deferred = CoroutineScope(Dispatchers.IO).async {
                wallpapersApi.getImageList(
                    BuildConfig.API_KEY,
                    IMAGE_PER_PAGE,
                    BACKGROUND,
                    VERTICAL_ORIENTATION,
                    PHOTO,
                    SAFE_SEARCH,
                    collection
                )
            }
            val response = withTimeoutOrNull(5000) {
                deferred.await()
            }

            when {
                response != null && response.isSuccessful -> {
                    response
                }
                response != null && !response.isSuccessful -> {
                    val errorMessage = response.body().toString()
                    errorCallback.onError(errorMessage)
                    Response.error(400, errorMessage.toResponseBody(null))
                }
                else -> {
                    errorCallback.onError(errorMessage)
                    Response.error(400, errorMessage.toResponseBody(null))
                }
            }
        } catch (e: Throwable) {
            when (e) {
                is CancellationException -> {}
                else -> {
                    errorCallback.onError(errorMessage)
                }
            }
            e.printStackTrace()
            Response.error(400, errorMessage.toResponseBody(null))
        }
    }

    interface ErrorCallback {
        fun onError(errorMessage: String)
    }
}
