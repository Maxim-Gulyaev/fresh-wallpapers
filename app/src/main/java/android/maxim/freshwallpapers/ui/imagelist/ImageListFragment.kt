package android.maxim.freshwallpapers.ui.imagelist

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.databinding.FragmentImageListBinding
import android.maxim.freshwallpapers.utils.Constants
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImageListFragment: Fragment(R.layout.fragment_image_list) {

    private var _binding: FragmentImageListBinding? = null
    private val binding get() = _binding!!
    private val imageListViewModel: ImageListViewModel by viewModels()
    private var collection: String? = null
    private var mBundleRecyclerViewState: Bundle? = null
    private var mListState: Parcelable? = null

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
            if (mBundleRecyclerViewState != null) {
                restoreRecyclerState()
            }
            initRecycler(imageList)
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
        mBundleRecyclerViewState = Bundle()
        val mListState = binding.recyclerImageList.layoutManager?.onSaveInstanceState()
        mBundleRecyclerViewState!!.putParcelable("KEY_RECYCLER_STATE", mListState)
        _binding = null
    }

    private fun initRecycler(imageList:  List<Image>) {
        binding.recyclerImageList.layoutManager = GridLayoutManager(
            activity,
            3,
            GridLayoutManager.VERTICAL,
            false)
        binding.recyclerImageList.adapter = ImageListAdapter(imageList)
    }

    private fun restoreRecyclerState() {
        lifecycleScope.launch(Dispatchers.Main) {
            mListState = mBundleRecyclerViewState!!.getParcelable("KEY_RECYCLER_STATE")
            binding.recyclerImageList.layoutManager?.onRestoreInstanceState(mListState)
        }
    }
}