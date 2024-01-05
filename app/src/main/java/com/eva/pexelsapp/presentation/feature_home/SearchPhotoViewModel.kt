package com.eva.pexelsapp.presentation.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchPhotoViewModel @Inject constructor(
	private val searchRepository: SearchRepository
) : ViewModel() {

	private val _searchResults = MutableStateFlow(PagingData.empty<PhotoResource>())

	val searchResults = _searchResults
		.cachedIn(viewModelScope)

	fun onSearch(query: String) = viewModelScope.launch {
		val trimmedQuery = query.trim()
		searchRepository.searchPhotoAsFlow(trimmedQuery)
			.collect { results ->
			_searchResults.update { results }
		}
	}

}