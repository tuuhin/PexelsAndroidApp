package com.eva.pexelsapp.presentation.feature_detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eva.pexelsapp.databinding.DownloadOptionSheetBinding
import com.eva.pexelsapp.domain.models.PhotoDownloadOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DownloadOptionsBottomSheet : BottomSheetDialogFragment() {


	private var _onOptionSelect: ((PhotoDownloadOptions) -> Unit)? = null

	private var _binding: DownloadOptionSheetBinding? = null

	private val binding: DownloadOptionSheetBinding
		get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		_binding = DownloadOptionSheetBinding.inflate(inflater, container, false)

		binding.optionLandscape.setOnClickListener {
			_onOptionSelect?.invoke(PhotoDownloadOptions.LANDSCAPE)
		}

		binding.optionPortrait.setOnClickListener {
			_onOptionSelect?.invoke(PhotoDownloadOptions.PORTRAIT)
		}

		binding.optionMedium.setOnClickListener {
			_onOptionSelect?.invoke(PhotoDownloadOptions.MEDIUM)
		}

		binding.optionLarge.setOnClickListener {
			_onOptionSelect?.invoke(PhotoDownloadOptions.LARGE)
		}

		return binding.root
	}

	fun onOptionSelect(callback: (PhotoDownloadOptions) -> Unit) {
		_onOptionSelect = callback
	}

	companion object {
		const val TAG = "DOWNLOAD_OPTION_BOTTOM_SHEET"
	}
}