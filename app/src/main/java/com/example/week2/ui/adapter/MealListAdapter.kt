package com.example.week2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.Meal
import com.example.week2.R
import com.example.week2.ui.adapter.MealListAdapter.MealViewHolder

class MealListAdapter(private val onItemClickListener: OnItemClickListener) : ListAdapter<Meal, MealViewHolder>(MEALS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder.create(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.mealTime, current.mealName, current.price)
    }

    class MealViewHolder(itemView: View, private val onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val mealItemView0: TextView = itemView.findViewById(R.id.textView0)
        private val mealItemView1: TextView = itemView.findViewById(R.id.textView1)
        private val mealItemView2: TextView = itemView.findViewById(R.id.textView2)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(meal_time: String?, name: String?, cost: Int?) {
            mealItemView0.text = meal_time
            mealItemView1.text = name
            mealItemView2.text = cost.toString()
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(position)
            }
        }

        companion object {
            fun create(parent: ViewGroup, onItemClickListener: OnItemClickListener): MealViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.meal_recyclerview_item, parent, false)
                return MealViewHolder(view, onItemClickListener)
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

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
