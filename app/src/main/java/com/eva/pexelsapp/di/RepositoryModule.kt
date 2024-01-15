package com.eva.pexelsapp.di

import com.eva.pexelsapp.data.repository.CollectionMediaRepoImpl
import com.eva.pexelsapp.data.repository.CollectionsRepoImpl
import com.eva.pexelsapp.data.repository.CuratedPhotoRepoImpl
import com.eva.pexelsapp.data.repository.SearchRepositoryImpl
import com.eva.pexelsapp.domain.repository.CollectionMediaRepository
import com.eva.pexelsapp.domain.repository.CollectionsRepository
import com.eva.pexelsapp.domain.repository.CuratedPhotoRepository
import com.eva.pexelsapp.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

	@Binds
	@ViewModelScoped
	abstract fun bindsSearchRepo(
		impl: SearchRepositoryImpl
	): SearchRepository

	@Binds
	@ViewModelScoped
	abstract fun bindsCuratedPhotoRepo(
		impl: CuratedPhotoRepoImpl
	): CuratedPhotoRepository

	@Binds
	@ViewModelScoped
	abstract fun bindsCollectionRepo(
		impl: CollectionsRepoImpl
	): CollectionsRepository

	@Binds
	@ViewModelScoped
	abstract fun bindCollectionMediaRepo(
		impl: CollectionMediaRepoImpl
	): CollectionMediaRepository

}