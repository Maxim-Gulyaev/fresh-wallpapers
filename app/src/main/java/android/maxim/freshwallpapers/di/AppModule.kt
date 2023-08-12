package android.maxim.freshwallpapers.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.maxim.freshwallpapers.data.network.WallpapersApi
import android.maxim.freshwallpapers.utils.Constants
import android.maxim.freshwallpapers.utils.DARK_MODE_SHARE_PREFS
import android.maxim.freshwallpapers.utils.LIKED_IMAGE_PREFS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideWallpapersApi(): WallpapersApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.WALLPAPERSAPI_BASE_URL)
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
}