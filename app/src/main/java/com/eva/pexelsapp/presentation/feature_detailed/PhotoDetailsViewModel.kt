package com.eva.pexelsapp.presentation.feature_detailed

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.pexelsapp.core.workers.DownloadImageWorker
import com.eva.pexelsapp.data.parcelable.PhotoResourceParcelable
import com.eva.pexelsapp.domain.enums.WallpaperMode
import com.eva.pexelsapp.domain.models.PhotoDownloadOptions
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.repository.PhotoDetailsRepository
import com.eva.pexelsapp.utils.Resource
import com.eva.pexelsapp.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
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

	private val _isWorkerDoingWork = MutableStateFlow(false)
	val isWorkerRunning = _isWorkerDoingWork.asStateFlow()

	private val _contentUriAsString = MutableStateFlow<String?>(null)

	private var _workerListener: Job? = null
	private var _workerId: UUID? = null

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
				_uiEvent.emit(
					UiEvent.ShowDialog(
						content = resource.error?.message ?: "",
						onRetry = ::retryLoading
					)
				)
			}

			is Resource.Success -> {
				_isContentLoading.update { false }
				_revisedContent.update { resource.data }
			}

			else -> {}
		}
	}

	private fun retryLoading() {
		val navParams = savedStateHandle.get<PhotoResourceParcelable?>(key = "photo")
		navParams?.id?.let(::loadPhoto)
	}

	fun onDownloadOptionSelected(option: PhotoDownloadOptions) {
		val content = _revisedContent.value ?: return
		val image = content.sources
		val url = when (option) {
			PhotoDownloadOptions.PORTRAIT -> image.portrait
			PhotoDownloadOptions.LANDSCAPE -> image.landScape
			PhotoDownloadOptions.MEDIUM -> image.medium
			PhotoDownloadOptions.LARGE -> image.original
		}
		repository.downloadImage(url, imageId = "${content.id}")
	}

	fun onSetWallpaperClicked(context: Context, mode: WallpaperMode) {
		_contentUriAsString.value?.let { uriAsString ->
			repository.setWallpaper(fileUri = uriAsString, mode = mode)
		} ?: run {
			startAndListenToWorker(context) { uriAsString ->
				repository.setWallpaper(fileUri = uriAsString, mode = mode)
			}
		}
	}

	private fun startAndListenToWorker(context: Context, onSuccess: (String) -> Unit) {
		val photo = _revisedContent.value ?: return
		// cancel worker listener job if present
		_workerListener?.cancel()
		// create the new job for the listener
		_workerListener = viewModelScope.launch {
			// If there is a worker I'd then cancel the worker
			_workerId?.let { workId ->
				DownloadImageWorker.cancelWorkerIfNotComplete(context = context, workId = workId)
			}
			// creates the new worker
			_workerId = DownloadImageWorker.downloadCacheImage(context, photo)
			// no need to observe if there is no workerId
			val workId = _workerId ?: return@launch
			// observe worker changes
			DownloadImageWorker.observeWorkerState(
				context = context,
				workerId = workId,
				onUriReceived = { resource ->
					when (resource) {
						is Resource.Error -> _uiEvent.emit(UiEvent.ShowDialog(resource.message))
						Resource.Loading -> _uiEvent.emit(UiEvent.ShowSnackBar("Preparing Image..."))
						is Resource.Success -> {
							_contentUriAsString.update { resource.data }
							onSuccess(resource.data)
						}
					}

					when (resource) {
						Resource.Loading -> _isWorkerDoingWork.update { true }
						else -> _isWorkerDoingWork.update { false }
					}
				},
			)
		}
	}

}