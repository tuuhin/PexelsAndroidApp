package com.eva.pexelsapp.domain.repository

import androidx.paging.PagingData
import com.eva.pexelsapp.domain.models.PhotoResource
import kotlinx.coroutines.flow.Flow

interface CuratedPhotoRepository {

	val curatedPhotos: Flow<PagingData<PhotoResource>>
}