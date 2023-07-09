package android.maxim.freshwallpapers.ui.image

import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var repository: WallpapersRepository

}