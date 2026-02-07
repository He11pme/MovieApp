package io.github.he11pme.movieapp.view.rv.adapters

import android.view.View
import io.github.he11pme.movieapp.view.rv.diffcallbacks.IdentifiableDiffCallback
import io.github.he11pme.movieapp.fragments.home.carousel.MovieCardDelegateAdapter
import io.github.he11pme.movieapp.fragments.home.carousel.ShowAllButtonDelegateAdapter
import io.github.he11pme.movieapp.view.model.Identifiable
import io.github.he11pme.movieapp.view.rv.diffcallbacks.ListDelegationAdapterDiff

class CarouselAdapter(
    selectionId: String,
    toMovieDetails: (sharedView: View, movieId: Int) -> Unit,
    toSelections: (selectionId: String) -> Unit
) : ListDelegationAdapterDiff<Identifiable>(IdentifiableDiffCallback()) {

    init {
        delegatesManager.addDelegate(MovieCardDelegateAdapter(selectionId, toMovieDetails))
        delegatesManager.addDelegate(ShowAllButtonDelegateAdapter(toSelections))
    }

}