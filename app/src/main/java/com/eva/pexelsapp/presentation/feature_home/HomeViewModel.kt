package com.eva.pexelsapp.presentation.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eva.pexelsapp.domain.enums.OrientationOptions
import com.eva.pexelsapp.domain.enums.SizeOptions
import com.eva.pexelsapp.domain.models.PhotoCollection
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.models.SearchFilters
import com.eva.pexelsapp.domain.repository.CollectionsRepository
import com.eva.pexelsapp.domain.repository.CuratedPhotoRepository
import com.eva.pexelsapp.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
	private val searchRepository: SearchRepository,
	curatedPhotoRepository: CuratedPhotoRepository,
	collectionsRepository: CollectionsRepository,
) : ViewModel() {

	val searchResults: Flow<PagingData<PhotoResource>> =
		searchRepository.searchResults.cachedIn(viewModelScope)

	val curatedPhotos: Flow<PagingData<PhotoResource>> =
		curatedPhotoRepository.curatedPhotos.cachedIn(viewModelScope)

	val photoCollections: Flow<PagingData<PhotoCollection>> =
		collectionsRepository.collections.cachedIn(viewModelScope)

	val searchFilters: Flow<SearchFilters> = searchRepository.searchFilters
		.shareIn(viewModelScope, SharingStarted.Lazily)

	private var _searchJob: Job? = null

	fun onSearch(query: String) {
		_searchJob?.cancel()
		_searchJob = viewModelScope.launch {
			val trimmedQuery = query.trim()
			searchRepository.searchPhoto(trimmedQuery)
		}
	}

	fun setSearchFilterOrientation(orientation: OrientationOptions? = null) =
		viewModelScope.launch { searchRepository.setSearchFiltersOrientation(option = orientation) }

	fun setSearchFilterSize(size: SizeOptions? = null) = viewModelScope.launch {
		searchRepository.setSearchFilterSize(size)
	}
}