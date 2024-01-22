package com.eva.pexelsapp.data.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoCollectionParcelable(
	val collectionId: String,
	val title: String,
) : Parcelable
