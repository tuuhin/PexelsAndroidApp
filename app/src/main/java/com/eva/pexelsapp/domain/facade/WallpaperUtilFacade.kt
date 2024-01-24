package com.eva.pexelsapp.domain.facade

import com.eva.pexelsapp.domain.enums.WallpaperMode

interface WallpaperUtilFacade {

	fun setWallpaper(imageUri: String, mode: WallpaperMode = WallpaperMode.HOME_AND_LOCK_SCREEN)

	fun setWallpaperViaOtherApps(contentUri: String)
}