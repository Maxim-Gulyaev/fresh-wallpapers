package android.maxim.freshwallpapers.ui.image

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentImageBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageFragment: Fragment(R.layout.fragment_image) {

    private var largeImageURL: String? = null
    private val imageViewModel: ImageViewModel by viewModels()
    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(layoutInflater, container, false)
        largeImageURL = arguments?.getString("largeImageURL")
        Picasso.get().load(largeImageURL).into(binding.ivImage)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}