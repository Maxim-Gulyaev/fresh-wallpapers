package android.maxim.freshwallpapers.ui.collections

import android.content.res.Configuration
import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentCollectionsBinding
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
            collectionsViewModel.getCategoriesList().observe(viewLifecycleOwner) { collection ->
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
        _binding = null
    }
}