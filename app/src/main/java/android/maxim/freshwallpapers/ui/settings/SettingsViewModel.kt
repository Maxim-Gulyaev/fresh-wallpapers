package android.maxim.freshwallpapers.ui.settings

import android.content.SharedPreferences
import android.maxim.freshwallpapers.utils.MODE_KEY
import android.maxim.freshwallpapers.utils.SYSTEM_MODE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel() {

    private val _currentMode = MutableLiveData<Int>()
    val currentMode: LiveData<Int>
        get() = _currentMode

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    fun getCurrentMode() {
        _currentMode.value = sharedPreferences.getInt(MODE_KEY, SYSTEM_MODE)
    }
}