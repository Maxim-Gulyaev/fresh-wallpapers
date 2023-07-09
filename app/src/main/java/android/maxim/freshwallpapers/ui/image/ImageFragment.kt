package android.maxim.freshwallpapers.ui.image

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.ui.collections.CollectionsViewModel
import android.maxim.freshwallpapers.utils.Constants.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageFragment: Fragment(R.layout.fragment_image) {

    private var largeImageURL: String? = null
    private val imageViewModel: ImageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        largeImageURL = arguments?.getString("largeImageURL")
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}