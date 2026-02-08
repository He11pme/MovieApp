package io.github.he11pme.movieapp.view.rv.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import io.github.he11pme.movieapp.domain.models.Selection

class SelectionDiffCallback: DiffUtil.ItemCallback<Selection>() {
    override fun areItemsTheSame(
        oldItem: Selection,
        newItem: Selection
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Selection,
        newItem: Selection
    ): Boolean {
        return oldItem == newItem
    }
}