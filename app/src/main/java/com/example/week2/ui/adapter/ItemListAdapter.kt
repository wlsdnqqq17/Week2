package com.example.week2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.week2.R
import com.example.week2.data.item.Item

class ItemListAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val showCheckmark: Boolean = false // 추가된 부분
) : ListAdapter<Item, ItemListAdapter.ItemViewHolder>(ITEMS_COMPARATOR) {

    var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.create(parent, onItemClickListener, this, showCheckmark) // 수정된 부분
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name, current.price, current.item_image_url, position == selectedPosition)
    }

    class ItemViewHolder(
        itemView: View,
        private val onItemClickListener: OnItemClickListener,
        private val adapter: ItemListAdapter,
        private val showCheckmark: Boolean // 추가된 부분
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val itemImageView: ImageView = itemView.findViewById(R.id.imageView)
        private val checkmarkImageView: ImageView = itemView.findViewById(R.id.checkmark)
        private val itemItemView1: TextView = itemView.findViewById(R.id.textView1)
        private val itemItemView2: TextView = itemView.findViewById(R.id.textView2)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(name: String?, cost: Int?, imageUrl: String?, isSelected: Boolean) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(itemImageView)
            itemItemView1.text = name
            itemItemView2.text = cost.toString()
            if (showCheckmark) { // 수정된 부분
                checkmarkImageView.visibility = if (isSelected) View.VISIBLE else View.GONE
            } else {
                checkmarkImageView.visibility = View.GONE
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (showCheckmark) { // 수정된 부분
                    adapter.notifyItemChanged(adapter.selectedPosition)
                    adapter.selectedPosition = position
                    adapter.notifyItemChanged(adapter.selectedPosition)
                }
                onItemClickListener.onItemClick(position)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener,
                adapter: ItemListAdapter,
                showCheckmark: Boolean // 추가된 부분
            ): ItemViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recyclerview_item, parent, false)
                return ItemViewHolder(view, onItemClickListener, adapter, showCheckmark)
            }
        }
    }

    companion object {
        private val ITEMS_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
