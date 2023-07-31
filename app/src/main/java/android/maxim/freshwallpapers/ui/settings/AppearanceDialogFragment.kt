package android.maxim.freshwallpapers.ui.settings

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentDialogAppearanceBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.DialogFragment

class AppearanceDialogFragment: DialogFragment(R.layout.fragment_dialog_appearance) {

    private var _binding: FragmentDialogAppearanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogAppearanceBinding.inflate(layoutInflater, container, false)

        binding.rgAppearanceDialog.setOnCheckedChangeListener { _, checkedId ->
            val clickedButton = view?.findViewById<RadioButton>(checkedId)
            binding.apply {
                when (clickedButton) {
                    rbLight -> setDefaultNightMode(MODE_NIGHT_NO)
                    rbDark -> setDefaultNightMode(MODE_NIGHT_YES)
                    rbSystem -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }

        return binding.root
    }
}