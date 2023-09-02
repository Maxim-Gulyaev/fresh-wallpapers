package android.maxim.freshwallpapers.ui.collections

import android.content.res.Configuration
import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentCollectionsBinding
import android.maxim.freshwallpapers.ui.ImageSharedViewModel
import android.maxim.freshwallpapers.utils.COLLECTION_KEY
import android.maxim.freshwallpapers.utils.Constants
import android.maxim.freshwallpapers.utils.LIKED
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionsFragment: Fragment(R.layout.fragment_collections) {

    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!
    private val imageSharedViewModel: ImageSharedViewModel by viewModels()
    private var recyclerStateBundle: Bundle? = null
    private var recyclerState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.collectionsToolbar.apply {
            inflateMenu(R.menu.collections_toolbar_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_settings -> {
                        findNavController()
                            .navigate(R.id.action_collectionsFragment_to_settingsFragment)
                    }
                    R.id.action_liked_images -> {
                        showLikedImages()
                    }
                }
                false
            }
            val searchView = menu.findItem(R.id.action_search).actionView
                    as androidx.appcompat.widget.SearchView
            setupSearchView(searchView)
        }

        lifecycleScope.launch {
            imageSharedViewModel.getCategoriesList().observe(viewLifecycleOwner) { collection ->
                if (recyclerStateBundle != null) restoreRecyclerState()
                binding.recyclerCollections.apply {
                    layoutManager = GridLayoutManager(
                        activity,
                        2,
                        GridLayoutManager.VERTICAL,
                        false)
                    adapter = CollectionsAdapter(collection)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
            }
        }
    }

    override fun onStop() {
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
            }
        }
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerStateBundle = Bundle()
        val mListState = binding.recyclerCollections.layoutManager?.onSaveInstanceState()
        recyclerStateBundle!!.putParcelable(Constants.KEY_RECYCLER_STATE, mListState)
        _binding = null
    }

    private fun showLikedImages() {
        val bundle = Bundle()
        bundle.putString(COLLECTION_KEY, LIKED)
        Navigation
            .createNavigateOnClickListener(
                R.id.action_collectionsFragment_to_imagesListFragment,
                bundle)
            .onClick(binding.collectionsToolbar)
    }

    private fun setupSearchView(searchView: androidx.appcompat.widget.SearchView) {
        searchView.queryHint = resources.getString(R.string.search_images)
        searchView.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val bundle = Bundle()
                bundle.putString(COLLECTION_KEY, query)
                Navigation
                    .createNavigateOnClickListener(
                        R.id.action_collectionsFragment_to_imagesListFragment,
                        bundle)
                    .onClick(view)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun restoreRecyclerState() {
        lifecycleScope.launch(Dispatchers.Main) {
            //TODO Replace getParcelable method
            recyclerState = recyclerStateBundle!!.getParcelable(Constants.KEY_RECYCLER_STATE)
            binding.recyclerCollections.layoutManager?.onRestoreInstanceState(recyclerState)
        }
    }
}