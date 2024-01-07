package com.eva.pexelsapp.domain.models


data class PhotoCollection(
	val description: String,
	val id: String,
	val photosCount: Int,
	val title: String,
	val resource: PhotoResource? = null,
)
