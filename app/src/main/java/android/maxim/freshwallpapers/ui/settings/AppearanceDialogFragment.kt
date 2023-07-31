package android.maxim.freshwallpapers.ui.settings

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentDialogAppearanceBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
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
        binding.rbLight.setOnClickListener(this)
        binding.rbDark.setOnClickListener(this)
        binding.rbSystem.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.rbLight -> Toast.makeText(requireActivity(), "Light", Toast.LENGTH_SHORT).show()
            binding.rbDark -> Toast.makeText(requireActivity(), "Dark", Toast.LENGTH_SHORT).show()
            binding.rbSystem -> Toast.makeText(requireActivity(), "System", Toast.LENGTH_SHORT).show()
        }
    }
}