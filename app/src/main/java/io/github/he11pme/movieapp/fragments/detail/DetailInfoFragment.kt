package io.github.he11pme.movieapp.fragments.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import io.github.he11pme.movieapp.R
import io.github.he11pme.movieapp.databinding.FragmentDetailInfoBinding
import io.github.he11pme.movieapp.managers.AppBarManager
import io.github.he11pme.movieapp.model.MovieDetails
import io.github.he11pme.movieapp.model.PosterSizes
import io.github.he11pme.movieapp.utils.EmptyRequestListener
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow

@AndroidEntryPoint
class DetailInfoFragment : Fragment() {

    private lateinit var binding: FragmentDetailInfoBinding
    private var movieId = -1
    private var transitionName = ""
    private var source = Source.CONTENT
    private val foregroundItems = mutableListOf<View>()
    private val viewModel: DetailInfoViewModel by viewModels()
    val args: DetailInfoFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getArgs()
        handleBack()

        viewModel.loadDetails(movieId)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.detailInfoMain
            duration = 250
            scrimColor = Color.TRANSPARENT
        }

        sharedElementReturnTransition = MaterialContainerTransform().apply {
            drawingViewId =
                if (source == Source.CONTENT) R.id.contentContainer else R.id.searchFragmentContainer
            duration = 250
            scrimColor = Color.TRANSPARENT
        }

    }

    private fun getArgs() {

        movieId = args.movieId
        if (movieId == -1) throw RuntimeException("Movie id is required")

        transitionName = args.transitionName
        if (transitionName.isEmpty()) throw RuntimeException("Transition name is required")

        source = args.source

    }

    private fun handleBack() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateBack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailInfoBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel

        if (source != Source.SEARCH) bindToAppBarManager()
        setTransitionNames()
        fillListOfForegroundItems()
        setupViews()
        postponeEnterTransition()
        bindToViewModel()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Lock is necessary to prevent the app bar from changing when the fragment is closed
        if (source != Source.SEARCH) viewModel.appBarManager.lockAppBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (source != Source.SEARCH) viewModel.appBarManager.unlockAppBar()
    }

    private fun bindToAppBarManager() {
        bindAppBarState()
        bindMenuActions()
    }

    private fun bindMenuActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appBarManager.menuActions.collect(::handleMenuActions)
            }
        }
    }

    private fun handleMenuActions(action: AppBarManager.MenuAction) {
        when (action) {
            AppBarManager.MenuAction.DownloadBtnClicked -> viewModel.onDownloadBtnClicked()
            AppBarManager.MenuAction.FavoriteBtnClicked -> viewModel.onFavoriteBtnClicked()
            AppBarManager.MenuAction.ShareBtnClicked -> viewModel.onShareBtnClicked()
            else -> {}
        }
    }

    private fun bindAppBarState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appBarManager.appBarState.collect(::handleAppBarState)
            }
        }
    }

    private fun handleAppBarState(appBarState: AppBarManager.AppBarState) {
        appBarState.heightToolbar?.let { binding.toolbarDetail.layoutParams.height = it }
    }

    private fun setTransitionNames() {
        binding.posterDetail.transitionName = transitionName
    }

    private fun bindToViewModel() {
        bindState()
        bindAction()
    }

    private fun bindState() {
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: DetailInfoViewModel.State) {

        if (state is DetailInfoViewModel.State.Loaded) {
            val movieDetails = state.movieDetails

            setDataAboutMovie(movieDetails)
            loadPoster(
                movieDetails.posterUrl(PosterSizes.MEDIUM),
                movieDetails.posterUrl(PosterSizes.ORIGINAL)
            )
        }

    }

    private fun bindAction() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect(::handleAction)
            }
        }
    }

    private fun handleAction(action: DetailInfoViewModel.Action) {
        when (action) {
            is DetailInfoViewModel.Action.ShareMovie -> shareMovie(action.movie)
            is DetailInfoViewModel.Action.DownloadMovie -> downloadMovie()
            DetailInfoViewModel.Action.AddFavorite -> addFavorite()
            DetailInfoViewModel.Action.RemoveFavorite -> removeFavorite()
        }
    }

    private fun addFavorite() {
        toggleFavorite(
            getString(R.string.movie_added_to_favorites),
            true
        )
    }

    private fun removeFavorite() {
        toggleFavorite(
            getString(R.string.movie_removed_from_favorites),
            false
        )
    }

    private fun toggleFavorite(text: String, isFavorite: Boolean) {
        Snackbar.make(
            binding.detailInfoMain,
            text,
            Snackbar.LENGTH_SHORT
        ).show()
        setIconFavoriteBtn(isFavorite)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setIconFavoriteBtn(isFavorite: Boolean) {
        binding.favoriteBtn.icon =
            requireContext().getDrawable(if (isFavorite) ID_DRAWABLE_FAVORITE else ID_DRAWABLE_UNFAVORITE)
    }

    private fun shareMovie(movie: MovieDetails) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.text_for_share, movie.title, movie.vote, movie.overview)
            )
            type = "text/plain"
        }

        startActivity(Intent.createChooser(intent, getString(R.string.share)))
    }

    private fun downloadMovie() {
        Snackbar.make(
            binding.detailInfoMain,
            getString(R.string.functionality_will_be_added_later),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun navigateBack() = findNavController().navigateUp()

    private fun fillListOfForegroundItems() {
        foregroundItems.addAll(
            listOf(
                binding.titleText,
                binding.taglineText,
                binding.parametersText,
                binding.voteText,
                binding.downloadBtn,
                binding.favoriteBtn,
                binding.shareBtn,
                binding.bottomBackground
            )
        )
        if (source != Source.SEARCH) foregroundItems.add(binding.backBtn)
    }

    private fun setupViews() {
        binding.backBtn.visibility =
            if (source == Source.SEARCH) View.INVISIBLE else View.VISIBLE

        binding.headerBackground.visibility =
            if (source == Source.SEARCH) View.GONE else View.VISIBLE

        binding.toolbarDetail.visibility =
            if (source == Source.SEARCH) View.GONE else View.INVISIBLE

        foregroundItems.forEach { prepareViewForAnimation(it) }

        if (source != Source.SEARCH) binding.backBtn.setOnClickListener { navigateBack() }

        handleAppBarScroll()
    }

    private fun setDataAboutMovie(movie: MovieDetails) {

        fun setupTagline() {
            if (movie.tagline.isEmpty()) binding.taglineText.visibility = View.GONE
            else binding.taglineText.text = movie.tagline
        }

        fun compoundParameters(): String {
            val separator = " • "
            val year = movie.releaseDate.take(4)
            val genres = movie.genres.take(3).joinToString(separator) { genre -> genre.name }
            val country = movie.countries.first().name
            val runtime = "${movie.runtime} min"

            return "$year$separator$genres\n$country$separator$runtime"
        }

        binding.titleText.text = movie.title
        setupTagline()
        binding.parametersText.text = compoundParameters()
        binding.overviewText.text = movie.overview
        binding.voteText.text = movie.vote.toString()
        setIconFavoriteBtn(movie.isFavorite)

    }

    private fun loadPoster(sharedPosterUrl: String, originalPosterUrl: String) {
        Glide.with(binding.posterDetail)
            .load(originalPosterUrl)
            .thumbnail(preloadPosterBuilder(sharedPosterUrl))
            .listener(EmptyRequestListener<Drawable>().apply {
                doSimpleResourceReady = {
                    foregroundItems.forEach { enterAnimationForView(it) }
                }
            })
            .into(binding.posterDetail)
    }

    private fun preloadPosterBuilder(sharedPosterUrl: String): RequestBuilder<Drawable?> {
        return Glide.with(binding.posterDetail)
            .load(sharedPosterUrl)
            .also { startPostponedEnterTransition() }
    }

    private fun prepareViewForAnimation(v: View) {
        v.visibility = View.GONE
        v.alpha = 0f
    }

    private fun enterAnimationForView(v: View) {
        v.translationY = 50f
        v.visibility = View.VISIBLE
        v.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(300L)
            .start()
    }

    private fun handleAppBarScroll() {
        binding.appBarDetail.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScroll = appBarLayout.totalScrollRange
            val collapseRatio = abs(verticalOffset) / totalScroll.toFloat()

            if (source != Source.SEARCH) viewModel.onAppBarScrolled(collapseRatio)
            applyScrollToForeground(collapseRatio)
        }
    }

    private fun applyScrollToForeground(collapseRatio: Float) {
        foregroundItems.forEach {
            if (it.id == R.id.bottom_background || it.id == R.id.header_background) return@forEach
            applyScrollTransformation(it, collapseRatio)
        }
    }

    private fun applyScrollTransformation(view: View, collapseRatio: Float) {
        view.alpha = 1f - collapseRatio.pow(2)
        view.scaleX = 1f - collapseRatio.pow(2)
        view.scaleY = 1f - collapseRatio.pow(2)
    }

    companion object {
        val ID_DRAWABLE_FAVORITE = R.drawable.ic_favorite
        val ID_DRAWABLE_UNFAVORITE = R.drawable.ic_favorite_outline

        enum class Source {
            SEARCH,
            CONTENT
        }
    }

}
