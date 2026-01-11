package io.github.he11pme.movieapp.fragments.home

import androidx.recyclerview.widget.DiffUtil
import io.github.he11pme.movieapp.model.Selection

class ContentDiffCallback: DiffUtil.ItemCallback<Selection>() {
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
