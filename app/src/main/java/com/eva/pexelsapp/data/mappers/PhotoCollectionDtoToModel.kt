package com.eva.pexelsapp.data.mappers

import com.eva.pexelsapp.data.remote.dto.PhotoCollectionDto
import com.eva.pexelsapp.domain.models.PhotoCollection
import com.eva.pexelsapp.domain.models.PhotoResource

fun PhotoCollectionDto.toModel(resource: PhotoResource?) = PhotoCollection(
	description = description ?: "",
	id = id,
	photosCount = photosCount,
	title = title,
	resource = resource
)