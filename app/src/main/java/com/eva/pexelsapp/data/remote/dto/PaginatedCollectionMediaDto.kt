package com.eva.pexelsapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaginatedCollectionMediaDto(
	@Json(name = "id") val id: String,
	@Json(name = "page") val page: Int,
	@Json(name = "per_page") val perPage: Int,
	@Json(name = "total_results") val totalResults: Int,
	@Json(name = "next_page") val next: String? = null,
	@Json(name = "prev_page") val previous: String? = null,
	@Json(name = "media") val media: List<PhotoResourceDto>
)
