package android.maxim.freshwallpapers.ui.collections

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.ItemCollectionsBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CollectionsAdapter(private val collectionsList: List<String>)
    : RecyclerView.Adapter<CollectionsAdapter.CollectionsAdapterViewHolder>() {

    class CollectionsAdapterViewHolder(
        val itemBinding: ItemCollectionsBinding
        ): ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : CollectionsAdapterViewHolder {
        val itemBinding = ItemCollectionsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionsAdapterViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CollectionsAdapterViewHolder, position: Int) {
        val title: String = collectionsList[position]
        with(holder) {
            itemBinding.tvCollection.text = title
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("category", title)
                Navigation.createNavigateOnClickListener(
                    R.id.action_collectionsFragment_to_imagesListFragment,
                    bundle
                ).onClick(holder.itemView)
            }
        }
    }

    override fun getItemCount(): Int = collectionsList.size
}