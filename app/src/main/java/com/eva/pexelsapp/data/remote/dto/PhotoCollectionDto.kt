package com.eva.pexelsapp.data.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoCollectionDto(
	@Json(name = "id") val id: String,
	@Json(name = "title") val title: String,
	@Json(name = "description") val description: String? = null,
	@Json(name = "private") val isPrivate: Boolean,
	@Json(name = "media_count") val mediaCount: Int,
	@Json(name = "photos_count") val photosCount: Int,
	@Json(name = "videos_count") val videosCount: Int
)