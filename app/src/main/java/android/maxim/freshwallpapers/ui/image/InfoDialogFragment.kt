package android.maxim.freshwallpapers.ui.image

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.maxim.freshwallpapers.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class InfoDialogFragment: DialogFragment(R.layout.fragment_info) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //transparent background to make visible rounded corners
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}