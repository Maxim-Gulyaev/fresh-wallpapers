package android.maxim.freshwallpapers.ui.image

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentImageBinding
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import com.squareup.picasso.Target
import java.io.IOException

@AndroidEntryPoint
class ImageFragment: Fragment(R.layout.fragment_image) {

    private var largeImageURL: String? = null
    private val imageViewModel: ImageViewModel by viewModels()
    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    //TODO Delete imageViewModel if will not use

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(layoutInflater, container, false)
        largeImageURL = arguments?.getString("largeImageURL")
        Picasso.get().load(largeImageURL).into(binding.ivImage)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_ios_black_24)
            setNavigationOnClickListener {
                findNavController().navigate(R.id.imageListFragment)
            }
            inflateMenu(R.menu.image_toolbar_menu)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_set_wallpaper) {
                    setWallpaper(getDisplayMetrics())
                }
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setWallpaper(displayMetrics: DisplayMetrics) {
        Picasso.get()
            .load(largeImageURL)
            .resize(displayMetrics.widthPixels, displayMetrics.heightPixels)
            .centerCrop()
            .into(object : Target {
                override fun onBitmapLoaded(resource: Bitmap, from: Picasso.LoadedFrom) {
                    try {
                        WallpaperManager
                            .getInstance(context)
                            .setBitmap(resource)
                        showToast(R.string.setWallpaper_done)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        showToast(R.string.setWallpaper_error)
                    }
                }
                override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {
                    e.printStackTrace()
                }
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
    }

    private fun showToast(resId: Int) {
        Toast.makeText(
            requireActivity(),
            requireActivity().getString(resId),
            Toast.LENGTH_LONG).show()
    }

    @Suppress("DEPRECATION")
    private fun getDisplayMetrics(): DisplayMetrics {
        val outMetrics = DisplayMetrics()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity?.display
            display?.getRealMetrics(outMetrics)
        } else {
            activity?.windowManager?.defaultDisplay?.getMetrics(outMetrics)
        }
        return outMetrics
    }
}