package com.example.hrr_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemBlockListBinding

interface OnBlockClickListener{
    fun onBlockClicked(tempUser: TempUser)
    fun onBlockedClicked(tempUser: TempUser)
}

class BlockRVAdapter (
    private val tempList : ArrayList<TempUser>
)
    : RecyclerView.Adapter<BlockRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): BlockRVAdapter.ViewHolder {
        val binding: ItemBlockListBinding
                = ItemBlockListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tempList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = tempList.size

    inner class ViewHolder(val binding: ItemBlockListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tempUser: TempUser){
            binding.ivBlockedImage.setImageResource(tempUser.img)
            binding.tvBlockedName.text = tempUser.name
            binding.tvBlockedLevel.text = tempUser.level

            if(tempUser.isBlocked){
                // 차단된 사용자
                binding.ivBlockedBtn.visibility = View.VISIBLE
                binding.ivBlockBtn.visibility = View.GONE
            }
            else{
                binding.ivBlockedBtn.visibility = View.GONE
                binding.ivBlockBtn.visibility = View.VISIBLE
            }

            //클릭 이벤트 처리
            binding.ivBlockBtn.setOnClickListener {
                // 차단 버튼 클릭 시
                binding.ivBlockedBtn.visibility = View.VISIBLE
                binding.ivBlockBtn.visibility = View.GONE
                // Todo: 차단 처리
            }
            binding.ivBlockedBtn.setOnClickListener {
                // 차단됨 버튼 클릭 시
                binding.ivBlockedBtn.visibility = View.GONE
                binding.ivBlockBtn.visibility = View.VISIBLE
                //Todo: 차단 해제 처리
            }

        }
    }
}