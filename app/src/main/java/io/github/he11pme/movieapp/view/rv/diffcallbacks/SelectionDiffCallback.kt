package io.github.he11pme.movieapp.view.rv.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import io.github.he11pme.movieapp.view.model.SelectionUi

class SelectionDiffCallback: DiffUtil.ItemCallback<SelectionUi>() {
    override fun areItemsTheSame(
        oldItem: SelectionUi,
        newItem: SelectionUi
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SelectionUi,
        newItem: SelectionUi
    ): Boolean {
        return oldItem == newItem
    }
}