package android.maxim.freshwallpapers.ui.image

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentImageBinding
import android.maxim.freshwallpapers.utils.LARGE_IMAGE_URL_KEY
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        setToolbarTopMargin()

        largeImageURL = arguments?.getString(LARGE_IMAGE_URL_KEY)
        Glide
            .with(requireActivity())
            .load(largeImageURL)
            .into(binding.ivImage)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_ios_black_24)
            setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
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

    override fun onStop() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setWallpaper(displayMetrics: DisplayMetrics) {
        Glide
            .with(requireActivity())
            .asBitmap()
            .load(largeImageURL)
            .override(displayMetrics.widthPixels, displayMetrics.heightPixels)
            .centerCrop()
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    try {
                        WallpaperManager
                            .getInstance(context)
                            .setBitmap(resource)
                        lifecycleScope.launch(Dispatchers.Main) {
                            showToast(R.string.setWallpaper_done)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        lifecycleScope.launch(Dispatchers.Main) {
                            showToast(R.string.setWallpaper_error)
                        }
                    }
                    return false
                }
            })
            .submit()
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

    private fun setToolbarTopMargin() {
        val rect = Rect()
        requireActivity().window.decorView.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight = rect.top
        val params = binding.imageToolbar.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = statusBarHeight
        binding.imageToolbar.layoutParams = params
    }
}