package com.eva.pexelsapp.data.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoResourceParcelable(
	val id: Int,
	val photoUrl: String,
	val placeHolderColor: String,
) : Parcelable

