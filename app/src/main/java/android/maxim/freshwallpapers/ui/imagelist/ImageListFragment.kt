package android.maxim.freshwallpapers.ui.imagelist

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentImageListBinding
import android.maxim.freshwallpapers.utils.Constants
import android.maxim.freshwallpapers.utils.GridSpacing
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageListFragment: Fragment(R.layout.fragment_image_list) {

    private var _binding: FragmentImageListBinding? = null
    private val binding get() = _binding!!
    private val imageListViewModel: ImageListViewModel by viewModels()
    private var collection: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(Constants.TAG, "ImageListFragment.onCreateView()")
        _binding = FragmentImageListBinding.inflate(layoutInflater, container, false)

        collection = arguments?.getString("category")
        imageListViewModel.getImageList(collection!!)

        imageListViewModel.imageList.observe(viewLifecycleOwner, Observer { imageList ->
            val spacing = resources.getDimensionPixelSize(R.dimen.spacing)
            binding.recyclerImageList.also {
                it.layoutManager = GridLayoutManager(
                    activity,
                    2,
                    GridLayoutManager.VERTICAL,
                    false)
                it.addItemDecoration(GridSpacing(spacing))
                it.adapter = ImageListAdapter(imageList)
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(Constants.TAG, "ImageListFragment.onViewCreated()")
        val toolbarTitle = collection?.replaceFirstChar(Char::titlecase)
        binding.imageListToolbar.apply {
            title = toolbarTitle
            setNavigationIcon(R.drawable.baseline_arrow_back_ios_black_24)
            setNavigationOnClickListener {
                findNavController().navigate(R.id.collectionsFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(Constants.TAG, "ImageListFragment.onDestroyView()")
        _binding = null
    }
}