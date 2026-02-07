package io.github.he11pme.movieapp.view.rv.diffcallbacks

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

/**
 * A RecyclerView adapter that combines the capabilities of [com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter]
 * from the AdapterDelegates library with automatic list diffing using [androidx.recyclerview.widget.AsyncListDiffer].
 *
 * This class allows you to:
 * - Use delegates for different item types.
 * - Asynchronously update the list via [submitList] with minimal redraws.
 * - Access the current list of items through [currentList].
 */
open class ListDelegationAdapterDiff<T>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListDelegationAdapter<List<T>>() {

    private val differ = AsyncListDiffer(this, diffUtil)

    val currentList get() = differ.currentList

    // Overriding this functions is necessary for correct list display.
    // Otherwise, the items may appear to loop endlessly.
    override fun getItemCount() = currentList.size
    fun submitList(list: List<T>) {
        differ.submitList(list.toList()) {
            // Synchronization of ListDelegationAdapter and AsyncListDiffer
            items = currentList
        }
    }
}