package com.eva.pexelsapp.domain.facade

interface PhotoDownloaderFacade {

	fun downloadAsDownload(
		url: String,
		imageId: String? = null
	): Long

	fun cancelDownload(id: Long)

}