package com.eva.pexelsapp.data.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CollectionMediaPagingSource(
	private val api: PexelsApi,
	private val collectionId: String,
) : PagingSource<Int, PhotoResourceDto>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResourceDto> {
		val key = params.key ?: 1
		val pageSize = params.loadSize
		return withContext(Dispatchers.IO) {
			try {
				val response = api.getCollectionMedia(
					collectionId = collectionId,
					page = key,
					perPage = pageSize
				)

				val nextKey = response.next?.let { key + 1 }
				LoadResult.Page(data = response.media, prevKey = null, nextKey = nextKey)
			} catch (e: HttpException) {
				LoadResult.Error(throwable = e)
			} catch (e: Exception) {
				LoadResult.Error(throwable = e)
			}
		}
	}

	override fun getRefreshKey(state: PagingState<Int, PhotoResourceDto>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.nextKey?.minus(1)
		}
	}
}
