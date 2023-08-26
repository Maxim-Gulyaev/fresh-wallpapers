package android.maxim.freshwallpapers.ui.image

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.databinding.FragmentInfoBinding
import android.maxim.freshwallpapers.utils.IMAGE_KEY
import android.maxim.freshwallpapers.utils.SOURCE
import android.os.Build
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController

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
        }
        Linkify.addLinks(binding.tvSourceReference, Linkify.WEB_URLS)
    }
}