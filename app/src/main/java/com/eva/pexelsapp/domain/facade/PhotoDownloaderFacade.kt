package com.eva.pexelsapp.domain.facade

interface PhotoDownloaderFacade {

	fun downLoadUrl(url: String): Long
}