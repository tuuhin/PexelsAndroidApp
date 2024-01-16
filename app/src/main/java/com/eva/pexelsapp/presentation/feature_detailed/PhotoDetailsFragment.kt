package com.eva.pexelsapp.presentation.feature_detailed

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.dispose
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.pexelsapp.R
import com.eva.pexelsapp.databinding.PhotoDetailedFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class PhotoDetailsFragment : Fragment() {


	private var _binding: PhotoDetailedFragmentBinding? = null

	private val viewModel by viewModels<PhotoDetailsViewModel>()

	private val navArgs: PhotoDetailsFragmentArgs by navArgs<PhotoDetailsFragmentArgs>()

	private val binding: PhotoDetailedFragmentBinding
		get() = _binding!!


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val backDispatcher = object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				requireActivity().onNavigateUp()
			}
		}

		requireActivity().onBackPressedDispatcher
			.addCallback(viewLifecycleOwner, backDispatcher)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = PhotoDetailedFragmentBinding.inflate(inflater)

		// SHARED ELEMENT TRANSITION CONFIGURATION

		sharedElementEnterTransition = TransitionInflater.from(context)
			.inflateTransition(R.transition.shared_element_transition)

		sharedElementReturnTransition = TransitionInflater.from(context)
			.inflateTransition(R.transition.shared_element_transition)

		//setContent image
		setContentImage()
		//setShare button
		setShareButton()
		//set download button
		setDownloadButton()

		return binding.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		// Cancels the request if not completed
		binding.image.dispose()
		_binding = null
	}

	private fun setContentImage() {
		val context = requireContext()

		val photoResource = navArgs.photoRes

		binding.topAppBar.setNavigationOnClickListener {
			requireActivity().onNavigateUp()
		}

		binding.mediaImageAltText.apply {
			if (photoResource.title.isNotEmpty()) text = photoResource.title
			else visibility = View.GONE
		}

		val color = Color.parseColor(photoResource.placeHolderColor)
		binding.image.setBackgroundColor(color)

		val request = ImageRequest.Builder(context)
			.data(photoResource.images.portrait)
			.target(binding.image)
			.build()

		context.imageLoader.enqueue(request)
	}

	private fun setShareButton() {
		val context = requireContext()
		val photoRes = navArgs.photoRes

		val intent = Intent(Intent.ACTION_SEND).apply {
			type = "text/plain"
			if (photoRes.title.isNotEmpty()) {
				putExtra(Intent.EXTRA_TITLE, photoRes.title)
			}
			putExtra(Intent.EXTRA_TEXT, photoRes.photoUrl)
		}
		val shareIntent = Intent.createChooser(intent, null)

		binding.photoActions.shareButton.setOnClickListener {
			context.startActivity(shareIntent)
		}
	}

	private fun setDownloadButton() {

		val optionsBottomSheet = DownloadOptionsBottomSheet()

		val image = navArgs.photoRes.images

		(optionsBottomSheet.dialog as? BottomSheetDialog)?.apply {

			behavior.isHideable = true
			behavior.isFitToContents = true
			behavior.saveFlags = BottomSheetBehavior.SAVE_NONE
		}

		optionsBottomSheet.onOptionSelect { option ->
			viewModel.onDownloadPhoto(image, option)
			optionsBottomSheet.dismiss()
		}

		binding.photoActions.downloadPictureButton.setOnClickListener {
			optionsBottomSheet.show(parentFragmentManager, DownloadOptionsBottomSheet.TAG)
		}
	}

	private fun setWallpaperButton() {
		binding.photoActions.setWallpaperButton.setOnClickListener {

		}
	}

}