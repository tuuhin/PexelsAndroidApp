package com.eva.pexelsapp.presentation.feature_collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.repository.CollectionMediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoCollectionViewModel @Inject constructor(
	private val repository: CollectionMediaRepository
) : ViewModel() {

	val collection: Flow<PagingData<PhotoResource>> =
		repository.collectionMedia.cachedIn(viewModelScope)

	fun loadCollectionFromId(collectionId: String) = viewModelScope.launch {
		// TODO: Add some functionality to show the user its loading
		repository.loadCollection(id = collectionId)
	}

}