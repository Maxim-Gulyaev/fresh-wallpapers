package com.maxim.freshwallpapers.ui.collections

import com.maxim.freshwallpapers.R
import com.maxim.freshwallpapers.data.models.WallpapersCollection
import com.maxim.freshwallpapers.databinding.ItemCollectionsBinding
import com.maxim.freshwallpapers.utils.COLLECTION_KEY
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class CollectionsAdapter(private val collectionsList: List<WallpapersCollection>)
    : RecyclerView.Adapter<CollectionsAdapter.CollectionsAdapterViewHolder>() {

    inner class CollectionsAdapterViewHolder(private val itemBinding: ItemCollectionsBinding)
        : ViewHolder(itemBinding.root) {
        fun bind(title: String, previewUrl: String) {
            itemBinding.tvCollection.text = title
            Glide
                .with(itemView.context)
                .load(previewUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(itemBinding.ivCollectionPreview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : CollectionsAdapterViewHolder {
        val itemBinding = ItemCollectionsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return CollectionsAdapterViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CollectionsAdapterViewHolder, position: Int) {
        val title: String = collectionsList[position].title
        val previewUrl: String = collectionsList[position].imageUrl
        holder.bind(title, previewUrl)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(COLLECTION_KEY, title)
            Navigation
                .createNavigateOnClickListener(
                    R.id.action_collectionsFragment_to_imagesListFragment,
                    bundle)
                .onClick(holder.itemView)
        }
    }

    override fun getItemCount(): Int = collectionsList.size

}