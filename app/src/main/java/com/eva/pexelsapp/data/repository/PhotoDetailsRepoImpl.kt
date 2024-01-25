package com.eva.pexelsapp.data.repository

import com.eva.pexelsapp.data.mappers.toModel
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.domain.enums.WallpaperMode
import com.eva.pexelsapp.domain.facade.PhotoDownloaderFacade
import com.eva.pexelsapp.domain.facade.WallpaperUtilFacade
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.repository.PhotoDetailsRepository
import com.eva.pexelsapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class PhotoDetailsRepoImpl @Inject constructor(
	private val api: PexelsApi,
	private val downloader: PhotoDownloaderFacade,
	private val wallpaperUtil: WallpaperUtilFacade
) : PhotoDetailsRepository {

	override suspend fun getPhotoFromId(id: Int): Resource<PhotoResource> {
		return withContext(Dispatchers.IO) {
			try {
				val response = api.getPhotoFromId(id)
				Resource.Success(data = response.toModel())
			} catch (e: HttpException) {
				Resource.Error(e)
			} catch (e: Exception) {
				Resource.Error(e)
			}
		}
	}

	override fun downloadImage(url: String, imageId: String) {
		try {
			downloader.downloadAsDownload(url = url, imageId = imageId)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	override fun setWallpaper(fileUri: String, mode: WallpaperMode) {
		try {
			when (mode) {
				WallpaperMode.VIA_OTHER_APP -> wallpaperUtil.setWallpaperViaOtherApps(fileUri)
				else -> wallpaperUtil.setWallpaper(fileUri, mode)
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

}