package com.eva.pexelsapp.data.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoResourceParcelable(
	val title: String,
	val photoUrl: String,
	val photoGraphBy: String,
	val photographerUrl: String,
	val placeHolderColor: String,
	val images: ImageParcelable,
) : Parcelable

