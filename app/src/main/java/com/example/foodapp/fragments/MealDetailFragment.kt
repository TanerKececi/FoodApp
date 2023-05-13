package com.example.foodapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.FragmentMealDetailBinding
import com.example.foodapp.repository.remote.Meal
import com.example.foodapp.viewmodel.MealViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealDetailFragment : Fragment() {

    private var _binding: FragmentMealDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var meal: Meal
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeUrl: String

    private val viewModel by viewModels<MealViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMealDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingCase()

        getMealInformationFromNav()
        setInformationInViews()
        viewModel.getMealDetail(mealId)

        observeViewModel()
        setOnClickListeners()
    }

    private fun setInformationInViews() {
        Glide.with(this)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName

    }

    private fun getMealInformationFromNav() {
        mealId = arguments?.getString(HomeFragment.MEAL_ID).toString()
        mealName = arguments?.getString(HomeFragment.MEAL_NAME).toString()
        mealThumb = arguments?.getString(HomeFragment.MEAL_THUMB).toString()
    }

    private fun observeViewModel() {
        viewModel.mealDetailsLiveData.observe(requireActivity(), object: Observer<Meal> {
            override fun onChanged(value: Meal) {
                meal = value
                onResponseCase()
                binding.apply {
                    categoryTv.text = "Category: ${value.strCategory}"
                    areaTv.text ="Area: ${value.strArea}"
                    instructionsBody.text = value.strInstructions
                    youtubeUrl = value.strYoutube.toString()
                }
            }
        })
    }

    private fun loadingCase() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            addFavoriteBtn.visibility = View.INVISIBLE
            instructionsBody.visibility = View.INVISIBLE
            instructionsHeader.visibility = View.INVISIBLE
            categoryTv.visibility = View.INVISIBLE
            areaTv.visibility = View.INVISIBLE
            imgYoutube.visibility = View.INVISIBLE
        }
    }

    private fun onResponseCase() {
        binding.apply {
            progressBar.visibility = View.GONE
            addFavoriteBtn.visibility = View.VISIBLE
            instructionsBody.visibility = View.VISIBLE
            instructionsHeader.visibility = View.VISIBLE
            categoryTv.visibility = View.VISIBLE
            areaTv.visibility = View.VISIBLE
            imgYoutube.visibility = View.VISIBLE
        }
    }

    private fun setOnClickListeners() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
            startActivity(intent)
        }

        binding.addFavoriteBtn.setOnClickListener {
            viewModel.insertMeal(meal)
            Toast.makeText(requireContext(), "Meal Saved", Toast.LENGTH_SHORT).show()
        }
    }

}