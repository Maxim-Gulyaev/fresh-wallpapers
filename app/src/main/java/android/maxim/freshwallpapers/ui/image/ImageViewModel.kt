package android.maxim.freshwallpapers.ui.image

import android.app.Application
import android.maxim.freshwallpapers.data.models.LikedImageMap
import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import android.maxim.freshwallpapers.utils.LikedImageHelper
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(application: Application)
    : AndroidViewModel(application) {

    @Inject
    lateinit var repository: WallpapersRepository
    //TODO move it to DI
    private val likedImageHelper = LikedImageHelper(application)

    fun getLikedImageMap(): LikedImageMap {
        return likedImageHelper
            .getLikedImageMap()
    }
}