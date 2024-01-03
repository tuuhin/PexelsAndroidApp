package com.eva.pexelsapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageSourcesDto(
	@Json(name = "landscape") val landScape: String,
	@Json(name = "large") val large: String,
	@Json(name = "large2x") val large2x: String,
	@Json(name = "medium") val medium: String,
	@Json(name = "original") val original: String,
	@Json(name = "portrait") val portrait: String,
	@Json(name = "small") val small: String,
	@Json(name = "tiny") val tiny: String
)