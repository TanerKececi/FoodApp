package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.MealItemBinding
import com.example.foodapp.repository.remote.MealsByCategory

class CategoryMealsAdapter: RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() {

    private var dataSet = ArrayList<MealsByCategory>()
    lateinit var onItemClick: ((MealsByCategory) -> Unit)


    fun setMealsList(mealsList: List<MealsByCategory>) {
        this.dataSet = mealsList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    inner class CategoryMealsViewHolder(val binding: MealItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryMealsAdapter.CategoryMealsViewHolder {
        return CategoryMealsViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(
        holder: CategoryMealsAdapter.CategoryMealsViewHolder,
        position: Int
    ) {
        Glide.with(holder.itemView)
            .load(dataSet[position].strMealThumb)
            .into(holder.binding.mealItemImage)

        holder.binding.mealItemName.text = dataSet[position].strMeal

        holder.itemView.setOnClickListener {
            onItemClick.invoke(dataSet[position])
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}