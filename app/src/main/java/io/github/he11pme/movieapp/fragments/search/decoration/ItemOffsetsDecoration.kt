package io.github.he11pme.movieapp.fragments.search.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.github.he11pme.movieapp.utils.extensions.dp

class ItemOffsetsDecoration() : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val defaultOffset = 5.dp
        outRect.set(
            defaultOffset,
            defaultOffset,
            defaultOffset,
            defaultOffset
        )
    }

}