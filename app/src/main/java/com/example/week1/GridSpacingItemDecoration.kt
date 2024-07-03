package com.example.week1

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // 아이템의 위치
        val column = position % spanCount // 아이템이 속한 열

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount // 왼쪽 간격 설정
            outRect.right = (column + 1) * spacing / spanCount // 오른쪽 간격 설정
            if (position < spanCount) {
                outRect.top = spacing // 첫 번째 행의 위쪽 간격 설정
            }
            outRect.bottom = spacing // 아래쪽 간격 설정
        } else {
            outRect.left = column * spacing / spanCount // 왼쪽 간격 설정
            outRect.right = spacing - (column + 1) * spacing / spanCount // 오른쪽 간격 설정
            if (position >= spanCount) {
                outRect.top = spacing // 첫 번째 행 이후의 위쪽 간격 설정
            }
        }
    }
}
