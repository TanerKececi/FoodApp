package com.example.foodapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.repository.mappers.toMealEntity
import com.example.foodapp.repository.remote.Meal
import com.example.foodapp.repository.remote.MealList
import com.example.foodapp.service.MealApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "MealViewModel"

@HiltViewModel
class MealViewModel @Inject constructor(
    private val mealDatabase: MealDatabase,
    private val mealApi: MealApi
): ViewModel() {

    private var _mealDetailsLiveData = MutableLiveData<Meal>()
    val mealDetailsLiveData: LiveData<Meal> get() = _mealDetailsLiveData

    fun getMealDetail(id: String) {
        mealApi.getMealById(id).enqueue(object: Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    _mealDetailsLiveData.value = response.body()!!.meals[0]
                } else {
                    return
                }

            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d(TAG, "Failed to get meal details with: ${t.message}")
            }

        })
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal.toMealEntity())
        }
    }

}