package android.maxim.freshwallpapers.ui.imageslist

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentImagesListBinding
import android.maxim.freshwallpapers.utils.Constants
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ImagesListFragment: Fragment(R.layout.fragment_images_list) {

    private var _binding: FragmentImagesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(Constants.TAG, "ImagesListFragment.onCreateView()")
        _binding = FragmentImagesListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(Constants.TAG, "ImagesListFragment.onViewCreated()")
        binding.imageListToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_ios_black_24)
            setNavigationOnClickListener {
                findNavController().navigate(R.id.categoriesFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(Constants.TAG, "CategoriesFragment.onDestroyView()")
        _binding = null
    }
}