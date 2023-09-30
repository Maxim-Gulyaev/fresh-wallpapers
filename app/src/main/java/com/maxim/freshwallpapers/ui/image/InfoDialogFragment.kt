package com.maxim.freshwallpapers.ui.image

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.maxim.freshwallpapers.R
import com.maxim.freshwallpapers.data.models.Image
import com.maxim.freshwallpapers.databinding.FragmentInfoBinding
import com.maxim.freshwallpapers.utils.IMAGE_KEY
import com.maxim.freshwallpapers.utils.SOURCE
import android.os.Build
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class InfoDialogFragment: DialogFragment(R.layout.fragment_info) {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var image: Image

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(layoutInflater, container, false)

        //transparent background to make visible rounded corners
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(IMAGE_KEY, Image::class.java)!!
        } else {
            arguments?.getParcelable(IMAGE_KEY)!!
        }
        fillTextViews()
        binding.btnInfoDialogBack.setOnClickListener {
            this.dismiss()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fillTextViews() {
        binding.apply {
            tvSource.text = resources.getString(R.string.source, SOURCE)
            tvId.text = resources.getString(R.string.id, image.id)
            tvAuthor.text = resources.getString(R.string.author, image.user)
            tvSourceReference.text = resources.getString(R.string.source_reference, image.pageURL)
            Linkify.addLinks(tvSourceReference, Linkify.WEB_URLS)
        }
    }
}