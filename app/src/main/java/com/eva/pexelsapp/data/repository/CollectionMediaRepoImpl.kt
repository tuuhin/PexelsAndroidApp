package com.eva.pexelsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.eva.pexelsapp.data.mappers.toModel
import com.eva.pexelsapp.data.pager.CollectionMediaPagingSource
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.repository.CollectionMediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionMediaRepoImpl @Inject constructor(
	private val api: PexelsApi,
	private val pagerConfig: PagingConfig,
) : CollectionMediaRepository {

	private val _collectionMedia = MutableStateFlow(PagingData.empty<PhotoResource>())

	override val collectionMedia: Flow<PagingData<PhotoResource>>
		get() = _collectionMedia.asStateFlow()

	override suspend fun loadCollection(id: String) {

		val collectionPager = Pager(config = pagerConfig, initialKey = 1) {
			CollectionMediaPagingSource(api = api, collectionId = id)
		}

		val searchFlow = collectionPager.flow.map { pagingData ->
			pagingData.map(PhotoResourceDto::toModel)
		}
		_collectionMedia.emitAll(searchFlow)
	}
}