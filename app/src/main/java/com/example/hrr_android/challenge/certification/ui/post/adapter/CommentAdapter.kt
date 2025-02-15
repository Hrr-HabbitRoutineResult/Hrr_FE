package com.example.hrr_android.challenge.certification.ui.post.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.DialogCommentMoreBinding
import com.example.hrr_android.databinding.ItemCommentBinding

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private var popup: PopupWindow? = null

    inner class CommentViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvCommentName.text = when (position % 3) {
                    0 -> "작성자명1"
                    1 -> "작성자명2"
                    else -> "작성자명3"
                }

                tvCommentContent.text = when (position % 3) {
                    0 -> "Text1"
                    1 -> "Text2"
                    else -> "Text3"
                }

                tvCommentTime.text = when (position % 3) {
                    0 -> "방금 전"
                    1 -> "1시간 전"
                    else -> "2시간 전"
                }

                // 버튼 클릭 리스너 설정
                btnCommentAccept.setOnClickListener {
                    // TODO: 채택 버튼 클릭 처리
                }

                btnCommentReply.setOnClickListener {
                    // TODO: 답글 버튼 클릭 처리
                }

                btnCommentLike.setOnClickListener {
                    // TODO: 좋아요 버튼 클릭 처리
                }

                btnCommentMore.setOnClickListener { view ->
                    showPopupMenu(view, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(position)
    }

    // 더보기 다이얼로그 창을 팝업으로 표시하는 함수
    private fun showPopupMenu(anchorView: View, position: Int) {
        // 이전 팝업이 있다면 닫기
        popup?.dismiss()

        // 팝업 레이아웃 인플레이트
        val inflater = LayoutInflater.from(anchorView.context)
        val popupBinding = DialogCommentMoreBinding.inflate(inflater)

        // 팝업 윈도우 생성
        popup = PopupWindow(
            popupBinding.root,
            120.dpToPx(anchorView.context),
            70.dpToPx(anchorView.context)
        ).apply {
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        // 클릭 리스너 설정
        popupBinding.tvCommentSendMessage.setOnClickListener {
            // TODO: 쪽지 보내기 처리
            popup?.dismiss()
        }

        popupBinding.tvCommentBlock.setOnClickListener {
            // TODO: 차단 처리
            popup?.dismiss()
        }

        // 더보기 버튼 아래에 팝업 표시
        popup?.showAsDropDown(
            anchorView,
            -110.dpToPx(anchorView.context),
            11.dpToPx(anchorView.context))
    }

    // dp를 px로 변환하는 확장 함수
    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    override fun getItemCount() = 10  // 테스트용
}