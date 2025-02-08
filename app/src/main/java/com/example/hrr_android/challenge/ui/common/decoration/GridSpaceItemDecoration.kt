package com.example.hrr_android.challenge.ui.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpaceItemDecoration(
    private val spanCount: Int, // Grid의 column 수
    private val spacing: Int // 아이템 간 간격
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        // 가로 간격 설정 -> 마지막 열을 제외한 모든 아이템의 오른쪽에 간격 추가
        if (column != spanCount - 1) {
            outRect.right = spacing
        }

        // 세로 간격 설정 -> 마지막 행을 제외한 모든 아이템의 아래쪽에 간격 추가
        if (position < state.itemCount - spanCount) {
            outRect.bottom = spacing
        }
    }
}