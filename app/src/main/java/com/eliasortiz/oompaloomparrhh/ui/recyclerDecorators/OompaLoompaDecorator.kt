package com.eliasortiz.oompaloomparrhh.ui.recyclerDecorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.eliasortiz.oompaloomparrhh.utils.toDp

class OompaLoompaDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val margin = 16.toDp(view.context)
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = margin
            }
            bottom = margin
            left = margin
            right = margin
        }
    }
}