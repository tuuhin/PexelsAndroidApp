package com.eva.pexelsapp.data.mappers

import com.eva.pexelsapp.data.parcelable.PhotoResourceParcelable
import com.eva.pexelsapp.domain.models.PhotoResource

fun PhotoResource.toParcelable(): PhotoResourceParcelable = PhotoResourceParcelable(
	id = id,
	placeHolderColor = placeHolderColor,
	photoUrl = url
)