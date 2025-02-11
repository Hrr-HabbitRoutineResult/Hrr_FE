package com.example.hrr_android

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemProfileBadgeMoreBinding

interface OnBadgeMoreClickListener{
    fun onBadgeClick(badge: Badge)
}

class ProfileBadgeMoreRVAdapter(
    private val badgeList : ArrayList<Badge>,
    private val listener: OnBadgeMoreClickListener)
    : RecyclerView.Adapter<ProfileBadgeMoreRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ProfileBadgeMoreRVAdapter.ViewHolder {
        val binding: ItemProfileBadgeMoreBinding
                = ItemProfileBadgeMoreBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = badgeList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = badgeList.size

    inner class ViewHolder(val binding: ItemProfileBadgeMoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(badge: Badge){
            binding.includeItemProfileBadge.ivProfileBadgeIcon.setImageResource(badge.icon)
            binding.includeItemProfileBadge.tvProfileBadgeName.text = badge.name

            //미획득 뱃지일 경우 흑백 처리
            if(!badge.isObtained){
                applyBlackWhiteFilter(binding.includeItemProfileBadge.ivProfileBadgeIcon)
            }

            binding.root.setOnClickListener{
                listener.onBadgeClick(badge)
            }
        }
    }

    private fun applyBlackWhiteFilter(imageView: ImageView) {
        val colorMatrix = ColorMatrix()

        //흑백 변환
        colorMatrix.setSaturation(0f)

        // 밝기 조절 (1.0f = 원본, +a = 더 밝게)
        val brightness = 1.1f
        val brightnessMatrix = ColorMatrix(
            floatArrayOf(
                brightness, 0f, 0f, 0f, 20f,  // Red
                0f, brightness, 0f, 0f, 20f,  // Green
                0f, 0f, brightness, 0f, 20f,  // Blue
                0f, 0f, 0f, 1f, 0f  // Alpha
            )
        )

        // 대비 조절 (1.0f = 원본, -a = 연한 흑백)
        val contrast = 0.9f
        val contrastMatrix = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, 10f, // Red
                0f, contrast, 0f, 0f, 10f, // Green
                0f, 0f, contrast, 0f, 10f, // Blue
                0f, 0f, 0f, 1f, 0f  // Alpha
            )
        )

        // 효과 결합
        colorMatrix.postConcat(brightnessMatrix)
        colorMatrix.postConcat(contrastMatrix)

        // 최종 필터 적용
        val filter = ColorMatrixColorFilter(colorMatrix)
        imageView.colorFilter = filter
    }
}