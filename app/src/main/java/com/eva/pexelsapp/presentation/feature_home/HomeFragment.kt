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
import androidx.recyclerview.widget.LinearLayoutManager
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

		val curatedPhotoAdapter = CuratedPhotoAdapter(context)
		val snapHelper = CarouselSnapHelper()
		val carouselStrategy = MultiBrowseCarouselStrategy()

		binding.curatedPhotos.apply {
			snapHelper.attachToRecyclerView(this)
			adapter = curatedPhotoAdapter
			layoutManager = CarouselLayoutManager(carouselStrategy)
			lifecycleScope.launch {
				viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.curatedPhotos.collect(curatedPhotoAdapter::submitData)
				}
			}
		}

		val collectionPhotoAdapter = CollectionPhotoAdapter(context)

		binding.collections.apply {
			adapter = collectionPhotoAdapter
			layoutManager = LinearLayoutManager(context)
			lifecycleScope.launch {
				viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.photoCollections.collect(collectionPhotoAdapter::submitData)
				}
			}
		}

		return binding.root
	}
}