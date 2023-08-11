package android.maxim.freshwallpapers.ui.settings

import android.content.SharedPreferences
import android.maxim.freshwallpapers.di.DarkModePrefs
import android.maxim.freshwallpapers.utils.MODE_KEY
import android.maxim.freshwallpapers.utils.SYSTEM_MODE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppearanceDialogFragmentViewModel @Inject constructor(): ViewModel() {

    @Inject
    @DarkModePrefs
    lateinit var sharedPreferences: SharedPreferences
    private val _currentMode = MutableLiveData<Int>()
    val currentMode: LiveData<Int>
        get() = _currentMode

    fun getCurrentMode() {
        _currentMode.value = sharedPreferences.getInt(MODE_KEY, SYSTEM_MODE)
    }

    fun saveNewMode(mode: Int) {
        sharedPreferences.edit().putInt(MODE_KEY, mode).apply()
    }
}