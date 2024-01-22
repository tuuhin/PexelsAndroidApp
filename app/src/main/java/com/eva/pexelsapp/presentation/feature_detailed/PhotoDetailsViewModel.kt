package com.eva.pexelsapp.presentation.feature_detailed

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.pexelsapp.data.parcelable.PhotoResourceParcelable
import com.eva.pexelsapp.domain.models.PhotoDownloadOptions
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.repository.PhotoDetailsRepository
import com.eva.pexelsapp.utils.Resource
import com.eva.pexelsapp.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
	private val repository: PhotoDetailsRepository,
	private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private val _revisedContent = MutableStateFlow<PhotoResource?>(null)
	val loadedContent = _revisedContent.asStateFlow()

	private val _isContentLoading = MutableStateFlow(true)
	val isContentLoading = _isContentLoading.asStateFlow()

	private val _uiEvent = MutableSharedFlow<UiEvent>()
	val uiEvent = _uiEvent.asSharedFlow()

	init {
		savedStateHandle.getStateFlow<PhotoResourceParcelable?>(key = "photo", initialValue = null)
			.onEach { photoRes -> photoRes?.id?.let(::loadPhoto) }
			.launchIn(viewModelScope)
	}

	private fun loadPhoto(photoId: Int) = viewModelScope.launch {
		when (val resource = repository.getPhotoFromId(id = photoId)) {
			is Resource.Error -> {
				_isContentLoading.update { false }
				_revisedContent.update { null }
				_uiEvent.emit(UiEvent.ShowDialog(content = resource.error?.message ?: ""))
			}

			is Resource.Success -> {
				_isContentLoading.update { false }
				_revisedContent.update { resource.data }
			}

			else -> {}
		}
	}

	fun retryLoading() {
		val navParams = savedStateHandle.get<PhotoResourceParcelable?>(key = "photo")
		navParams?.id?.let(::loadPhoto)
	}

	fun onDownloadOptionSelected(option: PhotoDownloadOptions) {
		val image = _revisedContent.value?.sources
		val url = when (option) {
			PhotoDownloadOptions.PORTRAIT -> image?.portrait
			PhotoDownloadOptions.LANDSCAPE -> image?.landScape
			PhotoDownloadOptions.MEDIUM -> image?.medium
			PhotoDownloadOptions.LARGE -> image?.original
		}
		Log.d("URL ", "$url")
	}

}