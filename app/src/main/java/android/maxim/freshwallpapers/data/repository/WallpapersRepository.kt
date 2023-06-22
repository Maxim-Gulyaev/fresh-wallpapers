package android.maxim.freshwallpapers.data.repository

import android.maxim.freshwallpapers.data.network.WallpapersApi
import android.maxim.freshwallpapers.utils.Constants
import javax.inject.Inject


class WallpapersRepository @Inject constructor(wallpapersApi: WallpapersApi) {

    val categoriesList = wallpapersApi.getCategoriesList(Constants.PIXABAY_API_KEY)
}