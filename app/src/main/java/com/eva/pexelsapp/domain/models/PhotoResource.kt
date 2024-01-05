package com.eva.pexelsapp.domain.models


data class PhotoResource(
	val id: Int,
	val width: Int,
	val height: Int,
	val url: String,
	val photographer: String,
	val photographerUrl: String,
	val placeHolderColor: String,
	val sources: ImagesUrls,
	val alt: String,
)
