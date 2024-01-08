package com.eva.pexelsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.eva.pexelsapp.data.mappers.toModel
import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.domain.repository.CuratedPhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CuratedPhotoRepoImpl @Inject constructor(
	private val pager: Pager<Int, PhotoResourceDto>
) : CuratedPhotoRepository {

	override val curatedPhotos: Flow<PagingData<PhotoResource>>
		get() = pager.flow.map { pagingData -> pagingData.map(PhotoResourceDto::toModel) }

}