package com.eva.pexelsapp.presentation.feature_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eva.pexelsapp.R
import com.eva.pexelsapp.data.mappers.toParcelable
import com.eva.pexelsapp.databinding.HomeFragmentBinding
import com.eva.pexelsapp.presentation.feature_home.adapters.CollectionPhotoAdapter
import com.eva.pexelsapp.presentation.feature_home.adapters.CuratedPhotoAdapter
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.MultiBrowseCarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

	private val viewModel by activityViewModels<HomeViewModel>()

	private val listStateKey = "LIST_STATE"
	private val states = Bundle()

	private var _binding: HomeFragmentBinding? = null

	private val binding: HomeFragmentBinding
		get() = _binding!!


	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = HomeFragmentBinding.inflate(inflater)

		val context = requireContext()
		val navController = findNavController()


		val curatedPhotoAdapter = CuratedPhotoAdapter(
			context = context,
			onImageSelect = { viewHolder, photo ->

				val extras = FragmentNavigator.Extras.Builder()
					.addSharedElement(
						viewHolder.image,
						context.getString(R.string.transition_photo_large)
					)
					.build()

				val destination = HomeFragmentDirections
					.actionHomeFragmentToPhotoDetailsFragment(photoRes = photo.toParcelable())


				navController.navigate(destination, extras)
			},
		)
		val snapHelper = CarouselSnapHelper()
		val carouselStrategy = MultiBrowseCarouselStrategy()
		val layoutManager = CarouselLayoutManager(carouselStrategy)

		binding.curatedPhotos.apply {
			snapHelper.attachToRecyclerView(this)
			adapter = curatedPhotoAdapter
			this.layoutManager = layoutManager
			lifecycleScope.launch {
				viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.curatedPhotos.collect(curatedPhotoAdapter::submitData)
				}
			}
		}

		val collectionPhotoAdapter = CollectionPhotoAdapter(context)

		binding.collections.apply {
			adapter = collectionPhotoAdapter
			this.layoutManager = LinearLayoutManager(context)
			lifecycleScope.launch {
				viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.photoCollections.collect(collectionPhotoAdapter::submitData)
				}
			}
		}

		return binding.root
	}
}