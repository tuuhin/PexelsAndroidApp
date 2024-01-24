package com.eva.pexelsapp.presentation.feature_detailed

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eva.pexelsapp.databinding.WallpaperOptionsSheetBinding
import com.eva.pexelsapp.domain.enums.WallpaperMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WallpaperOptionsBottomSheet : BottomSheetDialogFragment() {

	private var _binding: WallpaperOptionsSheetBinding? = null

	private var _onOptionSelect: ((WallpaperMode) -> Unit)? = null

	val binding: WallpaperOptionsSheetBinding
		get() = _binding!!


	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = WallpaperOptionsSheetBinding.inflate(inflater, container, false)

		binding.optionHome.setOnClickListener {
			_onOptionSelect?.invoke(WallpaperMode.HOME_SCREEN)
		}

		binding.optionLock.setOnClickListener {
			_onOptionSelect?.invoke(WallpaperMode.LOCK_SCREEN)
		}

		binding.optionHomeLock.setOnClickListener {
			_onOptionSelect?.invoke(WallpaperMode.HOME_AND_LOCK_SCREEN)
		}
		binding.optionThirdPartyApp.setOnClickListener {
			_onOptionSelect?.invoke(WallpaperMode.VIA_OTHER_APP)
		}

		return binding.root
	}

	fun onOptionSelect(callback: (WallpaperMode) -> Unit) {
		_onOptionSelect = callback
	}

	override fun getDialog(): Dialog? {
		val dialog = super.getDialog() as? BottomSheetDialog

		return dialog?.apply {
			behavior.isHideable = true
			behavior.isFitToContents = true
			behavior.saveFlags = BottomSheetBehavior.SAVE_NONE
		}
	}


	override fun onDestroyView() {
		_binding = null
		super.onDestroyView()
	}

	companion object {
		const val TAG = "WALLPAPER_MODE_PICKER"
	}
}