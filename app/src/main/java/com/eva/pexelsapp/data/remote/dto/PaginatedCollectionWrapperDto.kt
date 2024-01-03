package com.eva.pexelsapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaginatedCollectionWrapperDto(
	@Json(name = "page") val page: Int,
	@Json(name = "per_page") val perPage: Int,
	@Json(name = "total_results") val totalResults: Int,
	@Json(name = "next_url") val next: String,
	@Json(name = "previous_url") val previous: String,
	@Json(name = "collections") val collections: List<PhotoCollectionDto>
)
