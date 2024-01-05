package com.eva.pexelsapp.di

import com.eva.pexelsapp.data.remote.PexelsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun providesApi(): PexelsApi = PexelsApi.instance


}