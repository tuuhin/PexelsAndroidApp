package com.eva.pexelsapp.domain.models

import com.eva.pexelsapp.domain.enums.OrientationOptions
import com.eva.pexelsapp.domain.enums.SizeOptions


data class SearchFilters(
	val orientation: OrientationOptions? = null,
	val sizeOption: SizeOptions? = null,
)
