package com.example.foodapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.repository.local.MealEntity
import com.example.foodapp.repository.mappers.toMeal
import com.example.foodapp.repository.mappers.toMealEntity
import com.example.foodapp.repository.remote.Meal
import com.example.foodapp.service.MealApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val mealDatabase: MealDatabase,
    private val mealApi: MealApi
): ViewModel() {

    private var _favoritesMealsLiveData = MutableLiveData<List<MealEntity>>()
    val favoritesMealsLiveData: LiveData<List<MealEntity>> get() = _favoritesMealsLiveData


    fun getFavoritesFromDB() {
        viewModelScope.launch {
            val favorites = mealDatabase.mealDao().getAllMeals()
            if (favorites.isNotEmpty()) {
                _favoritesMealsLiveData.postValue(favorites)
            }
        }
    }

    @Synchronized
    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal.toMealEntity())
        }
    }

    @Synchronized
    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal.toMealEntity())
        }
    }

}