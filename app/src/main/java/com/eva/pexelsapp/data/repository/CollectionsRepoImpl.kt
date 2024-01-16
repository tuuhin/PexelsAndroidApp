package com.eva.pexelsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.eva.pexelsapp.data.mappers.toModel
import com.eva.pexelsapp.data.pager.PhotoCollectionWithResourceDto
import com.eva.pexelsapp.domain.models.PhotoCollection
import com.eva.pexelsapp.domain.repository.CollectionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionsRepoImpl @Inject constructor(
	private val pager: Pager<Int, PhotoCollectionWithResourceDto>
) : CollectionsRepository {
	override val collections: Flow<PagingData<PhotoCollection>>
		get() = pager.flow.map { pagedData ->
			pagedData
				.filter { (_, photo) -> photo != null }
				.map { (collection, photo) ->
					val resModel = photo?.toModel()
					collection.toModel(resModel)
				}
		}

}