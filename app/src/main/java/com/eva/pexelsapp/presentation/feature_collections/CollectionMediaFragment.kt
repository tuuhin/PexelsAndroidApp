package com.eva.pexelsapp.presentation.feature_collections

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.eva.pexelsapp.R
import com.eva.pexelsapp.data.mappers.toParcelable
import com.eva.pexelsapp.databinding.PhotoCollectionFragmentBinding
import com.eva.pexelsapp.presentation.feature_collections.adapter.CollectionMediaAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionMediaFragment : Fragment() {

	private val navArgs by navArgs<CollectionMediaFragmentArgs>()


	private val viewModel by viewModels<CollectionMediaViewModel>()

	private var _binding: PhotoCollectionFragmentBinding? = null


	private val binding: PhotoCollectionFragmentBinding
		get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = PhotoCollectionFragmentBinding.inflate(inflater, container, false)

		// Set up title bar
		setUpTitleBar()
		// Set up the recycle view collection
		setUpCollectionMediaRecycleView()

		return binding.root
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val backPressedCallback = object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				requireActivity().onNavigateUp()
			}
		}

		requireActivity().onBackPressedDispatcher.addCallback(
			viewLifecycleOwner,
			backPressedCallback
		)

	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun setUpTitleBar() {

		binding.collectionTitleBar.title = navArgs.collection.title
		navArgs.collection.desc?.let { desc ->
			binding.collectionTitleBar.subtitle = desc
		}

		binding.collectionTitleBar.setNavigationOnClickListener {
			requireActivity().onNavigateUp()
		}
	}

	private fun setUpCollectionMediaRecycleView(context: Context = requireContext()) {

		val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
		val collectionMediaAdapter = CollectionMediaAdapter(context = context)


		collectionMediaAdapter.onMediaSelect { viewHolder, photo ->
			val navController = findNavController()
			val extras = FragmentNavigator.Extras.Builder()
				.addSharedElement(
					sharedElement = viewHolder.image,
					name = context.getString(R.string.transition_photo_large)
				)
				.build()

			val destination = CollectionMediaFragmentDirections
				.actionCollectionFragmentToPhotoDetailsFragment(photoRes = photo.toParcelable())

			navController.navigate(directions = destination, navigatorExtras = extras)
		}

		collectionMediaAdapter.stateRestorationPolicy =
			RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

		binding.collectionResults.apply {
			this@apply.adapter = collectionMediaAdapter
			this@apply.layoutManager = layoutManager
		}

		lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.collection.collect(collectionMediaAdapter::submitData)
			}
		}

		// show some circular progress indicator depicting loading state
		lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				collectionMediaAdapter.loadStateFlow.collect { state ->

					(state.refresh as? LoadState.Error)?.let { errorState ->
						val errorMessage = errorState.error.localizedMessage
							?: context.getString(R.string.unknown_error_message)

						val title = context.getString(R.string.failed_to_load_collection)

						val dialog = MaterialAlertDialogBuilder(context)
							.setTitle(title)
							.setMessage(errorMessage)
							.setNegativeButton(R.string.close_dialog_text) { dialog, _ -> dialog.dismiss() }
							.setPositiveButton(R.string.retry_text) { dialog, _ ->
								collectionMediaAdapter.retry()
								dialog.dismiss()
							}
							.create()

						dialog.show()
					}

					binding.isCollectionLoading.isVisible = state.refresh is LoadState.Loading
					binding.collectionResults.isVisible = state.refresh is LoadState.NotLoading
				}
			}
		}

	}
}