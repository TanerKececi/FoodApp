package com.example.foodapp.fragments.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentMealBottomSheetBinding
import com.example.foodapp.fragments.HomeFragment
import com.example.foodapp.repository.remote.Meal
import com.example.foodapp.viewmodel.MealBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

private const val MEAL_ID = "param1"

@AndroidEntryPoint
class MealBottomSheetFragment: BottomSheetDialogFragment() {
    private var mealId: String? = null
    private var mealName: String? = null
    private var mealThumb: String? = null

    private var _binding : FragmentMealBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MealBottomSheetViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMealBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let { viewModel.getMealById(it) }
        observeViewModel()

        bottomSheetDialogClick()

    }

    private fun bottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {
            if (mealName != null && mealThumb != null) {
                val bundle = bundleOf(
                    HomeFragment.MEAL_ID to mealId,
                    HomeFragment.MEAL_NAME to mealName,
                    HomeFragment.MEAL_THUMB to mealThumb)
                this.findNavController().navigate(R.id.action_global_mealDetailFragment, bundle)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.mealDetailsLiveData.observe(viewLifecycleOwner, object: Observer<Meal> {
            override fun onChanged(value: Meal) {
                mealId = value.idMeal
                mealName = value.strMeal
                mealThumb = value.strMealThumb

                Glide.with(requireContext())
                    .load(mealThumb)
                    .into(binding.imgBottomSheet)

                binding.apply {
                    tvBottomMealName.text = mealName
                    tvBottomSheetCategory.text = value.strCategory
                    tvBottomSheetArea.text = value.strArea
                }
            }
        })
    }

    companion object {
        @JvmStatic fun newInstance(param1: String) =
                MealBottomSheetFragment().apply {
                    arguments = Bundle().apply {
                        putString(MEAL_ID, param1)
                    }
                }
    }
}