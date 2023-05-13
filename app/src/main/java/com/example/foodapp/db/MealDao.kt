package com.example.foodapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import androidx.room.Upsert
import com.example.foodapp.repository.local.MealEntity

@Dao
interface MealDao {

    @Upsert
    suspend fun upsertMeal(meal: MealEntity)

    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Query("SELECT * FROM mealInformation")
    suspend fun getAllMeals(): List<MealEntity>

}