package android.maxim.freshwallpapers.ui.image

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentImageBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_ios_black_24)
            setNavigationOnClickListener {
                findNavController().navigate(R.id.imageListFragment)
            }
            inflateMenu(R.menu.image_toolbar_menu)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}