package android.maxim.freshwallpapers.ui.settings

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentSettingsBinding
import android.maxim.freshwallpapers.utils.DARK_MODE
import android.maxim.freshwallpapers.utils.LIGHT_MODE
import android.maxim.freshwallpapers.utils.SYSTEM_MODE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        settingsViewModel.getCurrentMode()

        binding.llAppearance.setOnClickListener {
            //TODO move this instance to DI
            val appearanceDialogFragment = AppearanceDialogFragment()
            appearanceDialogFragment.show(parentFragmentManager, "AppearanceDialog")
        }
        return binding.root
    }

    override fun onResume() {
        settingsViewModel.currentMode.observe(viewLifecycleOwner, Observer { currentMode ->
            when (currentMode) {
                LIGHT_MODE -> binding.tvCurrentMode.text = resources.getString(R.string.light)
                DARK_MODE -> binding.tvCurrentMode.text = resources.getString(R.string.dark)
                SYSTEM_MODE -> binding.tvCurrentMode.text = resources.getString(R.string.system)
            }
        })
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}