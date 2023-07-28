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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


class WallpapersRepository @Inject constructor(@ApplicationContext context: Context) {

    private val hiltEntryPoint = EntryPoints.get(
        context.applicationContext,
        WallpapersRepositoryEntryPoint::class.java)
    private val wallpapersApi = hiltEntryPoint.wallpapersApi()
    //private var collectionsList = hiltEntryPoint.collectionsList()

    private val firestoreDb = Firebase.firestore
    private val firestoreReference = firestoreDb.collection("collections")

    /*fun getCollectionsList(): List<String> {
        Log.d(TAG, "WallpapersRepository.getCategoriesList()")
        return collectionsList.collectionsList
    }*/

    fun getCollectionsList(): Flow<List<WallpapersCollection>> {
        Log.d(TAG, "WallpapersRepository.getCategoriesList()")
        return flow<List<WallpapersCollection>> {
            /*val wallpapersCollection = WallpapersCollection("one", "someUrl")
            val wallpapersCollection2 = WallpapersCollection("two", "someUrl")
            val collectionsList = mutableListOf<WallpapersCollection>()
            collectionsList.add(wallpapersCollection)
            collectionsList.add(wallpapersCollection2)*/
            val collectionsList = mutableListOf<WallpapersCollection>()
            firestoreReference
                .get()
                .addOnSuccessListener { result ->
                for (document in result) {
                    val collection = WallpapersCollection(
                        document.getString("title").toString(),
                        document.getString("imageUrl").toString()
                    )
                    collectionsList.add(collection)
                }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
            Log.d(TAG, collectionsList.size.toString())
            emit(collectionsList)
        }.flowOn(Dispatchers.IO)
    }

        /*val list = mutableListOf<WallpapersCollection>()
        firestoreReference
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val wallpapersCollection = WallpapersCollection(
                            document.getString("title").toString(),
                            document.getString("imageUrl").toString())
                        list.add(wallpapersCollection)
                        list.asFlow()
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            })
        Log.d(TAG, list.size.toString())
        return list
    }*/

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
        }
        return response
    }
}
