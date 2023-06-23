package android.maxim.freshwallpapers.data.repository

import android.maxim.freshwallpapers.data.network.WallpapersApi
import android.maxim.freshwallpapers.utils.Constants
import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class WallpapersRepository @Inject constructor(private val wallpapersApi: WallpapersApi) {

    fun getCategoriesList() {
        Log.d(Constants.TAG, "WallpapersRepository.getCategoriesList()")
        wallpapersApi.getCategoriesList(Constants.PIXABAY_API_KEY, "").enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}