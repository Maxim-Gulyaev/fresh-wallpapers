package android.maxim.freshwallpapers.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.maxim.freshwallpapers.data.network.CacheInterceptor
import android.maxim.freshwallpapers.data.network.WallpapersApi
import android.maxim.freshwallpapers.utils.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideWallpapersApi(@ApplicationContext appContext: Context): WallpapersApi {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .cache(Cache(File(appContext.cacheDir, "http-cache"), 15L * 1024L * 1024L))
            .addNetworkInterceptor(CacheInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(WALLPAPERSAPI_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(WallpapersApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    @DarkModePrefs
    fun provideDarkModePrefs(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(
            DARK_MODE_SHARE_PREFS,
            Application.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    @LikedImagesPrefs
    fun provideLikedImagesPrefs(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(
            LIKED_IMAGE_PREFS,
            Application.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideGson(): Gson{
        return Gson()
    }

    @Provides
    @Singleton
    fun provideLikedImageHelper(@ApplicationContext appContext: Context): LikedImageHelper{
        return LikedImageHelper(appContext)
    }
}