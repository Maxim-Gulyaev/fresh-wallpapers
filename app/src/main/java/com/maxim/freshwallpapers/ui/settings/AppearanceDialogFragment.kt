package com.maxim.freshwallpapers.ui.settings

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.maxim.freshwallpapers.R
import com.maxim.freshwallpapers.databinding.FragmentDialogAppearanceBinding
import com.maxim.freshwallpapers.utils.DARK_MODE
import com.maxim.freshwallpapers.utils.LIGHT_MODE
import com.maxim.freshwallpapers.utils.SYSTEM_MODE
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppearanceDialogFragment: DialogFragment(R.layout.fragment_dialog_appearance) {

    private var _binding: FragmentDialogAppearanceBinding? = null
    private val binding get() = _binding!!
    private val appearanceDialogFragmentViewModel: AppearanceDialogFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogAppearanceBinding.inflate(layoutInflater, container, false)

        //transparent background to make visible rounded corners
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        appearanceDialogFragmentViewModel.getCurrentMode()
        appearanceDialogFragmentViewModel.currentMode.observe(viewLifecycleOwner, Observer { currentMode ->
            when (currentMode) {
                LIGHT_MODE -> binding.rbLight.isChecked = true
                DARK_MODE -> binding.rbDark.isChecked = true
                SYSTEM_MODE -> binding.rbSystem.isChecked = true
            }
        })
        
        binding.btnAppearanceDialogOk.setOnClickListener {
            when {
                binding.rbLight.isChecked -> {
                    setDefaultNightMode(MODE_NIGHT_NO)
                    appearanceDialogFragmentViewModel.saveNewMode(LIGHT_MODE)
                }
                binding.rbDark.isChecked -> {
                    setDefaultNightMode(MODE_NIGHT_YES)
                    appearanceDialogFragmentViewModel.saveNewMode(DARK_MODE)
                }
                binding.rbSystem.isChecked -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
                    } else {
                        setDefaultNightMode(MODE_NIGHT_AUTO_BATTERY);
                    }
                    appearanceDialogFragmentViewModel.saveNewMode(SYSTEM_MODE)
                }
            }
            findNavController().navigate(R.id.action_appearanceDialogFragment_to_settingsFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}