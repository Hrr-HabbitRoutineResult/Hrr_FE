package com.example.hrr_android.challenge.ui.common.decoration

import android.content.res.Resources
import android.graphics.Canvas
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.R

class CenterDividerItemDecoration(
    private val resources: Resources
) : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val divider = ResourcesCompat.getDrawable(resources, R.drawable.divider_line_1dp, null)

        // dp를 px로 변환
        val dividerWidth = (324 * resources.displayMetrics.density).toInt()

        // 마지막 아이템을 포함한 모든 아이템 아래에 구분선 추가
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + (divider?.intrinsicHeight ?: 0)
            val left = (parent.width - dividerWidth) / 2
            val right = left + dividerWidth

            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }
}