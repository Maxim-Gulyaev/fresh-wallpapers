package android.maxim.freshwallpapers.ui.image

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.databinding.FragmentDialogApplyBinding
import android.maxim.freshwallpapers.utils.IMAGE_KEY
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApplyDialogFragment: DialogFragment(R.layout.fragment_dialog_apply) {

    private var _binding: FragmentDialogApplyBinding? = null
    private val binding get() = _binding!!
    private lateinit var image: Image
    private var toastMessage: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogApplyBinding.inflate(layoutInflater, container, false)

        //transparent background to make visible rounded corners
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //todo: consider to create method for it, it repeats several times(maybe move it to di)
        image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(IMAGE_KEY, Image::class.java)!!
        } else {
            arguments?.getParcelable(IMAGE_KEY)!!
        }

        binding.btnApplyDialogCancel.setOnClickListener {
            this.dismiss()
        }
        binding.btnApplyDialogOk.setOnClickListener {
            showProgressBar()
            lifecycleScope.launch(Dispatchers.IO) {
                setWallpaper()
            }
        }

        return binding.root
    }

    private fun setWallpaper() {
        Glide
            .with(requireActivity())
            .asBitmap()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .load(image.largeImageURL)
            .override(
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels)
            .centerCrop()
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    showToast(R.string.loading_error)
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
                        when {
                            binding.rbHomeScreen.isChecked -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    WallpaperManager
                                        .getInstance(context)
                                        .setBitmap(resource, null, true, WallpaperManager.FLAG_SYSTEM)
                                } else {
                                    WallpaperManager
                                        .getInstance(context)
                                        .setBitmap(resource)
                                }
                            }
                            binding.rbLockScreen.isChecked -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    WallpaperManager
                                        .getInstance(context)
                                        .setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK)
                                } else {
                                    WallpaperManager
                                        .getInstance(context)
                                        .setBitmap(resource)
                                }
                            }
                            binding.rbBothScreens.isChecked -> {
                                WallpaperManager
                                    .getInstance(context)
                                    .setBitmap(resource)
                            }
                        }
                        toastMessage = R.string.setWallpaper_done
                    } catch (e: Exception) {
                        e.printStackTrace()
                        toastMessage = R.string.setWallpaper_error
                    } finally {
                        this@ApplyDialogFragment.dismiss()
                    }
                    return false
                }
            })
            .submit()
    }

    override fun onStop() {
        super.onStop()
        toastMessage?.let { showToast(it) }
    }

    private fun showToast(messageResId: Int) {
        Toast.makeText(
            requireActivity(),
            requireActivity().getString(messageResId),
            Toast.LENGTH_LONG).show()
    }

    private fun showProgressBar() {
        binding.apply {
            rgApplyDialog.visibility = INVISIBLE
            viewApplyBackground.visibility = INVISIBLE
            btnApplyDialogOk.visibility = INVISIBLE
            btnApplyDialogCancel.visibility = INVISIBLE
            tvApplyTitle.visibility = INVISIBLE
            progressBarApply.visibility = VISIBLE
        }
    }
}