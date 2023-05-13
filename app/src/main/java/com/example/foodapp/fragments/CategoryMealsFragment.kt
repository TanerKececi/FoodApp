package com.example.foodapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapters.CategoryMealsAdapter
import com.example.foodapp.databinding.FragmentCategoryMealsBinding
import com.example.foodapp.viewmodel.CategoryMealsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryMealsFragment : Fragment() {

    private var _binding: FragmentCategoryMealsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CategoryMealsViewModel>()
    private var categoryName: String? = null
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        categoryName = arguments?.getString(HomeFragment.CATEGORY_NAME)
        if (categoryName != null) {
            viewModel.getMealsByCategory(categoryName!!)
        }

        observeViewModel()
    }

    private fun initRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }

        categoryMealsAdapter.onItemClick = { meal ->
            val bundle = bundleOf(
                HomeFragment.MEAL_ID to meal.idMeal,
                HomeFragment.MEAL_NAME to meal.strMeal,
                HomeFragment.MEAL_THUMB to meal.strMealThumb)
            this.findNavController().navigate(R.id.action_global_mealDetailFragment, bundle)
        }


    }

    private fun observeViewModel() {
        viewModel.categoryMealsLiveData.observe(requireActivity(), Observer { mealsList ->
            binding.tvCategoryCount.text = "Number of meals : ${mealsList.size}"
            categoryMealsAdapter.setMealsList(mealsList)
        })
    }
}