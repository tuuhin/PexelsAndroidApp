package com.eva.pexelsapp.presentation.feature_detailed

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import coil.dispose
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.pexelsapp.R
import com.eva.pexelsapp.databinding.PhotoDetailedFragmentBinding
import com.eva.pexelsapp.databinding.PhotoDetailsDailogLayoutBinding
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.presentation.util.extensions.launchAndRepeatOnLifeCycle
import com.eva.pexelsapp.utils.UiEvent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered as CenteredDialogTheme

@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {

	private val navArgs by navArgs<PhotoDetailsFragmentArgs>()

	private val viewModel by viewModels<PhotoDetailsViewModel>()

	private var _binding: PhotoDetailedFragmentBinding? = null

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

		//back button
		binding.topAppBar.setNavigationOnClickListener {
			requireActivity().onNavigateUp()
		}

		//setContent image
		setContentImage()
		//setShare button
		setShareButton()
		//set download button
		setDownloadButton()
		//setup revised content
		setUpRevisedContent()

		return binding.root
	}


	override fun onDestroyView() {
		super.onDestroyView()
		// Cancels the request if not completed
		binding.image.dispose()
		_binding = null
	}


	private fun setUpMenuCallback(photo: PhotoResource) {
		val context = requireContext()
		val inflater = LayoutInflater.from(context)

		val photoInfoDialogLayoutBinding = PhotoDetailsDailogLayoutBinding.inflate(inflater)
			.apply {
				photoDimen.text =
					context.getString(R.string.photo_dimensions, photo.width, photo.height)

				photographerName.text = photo.photographer

				photographerName.setOnClickListener {
					val intent = Intent(Intent.ACTION_VIEW).apply {
						data = Uri.parse(photo.photographerUrl)
					}
					context.startActivity(intent)
				}

				colorHex.text = photo.placeHolderColor

				val color = Color.parseColor(photo.placeHolderColor)

				colorActual.setBackgroundColor(color)
			}


		val photoDetailsDialog = MaterialAlertDialogBuilder(context, CenteredDialogTheme)
			.setTitle(R.string.photo_details_text)
			.setView(photoInfoDialogLayoutBinding.root)
			.create()

		binding.topAppBar.setOnMenuItemClickListener {
			photoDetailsDialog.show()
			true
		}
	}

	private fun showErrorDialog(message: String) {
		val context = requireContext()

		val errorDialog = MaterialAlertDialogBuilder(context, CenteredDialogTheme)
			.setTitle(R.string.failed_to_load_photo)
			.setMessage(message)
			.setPositiveButton(R.string.retry_text) { dialog, _ ->
				viewModel.retryLoading()
				dialog.dismiss()
			}.setNegativeButton(R.string.dismiss_button) { dialog, _ ->
				dialog.dismiss()
			}
			.create()

		errorDialog.show()
	}

	private fun setUpRevisedContent() {

		val context = requireContext()

		viewLifecycleOwner.launchAndRepeatOnLifeCycle(Lifecycle.State.STARTED) {
			viewModel.loadedContent.collect { resourceContent ->
				resourceContent?.let {
					// Title
					binding.topAppBar.title = resourceContent.alt

					// Image
					val request = ImageRequest.Builder(context)
						.data(resourceContent.sources.portrait)
						.target(binding.image)
						.build()
					context.imageLoader.enqueue(request)

					setUpMenuCallback(resourceContent)
				}
			}
		}

		viewLifecycleOwner.launchAndRepeatOnLifeCycle(Lifecycle.State.STARTED) {
			viewModel.isContentLoading.collect { isLoading ->
				binding.loadingDetails.root.isVisible = isLoading
				binding.photoActions.root.isVisible = !isLoading
			}
		}

		viewLifecycleOwner.launchAndRepeatOnLifeCycle(Lifecycle.State.STARTED) {
			viewModel.uiEvent.collect { event ->
				when (event) {
					is UiEvent.ShowDialog -> showErrorDialog(message = event.content)
				}
			}
		}
	}

	private fun setContentImage() {

		val photoResource = navArgs.photo


		val color = Color.parseColor(photoResource.placeHolderColor)
		binding.image.setBackgroundColor(color)

	}

	private fun setShareButton() {
		val context = requireContext()
		val photoRes = navArgs.photo

		val intent = Intent(Intent.ACTION_SEND).apply {
			type = "text/plain"
			putExtra(Intent.EXTRA_TEXT, photoRes.photoUrl)
		}
		val shareIntent = Intent.createChooser(intent, null)

		binding.photoActions.shareButton.setOnClickListener {
			context.startActivity(shareIntent)
		}
	}

	private fun setDownloadButton() {

		val optionsBottomSheet = DownloadOptionsBottomSheet().apply sheet@{
			(this@sheet.dialog as? BottomSheetDialog)?.apply {
				behavior.isHideable = true
				behavior.isFitToContents = true
				behavior.saveFlags = BottomSheetBehavior.SAVE_NONE
			}
		}
		optionsBottomSheet.onOptionSelect(viewModel::onDownloadOptionSelected)

		binding.photoActions.downloadPictureButton.setOnClickListener {
			optionsBottomSheet.show(parentFragmentManager, DownloadOptionsBottomSheet.TAG)
		}

	}

}