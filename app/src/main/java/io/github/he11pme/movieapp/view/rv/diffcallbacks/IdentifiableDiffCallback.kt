package io.github.he11pme.movieapp.view.rv.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import io.github.he11pme.movieapp.view.model.Identifiable

class IdentifiableDiffCallback : DiffUtil.ItemCallback<Identifiable>() {

    override fun areItemsTheSame(
        oldItem: Identifiable,
        newItem: Identifiable
    ): Boolean {
        return oldItem.getIdentifier() == newItem.getIdentifier()
    }

    override fun areContentsTheSame(
        oldItem: Identifiable,
        newItem: Identifiable
    ): Boolean {
        return oldItem::class == newItem::class && oldItem == newItem
    }

}