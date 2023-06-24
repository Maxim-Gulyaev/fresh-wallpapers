package android.maxim.freshwallpapers.ui.categories

import android.maxim.freshwallpapers.R
import android.maxim.freshwallpapers.databinding.FragmentCategoriesBinding
import android.maxim.freshwallpapers.utils.Constants.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class CategoriesFragment: Fragment(R.layout.fragment_categories) {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val categoriesViewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "CategoriesFragment.onCreateView()")
        _binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "CategoriesFragment.onViewCreated()")
        binding.recyclerCategories.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = CategoriesAdapter(categoriesViewModel.getCategoriesList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "CategoriesFragment.onDestroyView()")
        _binding = null
    }
}