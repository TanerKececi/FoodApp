package com.example.foodapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.repository.remote.MealsByCategory
import com.example.foodapp.repository.remote.MealsByCategoryList
import com.example.foodapp.service.MealApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "CategoryMealsViewModel"

@HiltViewModel
class CategoryMealsViewModel @Inject constructor(
    private val mealApi: MealApi
) : ViewModel() {

    private var _categoryMealsLiveData = MutableLiveData<List<MealsByCategory>>()
    val categoryMealsLiveData: LiveData<List<MealsByCategory>> get() = _categoryMealsLiveData

    fun getMealsByCategory(categoryName: String) {
        mealApi.getMealsByCategory(categoryName)
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        _categoryMealsLiveData.postValue(response.body()!!.meals)
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d(TAG, "getMealsByCategory failed with ${t.message}")
                }
            })
    }

}