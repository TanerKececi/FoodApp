package com.example.foodapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.repository.local.MealEntity
import com.example.foodapp.repository.remote.Category
import com.example.foodapp.repository.remote.CategoryList
import com.example.foodapp.repository.remote.MealsByCategoryList
import com.example.foodapp.repository.remote.MealsByCategory
import com.example.foodapp.repository.remote.Meal
import com.example.foodapp.repository.remote.MealList
import com.example.foodapp.service.MealApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mealApi: MealApi,
    private val mealDataBase: MealDatabase
) : ViewModel() {

    private var _randomMealLiveData = MutableLiveData<Meal>()
    val randomMealLiveData: LiveData<Meal> get() = _randomMealLiveData

    private var _popularMealsLiveData = MutableLiveData<List<MealsByCategory>>()
    val popularMealsLiveData: LiveData<List<MealsByCategory>> get() = _popularMealsLiveData

    private var _categoryListLiveData = MutableLiveData<List<Category>>()
    val categoryListLiveData: LiveData<List<Category>> get() = _categoryListLiveData

    fun getRandomMeal() {
        mealApi.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    Log.d(
                        TAG,
                        "random meal id: ${randomMeal.idMeal}, random meal name: ${randomMeal.strMeal}"
                    )
                    _randomMealLiveData.postValue(randomMeal)
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d(TAG, "getRandomMeal failed with: ${t.message}")
            }

        })
    }

    fun getPopularMeals() {
        mealApi.getPopularItems("SeaFood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        _popularMealsLiveData.postValue(response.body()!!.meals)
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d(TAG, "getPopularItems failed with: ${t.message}")
                }

            })
    }

    fun getCategories() {
        mealApi.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null) {
                    _categoryListLiveData.postValue(response.body()!!.categories)
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d(TAG, "getCategories failed with: ${t.message}")
            }
        })
    }

    companion object {
        private val TAG = "HomeViewModel"
    }
}