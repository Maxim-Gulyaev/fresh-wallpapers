package android.maxim.freshwallpapers.ui.image

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.data.models.LikedImageMap
import android.maxim.freshwallpapers.databinding.FragmentImageBinding
import android.maxim.freshwallpapers.ui.ImageSharedViewModel
import android.maxim.freshwallpapers.utils.IMAGE_KEY
import android.maxim.freshwallpapers.utils.MessageUtils
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImageFragment: Fragment(R.layout.fragment_image) {

    @Inject
    lateinit var messageUtils: MessageUtils
    private lateinit var image: Image
    private lateinit var retrievedImageMap: LikedImageMap
    private val imageSharedViewModel: ImageSharedViewModel by viewModels()
    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionRequestLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionRequestLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                saveImage()
            } else {
                messageUtils.showToast(
                    requireActivity(),
                    resources.getString(R.string.no_permission)
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(layoutInflater, container, false)

        binding.progressBarImage.visibility = View.VISIBLE

        image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(IMAGE_KEY, Image::class.java)!!
        } else {
            arguments?.getParcelable(IMAGE_KEY)!!
        }

        //set initial icon for "like" button
        imageSharedViewModel.getLikedImageMap()
        imageSharedViewModel.imageMap.observe(viewLifecycleOwner) { imageMap ->
            retrievedImageMap = imageMap
            if (retrievedImageMap.likedImageMap.containsKey(image.id)) {
                binding.btnLike.setIconResource(R.drawable.outline_favorite_white_24)
            } else {
                binding.btnLike.setIconResource(R.drawable.outline_favorite_border_white_24)
            }
        }

        //set margins to prevent Toolbar and BottomAppBar overlapping with system bars
        setBarMargin(
            binding.imageToolbar,
            getStatusBarHeight())
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.R) {
            setBarMargin(
                binding.imageBottomAppBar,
                getNavigationBarHeight())
        }

        //gradient views are to ensure action icons visibility when background is light
        setGradientViewHeight(
            binding.viewToolbarGradient,
            getStatusBarHeight(),
            getBarHeight(binding.imageToolbar))
        setGradientViewHeight(
            binding.viewBottomAppBarGradient,
            getNavigationBarHeight(),
            getBarHeight(binding.imageBottomAppBar))

        binding.apply {
            btnApply.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable(IMAGE_KEY, image)
                Navigation
                    .createNavigateOnClickListener(
                        R.id.action_imageFragment_to_applyDialogFragment,
                        bundle)
                    .onClick(view)
            }
            btnLike.setOnClickListener {
                if (!retrievedImageMap.likedImageMap.containsKey(image.id)) {
                    imageSharedViewModel.addImageToLiked(image)
                    btnLike.setIconResource(R.drawable.outline_favorite_white_24)
                } else {
                    imageSharedViewModel.removeImageFromLiked(image)
                    btnLike.setIconResource(R.drawable.outline_favorite_border_white_24)
                }
            }
            btnSave.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED) {
                            saveImage()
                        } else {
                            permissionRequestLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                    } else {
                        saveImage()
                    }
                }
            }
            btnInfo.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable(IMAGE_KEY, image)
                Navigation
                    .createNavigateOnClickListener(
                        R.id.action_imageFragment_to_infoDialogFragment,
                        bundle)
                    .onClick(view)
            }
        }

        Glide
            .with(requireActivity())
            .load(image.largeImageURL)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    lifecycleScope.launch {
                        delay(5000)
                        messageUtils.showSnackbar(
                            binding.ivImage,
                            resources.getString(R.string.network_error_message),
                            resources.getString(R.string.got_it)
                        )
                    }
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBarImage.visibility = View.INVISIBLE
                    return false
                }
            })
            .into(binding.ivImage)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        view?.let {
            WindowCompat
                .getInsetsController(requireActivity().window, it)
                .isAppearanceLightNavigationBars = false }

        setUiForApi30()
        setStatusBarTextColor()

        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_ios_white_24)
            setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onStop() {
        view?.let {
            WindowCompat
                .getInsetsController(requireActivity().window, it)
                .isAppearanceLightNavigationBars = true }
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        cancelStatusBarWhiteText()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getStatusBarHeight(): Int {
        val rect = Rect()
        requireActivity().window.decorView.getWindowVisibleDisplayFrame(rect)
        val accidentValue = (24 * (resources.displayMetrics.density)).toInt()
        return if (rect.top != 0) rect.top else accidentValue
    }

    private fun getBarHeight(bar: View): Int {
        bar.apply {
            measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            return measuredHeight
        }
    }

    private fun getNavigationBarHeight(): Int {
        val resourceId: Int = resources.getIdentifier(
            "navigation_bar_height",
            "dimen",
            "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    private fun setBarMargin(bar: View, systemBarHeight: Int) {
        val params = bar.layoutParams as ViewGroup.MarginLayoutParams
        if (bar == binding.imageToolbar) {
            params.topMargin = systemBarHeight
        } else {
            params.bottomMargin = systemBarHeight
        }
        bar.layoutParams = params
    }

    private fun setGradientViewHeight(view: View, navigationBarHeight: Int, bottomAppBarHeight: Int) {
        val gradientViewHeight = navigationBarHeight + bottomAppBarHeight
        val params = view.layoutParams as ViewGroup.LayoutParams
        params.height = gradientViewHeight
        view.layoutParams = params
    }

    private fun setStatusBarTextColor() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    requireActivity().window.insetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                } else {
                    @Suppress("DEPRECATION")
                    requireActivity().window.decorView.systemUiVisibility = 0
                }
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    requireActivity().window.insetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                }
            }
        }
    }

    private fun cancelStatusBarWhiteText() {
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            == Configuration.UI_MODE_NIGHT_NO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
            } else {
                @Suppress("DEPRECATION")
                requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    private fun setUiForApi30() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            requireActivity().window.navigationBarColor = Color.TRANSPARENT
            requireActivity().window.statusBarColor = Color.TRANSPARENT
            WindowCompat
                .getInsetsController(requireActivity().window, requireActivity().window.decorView)
                .isAppearanceLightStatusBars = false
            requireActivity().window.isNavigationBarContrastEnforced = false
            setBarMargin(
                binding.imageBottomAppBar,
                0)
        }
    }

    private fun saveImage() {
        Glide.with(this)
            .asBitmap()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .load(image.largeImageURL)
            .override(
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels)
            .centerCrop()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                        try {
                            imageSharedViewModel.saveBitmapToExternalStorage(resource, image.id)
                        } catch (exception: Exception) {
                            messageUtils.showToast(
                                requireActivity(),
                                resources.getString(R.string.error_when_saving_image)
                            )
                            exception.printStackTrace()
                        }
                    } else {
                        try {
                            imageSharedViewModel.saveBitmapToMediaStore(requireActivity(), resource, image.id)
                        } catch (exception: Exception) {
                            messageUtils.showToast(
                                requireActivity(),
                                resources.getString(R.string.error_when_saving_image)
                            )
                            exception.printStackTrace()
                        }
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        messageUtils.showToast(
                            requireActivity(),
                            resources.getString(R.string.image_saved)
                        )
                    }
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    lifecycleScope.launch(Dispatchers.Main) {
                        messageUtils.showToast(
                            requireActivity(),
                            resources.getString(R.string.loading_error)
                        )
                    }
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}