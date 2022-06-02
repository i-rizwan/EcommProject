package com.infigo.watchsaleapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infigo.watchsaleapp.model.CategoryItem
import com.infigo.watchsaleapp.databinding.CategoryItemBinding

class CategoryAdapter(private val listener: ICategoryListener) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var categoryList: ArrayList<CategoryItem> = ArrayList<CategoryItem>()
    var row_index = -1

    class CategoryViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)) }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]

        holder.binding.title.text = category.name

        holder.binding.apply {
            title.setOnClickListener {
                row_index = position
                notifyDataSetChanged()
                listener.onCategoryClick(category)
            }
        }

        holder.binding.apply {
            if (row_index == position)
                title.setTextColor(Color.parseColor("#c85a54"))
            else
                title.setTextColor(Color.parseColor("#000000"))
        }


    }

    override fun getItemCount() = categoryList.size

    private val diffCallback = object : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem
        }

    }

    fun updateList(newList: ArrayList<CategoryItem>) {
        categoryList.clear()
        categoryList = newList
        notifyDataSetChanged()
    }

}

interface ICategoryListener {
    fun onCategoryClick(category: CategoryItem)
}