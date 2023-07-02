package android.maxim.freshwallpapers.ui.categories

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.ItemCategoriesBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CategoriesAdapter(private val categoriesList: List<String>): RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterViewHolder>() {

    inner class CategoriesAdapterViewHolder(private val itemBinding: ItemCategoriesBinding): ViewHolder(itemBinding.root) {
        fun bind(title: String) {
            itemBinding.tvCategory.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapterViewHolder {
        val itemBinding = ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesAdapterViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CategoriesAdapterViewHolder, position: Int) {
        val title: String = categoriesList[position]
        holder.bind(title)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("category", title)
            Navigation.createNavigateOnClickListener(R.id.action_categoriesFragment_to_imagesListFragment, bundle).onClick(holder.itemView)
        }
    }

    override fun getItemCount(): Int = categoriesList.size

}