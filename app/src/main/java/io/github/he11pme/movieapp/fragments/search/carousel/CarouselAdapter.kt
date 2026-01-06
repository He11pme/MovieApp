package io.github.he11pme.movieapp.fragments.search.carousel

import io.github.he11pme.movieapp.model.Identifiable
import io.github.he11pme.movieapp.utils.ListDelegationAdapterDiff

class CarouselAdapter(
    toMovieDetails: (movieId: Int) -> Unit,
    toSelections: (selectionId: String) -> Unit
) : ListDelegationAdapterDiff<Identifiable>(CarouselDiffCallback()) {

    init {
        delegatesManager.addDelegate(MovieCardDelegateAdapter(toMovieDetails))
        delegatesManager.addDelegate(ShowAllButtonDelegateAdapter(toSelections))
    }

}