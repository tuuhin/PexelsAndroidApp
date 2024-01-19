package com.eva.pexelsapp.data.datastore

import com.eva.pexelsapp.SearchFilterOrientationOptions
import com.eva.pexelsapp.SearchFilterProtoMessage
import com.eva.pexelsapp.SearchFilterSizeOptions
import com.eva.pexelsapp.domain.enums.OrientationOptions
import com.eva.pexelsapp.domain.enums.SizeOptions


val SearchFilterProtoMessage.asDomainOrientation: OrientationOptions?
	get() = when (this.orientation) {
		SearchFilterOrientationOptions.LANDSCAPE -> OrientationOptions.LANDSCAPE
		SearchFilterOrientationOptions.SQUARE -> OrientationOptions.SQUARE
		SearchFilterOrientationOptions.PORTRAIT -> OrientationOptions.PORTRAIT
		else -> null
	}

val OrientationOptions.toProtoType: SearchFilterOrientationOptions
	get() = when (this) {
		OrientationOptions.LANDSCAPE -> SearchFilterOrientationOptions.LANDSCAPE
		OrientationOptions.PORTRAIT -> SearchFilterOrientationOptions.PORTRAIT
		OrientationOptions.SQUARE -> SearchFilterOrientationOptions.SQUARE
	}

val SearchFilterProtoMessage.asDomainSize: SizeOptions?
	get() = when (size) {
		SearchFilterSizeOptions.LARGE -> SizeOptions.LARGE
		SearchFilterSizeOptions.MEDIUM -> SizeOptions.MEDIUM
		SearchFilterSizeOptions.SMALL -> SizeOptions.SMALL
		else -> null
	}

val SizeOptions.toProtoType: SearchFilterSizeOptions
	get() = when (this) {
		SizeOptions.LARGE -> SearchFilterSizeOptions.LARGE
		SizeOptions.MEDIUM -> SearchFilterSizeOptions.MEDIUM
		SizeOptions.SMALL -> SearchFilterSizeOptions.SMALL
	}
