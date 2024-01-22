package com.eva.pexelsapp.presentation.feature_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.eva.pexelsapp.R
import com.eva.pexelsapp.data.mappers.toParcelable
import com.eva.pexelsapp.databinding.HomeFragmentBinding
import com.eva.pexelsapp.presentation.feature_home.adapters.CollectionPhotoAdapter
import com.eva.pexelsapp.presentation.feature_home.adapters.CommonFooterAdapter
import com.eva.pexelsapp.presentation.feature_home.adapters.CuratedPhotoAdapter
import com.eva.pexelsapp.presentation.feature_home.adapters.SearchResultsAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.MultiBrowseCarouselStrategy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

	private val viewModel by viewModels<HomeViewModel>()

	private var _binding: HomeFragmentBinding? = null

	private val binding: HomeFragmentBinding
		get() = _binding!!


	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = HomeFragmentBinding.inflate(inflater)

		// setting up collections
		setUpCollections()
//		// setting up curated photos
		setUpCuratedPhotos()
//		//setUp search
		setUpSearchBinding()
//		//setup search filters
		setUpSearchFilters()

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		postponeEnterTransition()
		view.doOnPreDraw { startPostponedEnterTransition() }

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun setUpSearchFilters() {
		val bottomSheet = SearchFilterBottomSheet(filtersFlow = viewModel.searchFilters).apply {
			(dialog as? BottomSheetDialog)
				?.apply {
					behavior.isHideable = true
					behavior.isFitToContents = true
					behavior.saveFlags = BottomSheetBehavior.SAVE_NONE
				}
		}

		bottomSheet.onOrientationChangeListener(viewModel::setSearchFilterOrientation)

		bottomSheet.onSizeChangeListener(viewModel::setSearchFilterSize)

		binding.searchBar.setOnMenuItemClickListener {
			bottomSheet.show(parentFragmentManager, SearchFilterBottomSheet.TAG)
			true
		}
	}


	private fun setUpCollections() {

		val context = requireContext()

		val collectionAdapter = CollectionPhotoAdapter(context = context)

		collectionAdapter.onCollectionSelect { _, collection ->

			val navController = findNavController()
			val direction = HomeFragmentDirections
				.actionHomeFragmentToCollectionFragment(collection = collection.toParcelable())

			navController.navigate(directions = direction)
		}

		val layoutManager = LinearLayoutManager(context)

		val collectionAdapterWithFooter = with(collectionAdapter) {
			val footer = CommonFooterAdapter(context)
			footer.onRetryCallback(this::retry)
			withLoadStateFooter(footer)
		}

		binding.collections.apply {
			this@apply.adapter = collectionAdapterWithFooter
			this@apply.layoutManager = layoutManager
		}

		lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.photoCollections.collect(collectionAdapter::submitData)
			}
		}
	}

	private fun setUpSearchBinding() {
		val context = requireContext()

		val searchResultsAdapter = SearchResultsAdapter(context = context)

		searchResultsAdapter.onSearchSelect { viewHolder, photo ->
			val navController = findNavController()

			val extras = FragmentNavigator.Extras.Builder()
				.addSharedElement(
					sharedElement = viewHolder.imageView,
					name = context.getString(R.string.transition_photo_large)
				)
				.build()

			val destination = HomeFragmentDirections
				.actionHomeFragmentToPhotoDetailsFragment(photo = photo.toParcelable())

			navController.navigate(directions = destination, navigatorExtras = extras)
		}

		val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)

		binding.searchView.editText.setOnEditorActionListener { view, action, _ ->
			val searchQuery = view.text.toString()
			if (action == EditorInfo.IME_ACTION_SEARCH && searchQuery.isNotEmpty()) {
				viewModel.onSearch(searchQuery)
				true
			} else false
		}

		binding.searchResults.apply {
			this.adapter = searchResultsAdapter
			this.layoutManager = layoutManager
		}

		lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.searchResults.collect(searchResultsAdapter::submitData)
			}
		}

		lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				searchResultsAdapter.loadStateFlow.collect { state ->

					(state.refresh as? LoadState.Error)?.let { errorState ->
						val errorMessage = errorState.error.localizedMessage
							?: context.getString(R.string.unknown_error_message)

						val dialog = MaterialAlertDialogBuilder(context)
							.setTitle(R.string.search_error_title)
							.setMessage(errorMessage)
							.setNegativeButton(R.string.close_dialog_text) { dialog, _ ->
								dialog.dismiss()
							}
							.create()

						dialog.show()
					}

					binding.searchProgressIndicator.root.isVisible =
						state.refresh is LoadState.Loading

					binding.searchResults.isVisible =
						state.refresh is LoadState.NotLoading

				}
			}
		}

	}

	private fun setUpCuratedPhotos() {

		val context = requireContext()

		val curatedPhotoAdapter = CuratedPhotoAdapter(context = context)

		curatedPhotoAdapter.onPhotoSelect { viewHolder, photo ->

			val navController = findNavController()
			val extras = FragmentNavigator.Extras.Builder()
				.addSharedElement(
					sharedElement = viewHolder.image,
					name = context.getString(R.string.transition_photo_large)
				)
				.build()

			val destination = HomeFragmentDirections
				.actionHomeFragmentToPhotoDetailsFragment(photo = photo.toParcelable())

			navController.navigate(directions = destination, navigatorExtras = extras)
		}

		val snapHelper = CarouselSnapHelper()
		val carouselStrategy = MultiBrowseCarouselStrategy()
		val layoutManager = CarouselLayoutManager(carouselStrategy)

		binding.curatedPhotos.apply {
			snapHelper.attachToRecyclerView(this)
			this.adapter = curatedPhotoAdapter
			this.layoutManager = layoutManager
		}

		lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.curatedPhotos.collect(curatedPhotoAdapter::submitData)
			}
		}

	}
}
