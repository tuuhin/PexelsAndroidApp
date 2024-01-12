package com.eva.pexelsapp.presentation.feature_detailed

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.pexelsapp.databinding.PhotoDetailedFragmentBinding

class PhotoDetailsFragment : Fragment() {


	private var _binding: PhotoDetailedFragmentBinding? = null


	private val navArgs: PhotoDetailsFragmentArgs by navArgs<PhotoDetailsFragmentArgs>()

	private val binding: PhotoDetailedFragmentBinding
		get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = PhotoDetailedFragmentBinding.inflate(inflater)

		// SHARED ELEMENT TRANSITION CONFIGURATION

		sharedElementEnterTransition = TransitionInflater.from(context)
			.inflateTransition(android.R.transition.move)

		sharedElementReturnTransition = TransitionInflater.from(context)
			.inflateTransition(android.R.transition.move)

		val context = requireContext()
		val activity = requireActivity()


		navArgs.photoRes?.let { photoResource ->

			binding.topAppBar.title = photoResource.title
			binding.topAppBar.setNavigationOnClickListener {
				activity.onNavigateUp()
			}

			val color = Color.parseColor(photoResource.placeHolderColor)
			binding.image.setBackgroundColor(color)

			val request = ImageRequest.Builder(context)
				.data(photoResource.images.portrait)
				.target(binding.image)
				.build()
			context.imageLoader.enqueue(request)

		}

		return binding.root
	}
}