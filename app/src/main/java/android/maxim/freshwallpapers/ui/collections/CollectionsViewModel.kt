package android.maxim.freshwallpapers.ui.collections

import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var repository: WallpapersRepository

    suspend fun getCategoriesList() = liveData {
        repository.getCollectionsList().collect { value ->
            emit(value)
        }
    }
}