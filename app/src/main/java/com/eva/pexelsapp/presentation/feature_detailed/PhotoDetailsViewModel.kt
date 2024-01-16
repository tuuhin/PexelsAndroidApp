package com.eva.pexelsapp.presentation.feature_detailed

import androidx.lifecycle.ViewModel
import com.eva.pexelsapp.data.parcelable.ImageParcelable
import com.eva.pexelsapp.domain.models.PhotoDownloadOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor() : ViewModel() {

	fun onDownloadPhoto(image: ImageParcelable, option: PhotoDownloadOptions) {
		val url = when (option) {
			PhotoDownloadOptions.PORTRAIT -> image.portrait
			PhotoDownloadOptions.LANDSCAPE -> image.landScape
			PhotoDownloadOptions.MEDIUM -> image.medium
			PhotoDownloadOptions.LARGE -> image.original
		}
	}
}