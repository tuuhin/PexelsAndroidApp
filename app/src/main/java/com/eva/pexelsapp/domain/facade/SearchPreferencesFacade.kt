package com.eva.pexelsapp.domain.facade

import com.eva.pexelsapp.domain.enums.OrientationOptions
import com.eva.pexelsapp.domain.enums.SizeOptions
import com.eva.pexelsapp.domain.models.SearchFilters
import kotlinx.coroutines.flow.Flow

interface SearchPreferencesFacade {

	val readSearchFiltersAsFlow: Flow<SearchFilters>

	val readSearchFilter: SearchFilters

	suspend fun setOrientationOption(option: OrientationOptions? = null)

	suspend fun setSizeOption(option: SizeOptions? = null)
}