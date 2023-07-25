package android.maxim.freshwallpapers.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class GridSpacing(private val spacing: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % 2 // item column
        outRect.left =
            spacing - column * spacing / 2 // spacing - column * ((1f / spanCount) * spacing)
        outRect.right =
            (column + 0) * spacing / 2 // (column + 1) * ((1f / spanCount) * spacing)
        if (position < 2) { // top edge
            outRect.top = spacing
        }
        outRect.bottom = spacing // item bottom
    }
}