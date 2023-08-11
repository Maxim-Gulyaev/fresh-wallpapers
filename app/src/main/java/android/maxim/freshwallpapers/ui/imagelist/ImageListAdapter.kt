package android.maxim.freshwallpapers.ui.imagelist

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.databinding.ItemImageListBinding
import android.maxim.freshwallpapers.utils.IMAGE_ID_KEY
import android.maxim.freshwallpapers.utils.LARGE_IMAGE_URL_KEY
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageListAdapter(private val imageList: List<Image>)
    : RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder>(){

    inner class ImageListViewHolder(private val itemBinding: ItemImageListBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(previewURL: String) {
            Glide
                .with(itemView.context)
                .load(previewURL)
                .centerCrop()
                .into(itemBinding.ivPreviewImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : ImageListViewHolder {
        val itemBinding = ItemImageListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ImageListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        val imageId: String = imageList[position].id
        val largeImageURL: String = imageList[position].largeImageURL
        val previewURL: String = imageList[position].previewURL
        holder.bind(previewURL)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(IMAGE_ID_KEY, imageId)
            bundle.putString(LARGE_IMAGE_URL_KEY, largeImageURL)
            Navigation
                .createNavigateOnClickListener(
                    R.id.action_imagesListFragment_to_imageFragment,
                    bundle)
                .onClick(holder.itemView)
        }
    }

    override fun getItemCount(): Int = imageList.size
}