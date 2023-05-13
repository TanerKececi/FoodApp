package com.example.foodapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.repository.remote.Category
import com.example.foodapp.repository.remote.CategoryList
import com.example.foodapp.service.MealApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "CategoryViewModel"

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val mealApi: MealApi,
    private val mealDataBase: MealDatabase
): ViewModel() {

    private var _categoryListLiveData = MutableLiveData<List<Category>>()
    val categoryListLiveData: LiveData<List<Category>> get() = _categoryListLiveData

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
}