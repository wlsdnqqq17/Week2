package com.example.week2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.MealListAdapter.MealViewHolder

class MealListAdapter : ListAdapter<Meal, MealViewHolder>(MEALS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.mealName, current.price)
    }

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mealItemView1: TextView = itemView.findViewById(R.id.textView1)
        private val mealItemView2: TextView = itemView.findViewById(R.id.textView2)

        fun bind(name: String?, cost: Int?) {
            mealItemView1.text = name
            mealItemView2.text = cost.toString()
        }

        companion object {
            fun create(parent: ViewGroup): MealViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.meal_recyclerview_item, parent, false)
                return MealViewHolder(view)
            }
        }
    }

    companion object {
        private val MEALS_COMPARATOR = object : DiffUtil.ItemCallback<Meal>() {
            override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return oldItem.mealName == newItem.mealName
            }
        }
    }
}
