package com.example.hrr_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemCategoryBinding

class CategoryRVAdapter(private val categories: List<Category>) : RecyclerView.Adapter<CategoryRVAdapter.CategoryViewHolder>() {

    // ViewHolder에 뷰 바인딩 적용
    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        // 뷰 바인딩 초기화
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        // 바인딩으로 뷰 연결
        holder.binding.ivHomeCategoryIcon.setImageResource(category.iconRes)  // 아이콘 설정
        holder.binding.tvHomeCategoryTitle.text = category.title  // 제목 설정
    }

    override fun getItemCount(): Int = categories.size
}
