package com.eva.pexelsapp.domain.facade

interface PhotoDownloaderFacade {

	fun downloadAsDownload(
		url: String,
		title: String? = null,
		desc: String? = null
	): Long

}