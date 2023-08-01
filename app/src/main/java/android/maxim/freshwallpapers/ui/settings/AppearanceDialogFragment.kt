package android.maxim.freshwallpapers.ui.settings

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.app.FreshWallpapersApp
import android.maxim.freshwallpapers.databinding.FragmentDialogAppearanceBinding
import android.maxim.freshwallpapers.utils.*
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.DialogFragment

class AppearanceDialogFragment: DialogFragment(R.layout.fragment_dialog_appearance), OnClickListener {

    private var _binding: FragmentDialogAppearanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogAppearanceBinding.inflate(layoutInflater, container, false)

        /*val currentNightMode = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.rbLight.isChecked = true
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.rbDark.isChecked = true
            }
        }*/

        val sharedPreferences = FreshWallpapersApp.sharedPreferences

        when (sharedPreferences.getInt(MODE_KEY, SYSTEM_MODE)) {
            LIGHT_MODE -> binding.rbLight.isChecked = true
            DARK_MODE -> binding.rbDark.isChecked = true
            SYSTEM_MODE -> binding.rbSystem.isChecked = true
        }

        binding.rgAppearanceDialog.setOnCheckedChangeListener { _, checkedId ->
            val clickedButton = view?.findViewById<RadioButton>(checkedId)
            binding.apply {
                when (clickedButton) {
                    rbLight -> {
                        setDefaultNightMode(MODE_NIGHT_NO)
                        sharedPreferences.edit().putInt(MODE_KEY, LIGHT_MODE).apply()
                    }
                    rbDark -> {
                        setDefaultNightMode(MODE_NIGHT_YES)
                        sharedPreferences.edit().putInt(MODE_KEY, DARK_MODE).apply()
                    }
                    rbSystem -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
                        } else {
                            setDefaultNightMode(MODE_NIGHT_AUTO_BATTERY);
                        }
                        sharedPreferences.edit().putInt(MODE_KEY, SYSTEM_MODE).apply()
                    }
                }
            }
        }
        binding.btnAppearanceDialogOk.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        this.dismiss()
    }
}