package android.maxim.freshwallpapers.ui.collections

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.data.models.WallpapersCollection
import android.maxim.freshwallpapers.databinding.FragmentCollectionsBinding
import android.maxim.freshwallpapers.utils.Constants
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CollectionsFragment: Fragment(R.layout.fragment_collections) {

    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!
    private val collectionsViewModel: CollectionsViewModel by viewModels()
    private val firestoreDb = Firebase.firestore
    private val firestoreReference = firestoreDb.collection("database").document("check")

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
            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_settings) {
                    findNavController().navigate(R.id.action_collectionsFragment_to_settingsFragment)
                }
                false
            }
        }

        lifecycleScope.launch {
            collectionsViewModel.getCategoriesList().observe(viewLifecycleOwner) { it ->
                binding.recyclerCollections.apply {
                    layoutManager = GridLayoutManager(
                        activity,
                        2,
                        GridLayoutManager.VERTICAL,
                        false)
                    adapter = CollectionsAdapter(it)
                }
            }
        }

        /*binding.recyclerCollections.apply {
            layoutManager = GridLayoutManager(
                activity,
                2,
                GridLayoutManager.VERTICAL,
                false)
            adapter = CollectionsAdapter(collectionsViewModel.getCategoriesList())
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}