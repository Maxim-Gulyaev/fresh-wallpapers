package android.maxim.freshwallpapers.ui.settings

import android.content.SharedPreferences
import android.maxim.freshwallpapers.app.FreshWallpapersApp
import android.maxim.freshwallpapers.utils.MODE_KEY
import android.maxim.freshwallpapers.utils.SYSTEM_MODE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {

    private val _currentMode = MutableLiveData<Int>()
    val currentMode: LiveData<Int>
        get() = _currentMode

    //TODO move this instance to DI
    private val sharedPreferences: SharedPreferences = FreshWallpapersApp.sharedPreferences

    fun getCurrentMode() {
        _currentMode.value = sharedPreferences.getInt(MODE_KEY, SYSTEM_MODE)
    }
}