package com.eva.pexelsapp.domain.repository

import com.eva.pexelsapp.domain.enums.WallpaperMode
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.utils.Resource

interface PhotoDetailsRepository {

	suspend fun getPhotoFromId(id: Int): Resource<PhotoResource>

	fun downloadImage(url: String, imageId: String)

	fun setWallpaper(fileUri: String, mode: WallpaperMode)

}