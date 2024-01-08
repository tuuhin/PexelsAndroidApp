package com.eva.pexelsapp.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.data.remote.dto.PhotoCollectionDto
import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import com.eva.pexelsapp.data.repository.CollectionsRepoImpl
import com.eva.pexelsapp.data.repository.CuratedPhotoRepoImpl
import com.eva.pexelsapp.data.repository.SearchRepositoryImpl
import com.eva.pexelsapp.domain.repository.CollectionsRepository
import com.eva.pexelsapp.domain.repository.CuratedPhotoRepository
import com.eva.pexelsapp.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

	@Provides
	@ViewModelScoped
	fun providesSearchRepository(
		api: PexelsApi,
		pagingConfig: PagingConfig
	): SearchRepository = SearchRepositoryImpl(api = api, pagerConfig = pagingConfig)

	@Provides
	@ViewModelScoped
	fun providesCuratedPhotoRepository(
		pager: Pager<Int, PhotoResourceDto>
	): CuratedPhotoRepository = CuratedPhotoRepoImpl(pager)


	@Provides
	@ViewModelScoped
	fun providesCollectionRepository(
		pager: Pager<Int, Pair<PhotoCollectionDto, PhotoResourceDto?>>
	): CollectionsRepository = CollectionsRepoImpl(pager = pager)
}