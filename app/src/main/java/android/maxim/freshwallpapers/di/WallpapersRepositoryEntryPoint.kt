package android.maxim.freshwallpapers.di

import android.maxim.freshwallpapers.data.network.WallpapersApi
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WallpapersRepositoryEntryPoint {
    fun wallpapersApi(): WallpapersApi
    fun firestoreDb(): FirebaseFirestore
}