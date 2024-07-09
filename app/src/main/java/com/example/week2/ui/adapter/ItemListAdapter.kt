package com.example.week2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.R
import com.example.week2.data.item.Item

class ItemListAdapter(private val onItemClickListener: OnItemClickListener) : ListAdapter<Item, ItemListAdapter.ItemViewHolder>(ITEMS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.create(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name, current.price)
    }

    class ItemViewHolder(itemView: View, private val onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val itemImageView: ImageView = itemView.findViewById(R.id.imageView)
        private val itemItemView1: TextView = itemView.findViewById(R.id.textView1)
        private val itemItemView2: TextView = itemView.findViewById(R.id.textView2)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(name: String?, cost: Int?) {
            // Sample image setting, replace with actual image loading logic if needed
            itemImageView.setImageResource(R.drawable.naked)
            itemItemView1.text = name
            itemItemView2.text = cost.toString()
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(position)
            }
        }

        companion object {
            fun create(parent: ViewGroup, onItemClickListener: OnItemClickListener): ItemViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recyclerview_item, parent, false)
                return ItemViewHolder(view, onItemClickListener)
            }
        }
    }

    companion object {
        private val ITEMS_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
