package android.maxim.freshwallpapers.ui.imagelist

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.databinding.ItemImageListBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class ImageListAdapter(private val imageList: List<Image>)
    : RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder>(){

    inner class ImageListViewHolder(private val itemBinding: ItemImageListBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(id: String) {
            itemBinding.tvImageTitle.text = id
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
        val id: String = imageList[position].id
        holder.bind(id)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", id)
            Navigation
                .createNavigateOnClickListener(
                    R.id.action_imagesListFragment_to_imageFragment,
                    bundle)
                .onClick(holder.itemView)
        }
    }

    override fun getItemCount(): Int = imageList.size


}