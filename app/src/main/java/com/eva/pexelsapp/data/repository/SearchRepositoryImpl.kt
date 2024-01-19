package com.eva.pexelsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.eva.pexelsapp.data.mappers.toModel
import com.eva.pexelsapp.data.pager.SearchPhotoPagingSource
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import com.eva.pexelsapp.domain.enums.OrientationOptions
import com.eva.pexelsapp.domain.enums.SizeOptions
import com.eva.pexelsapp.domain.facade.SearchPreferencesFacade
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.models.SearchFilters
import com.eva.pexelsapp.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
	private val api: PexelsApi,
	private val pagerConfig: PagingConfig,
	private val searchPreferences: SearchPreferencesFacade,
) : SearchRepository {

	private var _searchResults = MutableStateFlow(PagingData.empty<PhotoResource>())

	override val searchFilters: Flow<SearchFilters>
		get() = searchPreferences.readSearchFiltersAsFlow

	override val searchResults: Flow<PagingData<PhotoResource>>
		get() = _searchResults.asStateFlow()

	override suspend fun searchPhoto(query: String) {

		val pagingSource = SearchPhotoPagingSource(api = api, query = query)

		pagingSource.setSearchFilters(filters = searchPreferences.readSearchFilter)

		val searchPager = Pager(
			config = pagerConfig,
			initialKey = 1,
			pagingSourceFactory = { pagingSource },
		)

		val searchFlow = searchPager.flow.map { data -> data.map(PhotoResourceDto::toModel) }
		_searchResults.emitAll(searchFlow)
	}

	override suspend fun setSearchFiltersOrientation(option: OrientationOptions?) {
		searchPreferences.setOrientationOption(option)
	}

	override suspend fun setSearchFilterSize(option: SizeOptions?) {
		searchPreferences.setSizeOption(option)
	}
}