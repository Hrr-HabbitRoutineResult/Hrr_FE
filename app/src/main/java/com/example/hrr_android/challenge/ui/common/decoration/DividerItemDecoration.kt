package com.example.hrr_android.challenge.ui.common.decoration

import android.content.res.Resources
import android.graphics.Canvas
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.R

class DividerItemDecoration(
    private val resources: Resources
) : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val divider = ResourcesCompat.getDrawable(resources, R.drawable.divider_line_1dp, null)

        // 마지막 아이템을 제외한 모든 아이템 사이에 구분선 추가
        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + (divider?.intrinsicHeight ?: 0)
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight

            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }
}