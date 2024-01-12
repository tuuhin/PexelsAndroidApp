package com.eva.pexelsapp.data.services

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.eva.pexelsapp.domain.facade.PhotoDownloaderFacade

class PhotoDownloaderImpl(private val context: Context) : PhotoDownloaderFacade {

	private val downLoadManager by lazy { context.getSystemService<DownloadManager>() }

	override fun downLoadUrl(url: String): Long {
		val uri = url.toUri()
		val request = DownloadManager.Request(uri)
			.setMimeType("images/*")
			.setAllowedOverMetered(true)
			.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
			.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
			.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url)

		return downLoadManager?.enqueue(request) ?: -1L
	}

}