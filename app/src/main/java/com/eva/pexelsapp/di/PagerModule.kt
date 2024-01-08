package com.eva.pexelsapp.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.eva.pexelsapp.data.pager.CollectionPagingSource
import com.eva.pexelsapp.data.pager.CuratedPhotosPagingSource
import com.eva.pexelsapp.data.pager.PhotoCollectionWithResourceDto
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import com.eva.pexelsapp.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagerModule {

	@Provides
	@Singleton
	fun providesPagerConfig(): PagingConfig = PagingConfig(
		pageSize = AppConstants.PAGER_PAGE_SIZE,
		enablePlaceholders = false,
		initialLoadSize = AppConstants.PAGER_PAGE_SIZE
	)


	@Provides
	@Singleton
	fun providesCuratedPager(
		config: PagingConfig,
		api: PexelsApi
	): Pager<Int, PhotoResourceDto> =
		Pager(
			config = config,
			initialKey = 1,
			pagingSourceFactory = { CuratedPhotosPagingSource(api) },
		)

	@Provides
	@Singleton
	fun providesCollectionPager(
		api: PexelsApi
	): Pager<Int, PhotoCollectionWithResourceDto> =
		Pager(
			config = PagingConfig(
				pageSize = AppConstants.COLLECTION_PER_PAGE,
				enablePlaceholders = false,
				initialLoadSize = AppConstants.COLLECTION_PER_PAGE
			),
			initialKey = 1,
			pagingSourceFactory = { CollectionPagingSource(api) },
		)
}