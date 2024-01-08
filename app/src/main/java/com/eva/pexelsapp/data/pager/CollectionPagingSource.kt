package com.eva.pexelsapp.data.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.data.remote.dto.PhotoCollectionDto
import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import retrofit2.HttpException

typealias PhotoCollectionWithResourceDto = Pair<PhotoCollectionDto, PhotoResourceDto?>

class CollectionPagingSource(
	private val api: PexelsApi,
) : PagingSource<Int, PhotoCollectionWithResourceDto>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoCollectionWithResourceDto> {
		return withContext(Dispatchers.IO + SupervisorJob()) {
			try {
				val loadSize = params.loadSize
				val key = params.key ?: 1

				val results = api.featuredCollections(page = key, perPage = loadSize)

				val collectionWithPhotos = results.collections.filter { it.photosCount >= 0 }

				val collectionPhoto = collectionWithPhotos.map {
					async(Dispatchers.IO) {
						api.getCollectionMedia(collectionId = it.id, perPage = 1, page = 1)
					}
				}
				val collectionMedia = collectionPhoto.awaitAll()
					.map { it.media.firstOrNull() }

				val zippedData = collectionWithPhotos.zip(collectionMedia)

				LoadResult.Page(
					data = zippedData,
					prevKey = null,
					nextKey = results.next?.let { key + 1 },
				)
			} catch (e: HttpException) {
				LoadResult.Error(e)
			} catch (e: Exception) {
				LoadResult.Error(e)
			}
		}
	}

	override fun getRefreshKey(state: PagingState<Int, PhotoCollectionWithResourceDto>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.nextKey?.minus(1)
		}
	}
}