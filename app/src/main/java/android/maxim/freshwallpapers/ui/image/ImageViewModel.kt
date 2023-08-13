package android.maxim.freshwallpapers.ui.image

import android.maxim.freshwallpapers.data.models.LikedImageMap
import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import android.maxim.freshwallpapers.utils.LikedImageHelper
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var repository: WallpapersRepository
    @Inject
    lateinit var likedImageHelper: LikedImageHelper

    fun getLikedImageMap(): LikedImageMap {
        return likedImageHelper
            .getLikedImageMap()
    }
}