package com.example.foodapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodapp.repository.local.MealEntity

@Database(
    entities = [MealEntity::class],
    version = 1
)
@TypeConverters(MealTypeConvertor::class)
abstract class MealDatabase: RoomDatabase() {

    abstract fun mealDao(): MealDao

}