package com.eva.pexelsapp.data.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageParcelable(
	val medium: String,
	val original: String,
	val portrait: String,
	val landScape: String,
) : Parcelable
