package android.maxim.freshwallpapers.app

import android.app.Application
import android.content.SharedPreferences
import android.maxim.freshwallpapers.utils.DARK_MODE_SHARE_PREFS
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FreshWallpapersApp: Application() {

    companion object {
        lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        sharedPreferences = getSharedPreferences(DARK_MODE_SHARE_PREFS, MODE_PRIVATE)
        super.onCreate()
    }
}