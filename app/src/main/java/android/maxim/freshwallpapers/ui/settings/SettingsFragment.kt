package android.maxim.freshwallpapers.ui.settings

import android.content.SharedPreferences
import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.app.FreshWallpapersApp
import android.maxim.freshwallpapers.databinding.FragmentSettingsBinding
import android.maxim.freshwallpapers.utils.DARK_MODE
import android.maxim.freshwallpapers.utils.LIGHT_MODE
import android.maxim.freshwallpapers.utils.MODE_KEY
import android.maxim.freshwallpapers.utils.SYSTEM_MODE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SettingsFragment: Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        //TODO move this instance to DI
        sharedPreferences = FreshWallpapersApp.sharedPreferences
        when (sharedPreferences.getInt(MODE_KEY, SYSTEM_MODE)) {
            LIGHT_MODE -> binding.tvCurrentMode.text = resources.getString(R.string.light)
            DARK_MODE -> binding.tvCurrentMode.text = resources.getString(R.string.dark)
            SYSTEM_MODE -> binding.tvCurrentMode.text = resources.getString(R.string.system)
        }

        binding.llAppearance.setOnClickListener {
            //TODO move this instance to DI
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