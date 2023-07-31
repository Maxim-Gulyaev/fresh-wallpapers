package android.maxim.freshwallpapers.ui.settings

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentSettingsBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SettingsFragment: Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        binding.llAppearance.setOnClickListener {
            //TODO open dialog
            val appearanceDialogFragment = AppearanceDialogFragment()
            appearanceDialogFragment.show(parentFragmentManager, "AppearanceDialog")
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}