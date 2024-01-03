package com.eva.pexelsapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoResourceDto(
	@Json(name = "id") val id: Int,
	@Json(name = "width") val width: Int,
	@Json(name = "height") val height: Int,
	@Json(name = "url") val url: String,
	@Json(name = "photographer") val photographer: String,
	@Json(name = "photographer_id") val photographerId: Int,
	@Json(name = "photographer_url") val photographerUrl: String,
	@Json(name = "avg_color") val avgColor: String,
	@Json(name = "src") val sources: ImageSourcesDto,
	@Json(name = "liked") val liked: Boolean,
	@Json(name = "alt") val alt: String,
)