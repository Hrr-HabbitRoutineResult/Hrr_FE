package com.example.hrr_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.ItemProfileCertificationRecoredBinding

class ProfileCertificationRVAdapter (private val certificationList : ArrayList<Certification>) : RecyclerView.Adapter<ProfileCertificationRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ProfileCertificationRVAdapter.ViewHolder {
        val binding: ItemProfileCertificationRecoredBinding
                = ItemProfileCertificationRecoredBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = certificationList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = certificationList.size

    inner class ViewHolder(val binding: ItemProfileCertificationRecoredBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(certification: Certification){
            binding.tvProfileCertificationRecordContentName.text = certification.challengeName
            binding.tvProfileCertificationRecordContentTitle.text = certification.title
            binding.tvProfileCertificationRecordContentDate.text = certification.date

            if(certification.coverimg == null){
                //커버 이미지 없을 때
                binding.ivProfileCertificationRecordContentImg.visibility = View.INVISIBLE
            }
            else{
                //커버 이미지 있을 때
                binding.ivProfileCertificationRecordContentImg.setImageResource(certification.coverimg!!)
            }

            if(!certification.hasLink){
                //링크 없을 때
                binding.ivProfileCertificationRecordLink.visibility = View.INVISIBLE
            }

            if(certification.title==""){
                //게시글 제목 없을 때
                binding.llProfileCertificationTitleAndLink.visibility = View.GONE
            }
        }
    }
}