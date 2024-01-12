package com.eva.pexelsapp.data.mappers

import com.eva.pexelsapp.data.parcelable.ImageParcelable
import com.eva.pexelsapp.data.parcelable.PhotoResourceParcelable
import com.eva.pexelsapp.domain.models.PhotoResource

fun PhotoResource.toParcelable(): PhotoResourceParcelable = PhotoResourceParcelable(
	title = alt,
	photographerUrl = photographerUrl,
	photoGraphBy = photographer,
	placeHolderColor = placeHolderColor,
	images = ImageParcelable(
		medium = sources.medium,
		landScape = sources.landScape,
		original = sources.original,
		portrait = sources.portrait
	)
)