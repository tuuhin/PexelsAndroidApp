package com.eva.pexelsapp.core.wallpaper

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.core.content.getSystemService
import com.eva.pexelsapp.R
import com.eva.pexelsapp.domain.enums.WallpaperMode
import com.eva.pexelsapp.domain.facade.WallpaperUtilFacade

class WallpaperUtilImpl(
	private val context: Context
) : WallpaperUtilFacade {

	private val wallpaperManager by lazy { context.getSystemService<WallpaperManager>() }


	override fun setWallpaper(imageUri: String, mode: WallpaperMode) {
		val file = Uri.parse(imageUri)

		if (wallpaperManager?.isSetWallpaperAllowed == false)
			throw WallpaperChangeNotAllowedException()

		val wallpaperScreen = when (mode) {
			WallpaperMode.HOME_SCREEN -> WallpaperManager.FLAG_SYSTEM
			WallpaperMode.LOCK_SCREEN -> WallpaperManager.FLAG_LOCK
			WallpaperMode.HOME_AND_LOCK_SCREEN -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
			else -> throw WallpaperModeInvalidException()
		}

		context.contentResolver.openInputStream(file)?.use { stream ->

			val imageBytes = stream.readBytes()
			val bitmapImage = BitmapFactory
				.decodeByteArray(imageBytes, 0, imageBytes.size)

			wallpaperManager?.setBitmap(
				/* fullImage = */ bitmapImage,
				/* visibleCropHint = */null,
				/* allowBackup = */true,
				/* which = */wallpaperScreen
			)

			val toastText = context.getString(R.string.wallpaper_applied_text)

			Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
		}
	}

	override fun setWallpaperViaOtherApps(contentUri: String) {
		val file = Uri.parse(contentUri)

		val intent = Intent(Intent.ACTION_ATTACH_DATA).apply {
			setDataAndType(file, "image/*")
			addCategory(Intent.CATEGORY_DEFAULT)
			putExtra("mimeType", "image/*")
			flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
		}

		val intentChooserTitle = context.getString(R.string.set_wallpaper_via_other_title)

		val chooser = Intent.createChooser(intent, intentChooserTitle).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK
		}
		context.startActivity(chooser)
	}
}