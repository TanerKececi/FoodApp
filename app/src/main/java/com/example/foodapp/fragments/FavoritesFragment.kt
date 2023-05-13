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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.adapters.FavoriteMealsAdapter
import com.example.foodapp.databinding.FragmentFavoritesBinding
import com.example.foodapp.repository.local.MealEntity
import com.example.foodapp.repository.mappers.toMeal
import com.example.foodapp.viewmodel.FavoritesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "FavoritesFragment"

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FavoritesViewModel>()
    private lateinit var favoritesAdapter: FavoriteMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observeViewModel()
        viewModel.getFavoritesFromDB()
        setOnClickListeners()
        initItemTouchHelper()

    }

    private fun initItemTouchHelper() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = favoritesAdapter.getMeal(position)
                viewModel.deleteMeal(deletedMeal)
                favoritesAdapter.deleteMeal(position)
                favoritesAdapter.notifyItemRemoved(position)

                Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.insertMeal(deletedMeal)
                        favoritesAdapter.insertMeal(position, deletedMeal)
                        favoritesAdapter.notifyItemInserted(position)
                    }
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavoriteMeals)
    }

    private fun initRecyclerView() {
        favoritesAdapter = FavoriteMealsAdapter()
        binding.rvFavoriteMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.favoritesMealsLiveData.observe( viewLifecycleOwner,  object : Observer<List<MealEntity>> {
                override fun onChanged(value: List<MealEntity>) {
                    favoritesAdapter.setFavoriteMealsList(value.map {
                        it.toMeal()
                    })
                }
            })
    }

    private fun setOnClickListeners() {
        favoritesAdapter.onItemClick = { meal ->
            val bundle = bundleOf(
                HomeFragment.MEAL_ID to meal.idMeal,
                HomeFragment.MEAL_NAME to meal.strMeal,
                HomeFragment.MEAL_THUMB to meal.strMealThumb)
            this.findNavController().navigate(R.id.action_global_mealDetailFragment, bundle)
        }
    }
}