package com.eva.pexelsapp.presentation.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.repository.CollectionsRepository
import com.eva.pexelsapp.domain.repository.CuratedPhotoRepository
import com.eva.pexelsapp.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
	private val searchRepository: SearchRepository,
	curatedPhotoRepository: CuratedPhotoRepository,
	collectionsRepository: CollectionsRepository,
) : ViewModel() {

	private val _searchResults = MutableStateFlow(PagingData.empty<PhotoResource>())

	val searchResults = _searchResults
		.cachedIn(viewModelScope)

	val curatedPhotos = curatedPhotoRepository.curatedPhotos
		.cachedIn(viewModelScope)

	val photoCollections = collectionsRepository.collections
		.cachedIn(viewModelScope)

	fun onSearch(query: String) = viewModelScope.launch {
		val trimmedQuery = query.trim()
		searchRepository.searchPhotoAsFlow(trimmedQuery)
			.collect { results ->
				_searchResults.update { results }
			}
	}

}