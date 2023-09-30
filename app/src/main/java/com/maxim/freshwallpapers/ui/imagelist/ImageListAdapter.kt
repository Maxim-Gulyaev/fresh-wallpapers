package com.maxim.freshwallpapers.ui.imagelist

import com.maxim.freshwallpapers.R
import com.maxim.freshwallpapers.data.models.Image
import com.maxim.freshwallpapers.databinding.ItemImageListBinding
import com.maxim.freshwallpapers.utils.IMAGE_KEY
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ImageListAdapter(private val imageList: List<Image>)
    : RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder>(){

    inner class ImageListViewHolder(private val itemBinding: ItemImageListBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(previewURL: String) {
            Glide
                .with(itemView.context)
                .load(previewURL)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
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
        val rawPreviewURL: String = imageList[position].previewURL
        val resizedPreviewURL = rawPreviewURL.replace(
            "_640",
            "_340"
        )
        holder.bind(resizedPreviewURL)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(IMAGE_KEY, imageList[position])
            Navigation
                .createNavigateOnClickListener(
                    R.id.action_imagesListFragment_to_imageFragment,
                    bundle)
                .onClick(holder.itemView)
        }
    }

    override fun getItemCount(): Int = imageList.size
}