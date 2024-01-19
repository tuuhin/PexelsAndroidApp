package com.eva.pexelsapp.presentation.feature_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.eva.pexelsapp.R
import com.eva.pexelsapp.databinding.SearchFiltersSheetBinding
import com.eva.pexelsapp.domain.enums.OrientationOptions
import com.eva.pexelsapp.domain.enums.SizeOptions
import com.eva.pexelsapp.domain.models.SearchFilters
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class SearchFilterBottomSheet(
	private val filtersFlow: Flow<SearchFilters> = emptyFlow()
) : BottomSheetDialogFragment() {

	private var _onOrientationChange: ((OrientationOptions?) -> Unit)? = null
	private var _onSizeOptionChange: ((SizeOptions?) -> Unit)? = null


	private var _binding: SearchFiltersSheetBinding? = null

	private val binding: SearchFiltersSheetBinding
		get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = SearchFiltersSheetBinding.inflate(inflater, container, false)


		binding.searchFilterOrientation.orientationOptions.setOnCheckedChangeListener { _, checkedId ->
			val selected = when (checkedId) {
				R.id.orientation_option_portrait -> OrientationOptions.PORTRAIT
				R.id.orientation_option_landscape -> OrientationOptions.LANDSCAPE
				R.id.orientation_option_square -> OrientationOptions.SQUARE
				else -> null
			}
			_onOrientationChange?.invoke(selected)
		}

		binding.searchFilterSizes.sizeOptions.setOnCheckedChangeListener { _, checkedId ->

			val selected = when (checkedId) {
				R.id.size_option_large -> SizeOptions.LARGE
				R.id.size_option_medium -> SizeOptions.MEDIUM
				R.id.size_option_small -> SizeOptions.SMALL
				else -> null
			}

			_onSizeOptionChange?.invoke(selected)
		}

		setFilters()

		return binding.root
	}


	override fun onDestroyView() {
		_binding = null
		super.onDestroyView()
	}


	fun onOrientationChangeListener(listener: (OrientationOptions?) -> Unit) {
		_onOrientationChange = listener
	}

	fun onSizeChangeListener(listener: (SizeOptions?) -> Unit) {
		_onSizeOptionChange = listener
	}

	private fun setFilters() = lifecycleScope.launch {
		viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			filtersFlow.collect { (orientation, size) ->
				val orientationId = when (orientation) {
					OrientationOptions.LANDSCAPE -> R.id.orientation_option_landscape
					OrientationOptions.PORTRAIT -> R.id.orientation_option_portrait
					OrientationOptions.SQUARE -> R.id.orientation_option_square
					else -> R.id.orientation_option_all
				}

				val sizeId = when (size) {
					SizeOptions.LARGE -> R.id.size_option_large
					SizeOptions.MEDIUM -> R.id.size_option_medium
					SizeOptions.SMALL -> R.id.size_option_small
					else -> R.id.size_option_all
				}

				binding.searchFilterOrientation.orientationOptions.check(orientationId)
				binding.searchFilterSizes.sizeOptions.check(sizeId)
			}
		}
	}

	companion object {
		const val TAG = "SEARCH_FILTER_BOTTOM_SHEET_TAG"
	}
}