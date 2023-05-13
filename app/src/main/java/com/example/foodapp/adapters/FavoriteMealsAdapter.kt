package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.MealItemBinding
import com.example.foodapp.repository.remote.Meal
import com.example.foodapp.repository.remote.MealsByCategory

class FavoriteMealsAdapter: RecyclerView.Adapter<FavoriteMealsAdapter.FavoriteMealsViewHolder>() {

    inner class FavoriteMealsViewHolder(val binding: MealItemBinding): RecyclerView.ViewHolder(binding.root)
    lateinit var onItemClick: ((Meal) -> Unit)
    private var dataSet = ArrayList<Meal>()

    fun setFavoriteMealsList(mealList: List<Meal>) {
        this.dataSet = mealList as ArrayList<Meal>
        notifyDataSetChanged()
    }

    fun deleteMeal(index: Int) {
        dataSet.removeAt(index)
    }

    fun insertMeal(index: Int, meal: Meal) {
        dataSet.add(index, meal)
    }

    fun getMeal(position: Int): Meal {
        return dataSet[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealsViewHolder {
        return FavoriteMealsViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: FavoriteMealsViewHolder, position: Int) {
        val meal = dataSet[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.mealItemImage)
        holder.binding.mealItemName.text = meal.strMeal

        holder.itemView.setOnClickListener { onItemClick.invoke(meal) }
    }

}