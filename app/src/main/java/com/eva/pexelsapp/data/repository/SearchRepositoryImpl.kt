package com.eva.pexelsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.eva.pexelsapp.data.mappers.toModel
import com.eva.pexelsapp.data.pager.SearchPhotoPagingSource
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
	private val api: PexelsApi,
	private val pagerConfig: PagingConfig,
) : SearchRepository {

	override fun searchPhotoAsFlow(query: String): Flow<PagingData<PhotoResource>> {
		val searchPager = Pager(
			config = pagerConfig,
			initialKey = 1,
			pagingSourceFactory = { SearchPhotoPagingSource(api, query) })

		val searchFlow = searchPager.flow.map { pagingData ->
			pagingData.map(PhotoResourceDto::toModel)
		}
		return searchFlow
	}

}