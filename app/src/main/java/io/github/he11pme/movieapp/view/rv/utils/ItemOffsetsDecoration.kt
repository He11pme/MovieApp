package io.github.he11pme.movieapp.view.rv.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetsDecoration(
    private val leftOffset: Int = 0,
    private val topOffset: Int = 0,
    private val rightOffset: Int = 0,
    private val bottomOffset: Int = 0,
) : RecyclerView.ItemDecoration() {

    constructor(defaultOffset: Int) : this(
        defaultOffset,
        defaultOffset,
        defaultOffset,
        defaultOffset
    )

    constructor(defaultOffsetVertical: Int, defaultOffsetHorizontal: Int) : this(
        defaultOffsetHorizontal,
        defaultOffsetVertical,
        defaultOffsetHorizontal,
        defaultOffsetVertical
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(
            leftOffset,
            topOffset,
            rightOffset,
            bottomOffset
        )
    }

}