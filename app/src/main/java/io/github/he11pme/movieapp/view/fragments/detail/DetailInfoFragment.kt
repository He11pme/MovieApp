package io.github.he11pme.movieapp.view.fragments.detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.transition.doOnEnd
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import io.github.he11pme.movieapp.R
import io.github.he11pme.movieapp.databinding.FragmentDetailInfoBinding
import io.github.he11pme.movieapp.domain.models.MovieDetails
import io.github.he11pme.movieapp.managers.AppBarManager
import io.github.he11pme.movieapp.utils.EmptyRequestListener
import io.github.he11pme.movieapp.view.fragments.DefaultFragment
import io.github.he11pme.movieapp.view.mappers.toUi
import io.github.he11pme.movieapp.view.rv.utils.enums.PosterSizes
import io.github.he11pme.movieapp.view.rv.utils.enums.Source
import kotlin.math.abs
import kotlin.math.pow

@AndroidEntryPoint
class DetailInfoFragment : DefaultFragment() {

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
            doOnEnd {
                foregroundItems.forEach { enterAnimationForView(it) }
            }
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
        binding.source = source

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

    private fun bindAppBarState() {
        viewModel.appBarManager.appBarState.collectWithLifecycle(::handleAppBarState)
    }

    private fun handleAppBarState(appBarState: AppBarManager.AppBarState) {
        appBarState.heightToolbar?.let { binding.toolbarDetail.layoutParams.height = it }
    }

    private fun bindMenuActions() {
        viewModel.appBarManager.menuActions.collectWithLifecycle(::handleMenuActions)
    }

    private fun handleMenuActions(action: AppBarManager.MenuAction) {
        when (action) {
            AppBarManager.MenuAction.DownloadBtnClicked -> viewModel.onDownloadBtnClicked()
            AppBarManager.MenuAction.FavoriteBtnClicked -> viewModel.onFavoriteBtnClicked()
            AppBarManager.MenuAction.ShareBtnClicked -> viewModel.onShareBtnClicked()
            else -> {}
        }
    }

    private fun setTransitionNames() {
        binding.posterDetail.transitionName = transitionName
    }

    private fun bindToViewModel() {
        bindState()
        bindDownloadMovieState()
        bindAction()
    }

    private fun bindState() {
        viewModel.state.collectWithLifecycle(::handleState)
    }

    private fun handleState(state: DetailInfoViewModel.State) {
        binding.state = state

        binding.progressBar.visibility =
            if (state is DetailInfoViewModel.State.Loading) View.VISIBLE else View.GONE

        if (state is DetailInfoViewModel.State.Loaded) {
            val movieDetails = state.movieDetails.toUi(requireContext())
            binding.movie = movieDetails

            loadPoster(
                movieDetails.posterUrl(PosterSizes.MEDIUM),
                movieDetails.posterUrl(PosterSizes.ORIGINAL)
            )
        }


        binding.errorView.root.visibility =
            if (state is DetailInfoViewModel.State.Error) View.VISIBLE else View.GONE

        binding.errorView.textError.text = if (state is DetailInfoViewModel.State.Error) state.toUi() else ""

        if (state is DetailInfoViewModel.State.Error) startPostponedEnterTransition()


    }

    private fun DetailInfoViewModel.State.Error.toUi() = when (this.error) {
        DetailInfoViewModel.TypeError.InternetConnectionError -> getString(R.string.no_internet_connection_ui)

        DetailInfoViewModel.TypeError.NotFoundError -> getString(R.string.movie_not_found_ui)

        DetailInfoViewModel.TypeError.RequestLimitError -> getString(R.string.request_limit_ui)

        DetailInfoViewModel.TypeError.ServerConnectionError -> getString(R.string.server_connection_error_ui)

        DetailInfoViewModel.TypeError.UnexpectedError -> getString(R.string.unexpected_error_ui)
    }

    private fun bindDownloadMovieState() =
        viewModel.downloadMovieState.collectWithLifecycle(::handleDownloadMovieState)

    private fun handleDownloadMovieState(state: DetailInfoViewModel.DownloadMovieState) {
        binding.progressIndicatorDownload.apply {
            visibility =
                if (state == DetailInfoViewModel.DownloadMovieState.Loading) View.VISIBLE else View.GONE
        }

        binding.downloadBtn.apply {
            isEnabled = state == DetailInfoViewModel.DownloadMovieState.Idle
        }
    }

    private fun bindAction() = viewModel.actions.collectWithLifecycle(::handleAction)

    private fun handleAction(action: DetailInfoViewModel.Action) {
        when (action) {
            is DetailInfoViewModel.Action.ShareMovie -> shareMovie(action.movie)
            DetailInfoViewModel.Action.DownloadMovie -> downloadMovie()
            DetailInfoViewModel.Action.AddFavorite -> addFavorite()
            DetailInfoViewModel.Action.RemoveFavorite -> removeFavorite()
            DetailInfoViewModel.Action.Release -> startPostponedEnterTransition()
            is DetailInfoViewModel.Action.DownloadMovieError ->
                showSnackBarDownloadMovieError(action.e)

            DetailInfoViewModel.Action.DownloadMovieSuccess -> showSnackBarDownloadMovieSuccess()
        }
    }

    private fun showSnackBarDownloadMovieError(e: DetailInfoViewModel.DownloadMovieErrorType) {

        val text = when (e) {
            DetailInfoViewModel.DownloadMovieErrorType.FetchError -> getString(R.string.download_movie_error)
            DetailInfoViewModel.DownloadMovieErrorType.SaveError -> getString(R.string.save_movie_error)
        }

        Snackbar
            .make(binding.root, text, Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun showSnackBarDownloadMovieSuccess() {
        Snackbar.make(
            binding.root,
            getString(R.string.download_is_success),
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.open)) {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.type = "image/*"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }.show()
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewModel.saveMovieScopedStorage()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.saveMovieLegacyStorage()
            } else {
                Snackbar.make(
                    binding.detailInfoMain,
                    getString(R.string.permission_is_required_for_download_movie),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    private fun navigateBack() = findNavController().navigateUp()

    private fun fillListOfForegroundItems() {
        foregroundItems.addAll(
            listOf(
                binding.titleText,
                binding.taglineText,
                binding.parametersText,
                binding.downloadBtn,
                binding.favoriteBtn,
                binding.shareBtn,
                binding.bottomBackground,
                binding.starRating
            )
        )
        if (source != Source.SEARCH) foregroundItems.add(binding.backBtn)
    }

    private fun setupViews() {
        binding.backBtn.setOnClickListener { navigateBack() }
        binding.errorView.reloadBtn.setOnClickListener { viewModel.loadDetails(movieId) }
        handleAppBarScroll()
    }

    private fun loadPoster(sharedPosterUrl: String, originalPosterUrl: String) {
        Glide.with(binding.posterDetail)
            .load(originalPosterUrl)
            .thumbnail(preloadPosterBuilder(sharedPosterUrl))
            .listener(startTransitionOnImageReadyListener)
            .into(binding.posterDetail)
    }

    private fun preloadPosterBuilder(sharedPosterUrl: String): RequestBuilder<Drawable?> {
        return Glide.with(binding.posterDetail)
            .load(sharedPosterUrl)
            .listener(startTransitionOnImageReadyListener)
    }

    private val startTransitionOnImageReadyListener =
        EmptyRequestListener<Drawable>().apply {
            doSimpleResourceReady = { startPostponedEnterTransition() }
        }

    private fun enterAnimationForView(v: View) {
        v.translationY = 50f
        v.animate()
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

    }

}
