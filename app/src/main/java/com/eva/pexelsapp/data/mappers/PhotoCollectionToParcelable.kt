package com.eva.pexelsapp.data.mappers

import com.eva.pexelsapp.data.parcelable.PhotoCollectionParcelable
import com.eva.pexelsapp.domain.models.PhotoCollection

fun PhotoCollection.toParcelable() =
	PhotoCollectionParcelable(collectionId = id, title = title, desc = description)