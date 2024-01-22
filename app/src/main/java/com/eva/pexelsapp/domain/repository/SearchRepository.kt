package com.eva.pexelsapp.domain.repository

import androidx.paging.PagingData
import com.eva.pexelsapp.domain.enums.OrientationOptions
import com.eva.pexelsapp.domain.enums.SizeOptions
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.models.SearchFilters
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

	val searchResults: Flow<PagingData<PhotoResource>>

	val searchFilters: Flow<SearchFilters>

	suspend fun searchPhoto(query: String)

	suspend fun setSearchFiltersOrientation(option: OrientationOptions? = null)

	suspend fun setSearchFilterSize(option: SizeOptions? = null)

}