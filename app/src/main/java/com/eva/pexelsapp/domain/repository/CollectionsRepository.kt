package com.eva.pexelsapp.domain.repository

import androidx.paging.PagingData
import com.eva.pexelsapp.domain.models.PhotoCollection
import kotlinx.coroutines.flow.Flow

interface CollectionsRepository {

	val collections: Flow<PagingData<PhotoCollection>>
}