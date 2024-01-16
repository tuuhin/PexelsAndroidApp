package com.eva.pexelsapp.presentation.feature_home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.eva.pexelsapp.R
import com.eva.pexelsapp.data.mappers.toParcelable
import com.eva.pexelsapp.databinding.HomeFragmentBinding
import com.eva.pexelsapp.presentation.feature_home.adapters.CollectionPhotoAdapter
import com.eva.pexelsapp.presentation.feature_home.adapters.CuratedPhotoAdapter
import com.eva.pexelsapp.presentation.feature_home.adapters.SearchResultsAdapter
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.MultiBrowseCarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

	private val viewModel by activityViewModels<HomeViewModel>()

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
		// setting up curated photos
		setUpCuratedPhotos()
		//setUp search
		setUpSearchBinding()

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


	private fun setUpCollections(context: Context = requireContext()) {

		val collectionAdapter = CollectionPhotoAdapter(context = context)

		collectionAdapter.onCollectionSelect { _, collection ->

			val navController = findNavController()
			val direction = HomeFragmentDirections
				.actionHomeFragmentToCollectionFragment(collection = collection.toParcelable())

			navController.navigate(directions = direction)
		}

		val layoutManager = LinearLayoutManager(context)

		binding.collections.apply {
			this@apply.adapter = collectionAdapter
			this@apply.layoutManager = layoutManager
			lifecycleScope.launch {
				viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.photoCollections.collect(collectionAdapter::submitData)
				}
			}
		}
	}

	private fun setUpSearchBinding(context: Context = requireContext()) {

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
				.actionHomeFragmentToPhotoDetailsFragment(photoRes = photo.toParcelable())

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
			adapter = searchResultsAdapter
			this.layoutManager = layoutManager
			lifecycleScope.launch {
				viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.searchResults.collect(searchResultsAdapter::submitData)
				}
			}
		}
	}

	private fun setUpCuratedPhotos(context: Context = requireContext()) {

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
				.actionHomeFragmentToPhotoDetailsFragment(photoRes = photo.toParcelable())

			navController.navigate(directions = destination, navigatorExtras = extras)
		}

		val snapHelper = CarouselSnapHelper()
		val carouselStrategy = MultiBrowseCarouselStrategy()
		val layoutManager = CarouselLayoutManager(carouselStrategy)

		binding.curatedPhotos.apply {

			snapHelper.attachToRecyclerView(this)
			adapter = curatedPhotoAdapter
			this.layoutManager = layoutManager
		}

		lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.curatedPhotos.collect(curatedPhotoAdapter::submitData)
			}
		}

	}
}
