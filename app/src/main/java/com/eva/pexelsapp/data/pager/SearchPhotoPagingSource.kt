package com.eva.pexelsapp.data.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import com.eva.pexelsapp.domain.models.SearchFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SearchPhotoPagingSource(
	private val api: PexelsApi,
	private val query: String,
) : PagingSource<Int, PhotoResourceDto>() {

	private val _searchFilters = MutableStateFlow(SearchFilters())

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResourceDto> {
		return withContext(Dispatchers.IO) {
			try {
				val loadSize = params.loadSize
				val key = params.key ?: 1

				val filters = _searchFilters.value

				val results = api.searchPhoto(
					query = query,
					page = key,
					perPage = loadSize,
					orientation = filters.orientation?.string,
					size = filters.sizeOption?.size
				)

				val nextKey = results.next?.let { key + 1 }

				LoadResult.Page(data = results.photos, prevKey = null, nextKey = nextKey)
			} catch (e: HttpException) {
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

	fun setSearchFilters(filters: SearchFilters) = _searchFilters.updateAndGet { filters }


}