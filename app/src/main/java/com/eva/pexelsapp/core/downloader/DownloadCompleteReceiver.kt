package com.eva.pexelsapp.core.downloader

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.getSystemService

class DownloadCompleteReceiver : BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {

		if (intent.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return

		val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
		if (downloadId == -1L) return

		val downLoadManager = context.getSystemService<DownloadManager>()

		var toastText = "Image download successfully"

		downLoadManager?.let {
			try {
				val query = DownloadManager.Query().setFilterById(downloadId)
				val cursor = downLoadManager.query(query) ?: return@let

				val titleColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)

				if (cursor.moveToFirst()) {
					val title = cursor.getString(titleColumn)
					toastText = "$title downloaded"
				}
			} catch (_: Exception) {

			}
		}

		val toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT)
		toast.show()
	}
}