package com.eva.pexelsapp.data.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CuratedPhotosPagingSource(
	private val api: PexelsApi,
) : PagingSource<Int, PhotoResourceDto>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResourceDto> {
		return withContext(Dispatchers.IO) {
			try {
				val loadSize = params.loadSize
				val key = params.key ?: 1
				val results = api.curatedPhotos(page = key, perPage = loadSize)
				val nextKey = if (results.page == results.totalResults) null else key + 1
				LoadResult.Page(data = results.photos, prevKey = null, nextKey)
			} catch (e: HttpException) {
				e.printStackTrace()
				LoadResult.Error(e)
			} catch (e: Exception) {
				LoadResult.Error(e)
			}
		}
	}

	override fun getRefreshKey(state: PagingState<Int, PhotoResourceDto>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
		}
	}

}