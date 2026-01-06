package io.github.he11pme.movieapp.fragments.search.carousel

import androidx.recyclerview.widget.DiffUtil
import io.github.he11pme.movieapp.model.Identifiable

class CarouselDiffCallback : DiffUtil.ItemCallback<Identifiable>() {

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
