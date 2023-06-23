package android.maxim.freshwallpapers.di

import android.content.Context
import android.maxim.freshwallpapers.data.models.CategoriesList
import android.maxim.freshwallpapers.data.network.WallpapersApi
import android.maxim.freshwallpapers.utils.Constants
import android.maxim.freshwallpapers.utils.Constants.TAG
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

        Log.d(TAG, "AppModule.provideWallpapersApi()")
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.WALLPAPERSAPI_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return retrofit.create(WallpapersApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoriesList(): CategoriesList {
        return CategoriesList()
    }
}