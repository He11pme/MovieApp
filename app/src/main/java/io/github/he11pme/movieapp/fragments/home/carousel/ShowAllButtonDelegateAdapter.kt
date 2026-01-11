package io.github.he11pme.movieapp.fragments.home.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import io.github.he11pme.movieapp.databinding.ShowAllItemBinding
import io.github.he11pme.movieapp.model.Identifiable
import io.github.he11pme.movieapp.model.ShowAllMoviesButton

class ShowAllButtonDelegateAdapter(private val toSelection: (selectionId: String) -> Unit) :
    AbsListItemAdapterDelegate<ShowAllMoviesButton, Identifiable, ShowAllButtonDelegateAdapter.ViewHolder>() {
    override fun isForViewType(
        item: Identifiable,
        items: List<Identifiable?>,
        position: Int
    ): Boolean {
        return item is ShowAllMoviesButton
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            ShowAllItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        item: ShowAllMoviesButton,
        holder: ViewHolder,
        payloads: List<Any?>
    ) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ShowAllItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(showAllMovies: ShowAllMoviesButton) {

            binding.root.setOnClickListener { toSelection(showAllMovies.selectionId) }

        }

    }
}