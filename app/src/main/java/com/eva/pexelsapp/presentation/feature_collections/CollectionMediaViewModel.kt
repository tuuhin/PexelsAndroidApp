package com.eva.pexelsapp.presentation.feature_collections

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eva.pexelsapp.data.parcelable.PhotoCollectionParcelable
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.repository.CollectionMediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionMediaViewModel @Inject constructor(
	private val repository: CollectionMediaRepository,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {

	val collection: Flow<PagingData<PhotoResource>> =
		repository.collectionMedia.cachedIn(viewModelScope)

	init {
		savedStateHandle.getStateFlow<PhotoCollectionParcelable?>(
			key = "collection",
			initialValue = null
		).onEach { photoCollection ->
			photoCollection?.collectionId?.let(::loadCollectionFromId)
		}.launchIn(viewModelScope)
	}

	private fun loadCollectionFromId(collectionId: String) = viewModelScope.launch {
		repository.loadCollection(id = collectionId)
	}


}