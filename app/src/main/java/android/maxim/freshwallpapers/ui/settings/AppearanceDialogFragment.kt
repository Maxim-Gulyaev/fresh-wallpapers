package android.maxim.freshwallpapers.ui.settings

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentDialogAppearanceBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
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
                    rbLight -> Toast.makeText(requireActivity(), "Light", Toast.LENGTH_SHORT).show()
                    rbDark -> Toast.makeText(requireActivity(), "Dark", Toast.LENGTH_SHORT).show()
                    rbSystem -> Toast.makeText(requireActivity(), "System", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }
}