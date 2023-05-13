package com.example.foodapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.adapters.CategoriesAdapter
import com.example.foodapp.adapters.MostPopularAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.fragments.bottomsheet.MealBottomSheetFragment
import com.example.foodapp.repository.remote.Category
import com.example.foodapp.repository.remote.MealsByCategory
import com.example.foodapp.repository.remote.Meal
import com.example.foodapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPopularItemsRecyclerView()
        initCategoriesRecyclerView()

        viewModel.getRandomMeal()
        viewModel.getPopularMeals()
        viewModel.getCategories()

        observeViewModel()
        setOnClickListeners()

    }

    private fun initPopularItemsRecyclerView() {
        binding.recViewPopularMeals.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun initCategoriesRecyclerView() {
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.randomMealLiveData.observe(viewLifecycleOwner, object: Observer<Meal> {
            override fun onChanged(value: Meal) {
                Glide.with(this@HomeFragment)
                    .load(value.strMealThumb)
                    .into(binding.imgRandomMeal)

                randomMeal = value
            }
        })

        viewModel.popularMealsLiveData.observe(viewLifecycleOwner, object: Observer<List<MealsByCategory>> {
            override fun onChanged(value: List<MealsByCategory>) {
                popularItemsAdapter.setMeals(value as ArrayList<MealsByCategory>)
            }

        })

        viewModel.categoryListLiveData.observe(viewLifecycleOwner, object: Observer<List<Category>> {
            override fun onChanged(value: List<Category>) {
                categoriesAdapter.setCategoryList(value)
            }
        })
    }

    private fun setOnClickListeners() {
        binding.cardRandomMeal.setOnClickListener { navToMealDetails() }

        popularItemsAdapter.onItemClick = { meal ->
            val bundle = bundleOf(
                MEAL_ID to meal.idMeal,
                MEAL_NAME to meal.strMeal,
                MEAL_THUMB to meal.strMealThumb)
            this.findNavController().navigate(R.id.action_global_mealDetailFragment, bundle)
        }

        categoriesAdapter.onItemClick = {category ->
            val bundle = bundleOf(CATEGORY_NAME to category.strCategory)
            this.findNavController().navigate(R.id.action_homeFragment_to_categoryMealsFragment, bundle)
        }

        popularItemsAdapter.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }

    }

    private fun navToMealDetails() {
        val bundle = bundleOf(
            MEAL_ID to randomMeal.idMeal,
            MEAL_NAME to randomMeal.strMeal,
            MEAL_THUMB to randomMeal.strMealThumb)
        this.findNavController().navigate(R.id.action_global_mealDetailFragment, bundle)
    }

    companion object{
        const val MEAL_ID = "MEAL_ID"
        const val MEAL_NAME = "MEAL_NAME"
        const val MEAL_THUMB = "MEAL_THUMB"
        const val CATEGORY_NAME = "CATEGORY_NAME"

    }

}