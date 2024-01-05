package com.eva.pexelsapp.data.mappers

import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import com.eva.pexelsapp.domain.models.PhotoResource

fun PhotoResourceDto.toModel() = PhotoResource(
	id = id,
	width = width,
	height = height,
	photographer = photographer,
	photographerUrl = photographerUrl,
	url = url,
	placeHolderColor = avgColor,
	sources = sources.toUrls(),
	alt = alt
)

