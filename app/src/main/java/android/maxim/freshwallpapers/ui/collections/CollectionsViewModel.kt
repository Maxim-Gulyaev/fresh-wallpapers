package android.maxim.freshwallpapers.ui.collections

import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import android.maxim.freshwallpapers.utils.Constants
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var repository: WallpapersRepository

    fun getCategoriesList(): List<String> {
        Log.d(Constants.TAG, "CollectionsViewModel.getCategoriesList()")
        return repository.getCategoriesList()
    }
}