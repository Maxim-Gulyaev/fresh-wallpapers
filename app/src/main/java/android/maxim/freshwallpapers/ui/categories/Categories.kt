package android.maxim.freshwallpapers.ui.categories

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentCategoriesBinding
import android.maxim.freshwallpapers.databinding.ItemCategoriesBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

class Categories: Fragment(R.layout.fragment_categories) {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val categoriesViewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerCategories.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = CategoriesAdapter(categoriesViewModel.getCategoriesList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}