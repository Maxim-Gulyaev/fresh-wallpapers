package android.maxim.freshwallpapers.ui.collections

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentCollectionsBinding
import android.maxim.freshwallpapers.utils.Constants.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionsFragment: Fragment(R.layout.fragment_collections) {

    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!
    private val collectionsViewModel: CollectionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "CollectionsFragment.onCreateView()")
        _binding = FragmentCollectionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "CollectionsFragment.onViewCreated()")
        binding.collectionsToolbar.apply {
            inflateMenu(R.menu.collections_toolbar_menu)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_settings) {
                    findNavController().navigate(R.id.action_collectionsFragment_to_settingsFragment)
                }
                false
            }
        }
        binding.recyclerCollections.apply {
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            adapter = CollectionsAdapter(collectionsViewModel.getCategoriesList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "CollectionsFragment.onDestroyView()")
        _binding = null
    }
}