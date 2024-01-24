package com.eva.pexelsapp.core.downloader

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.eva.pexelsapp.domain.facade.PhotoDownloaderFacade

class PhotoDownloaderImpl(
	private val context: Context
) : PhotoDownloaderFacade {

	private val downLoadManager by lazy { context.getSystemService<DownloadManager>() }

	override fun downloadAsDownload(url: String, title: String?, desc: String?): Long {
		val uri = url.toUri()
		val request = DownloadManager.Request(uri)
			.setMimeType("images/*")
			.setTitle(title ?: "Downloading...")
			.setDescription(desc ?: "")
			.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
			.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url)

		return downLoadManager?.enqueue(request) ?: -1L
	}

	override fun cancelDownload(id: Long) {
		downLoadManager?.remove(id)
	}

}