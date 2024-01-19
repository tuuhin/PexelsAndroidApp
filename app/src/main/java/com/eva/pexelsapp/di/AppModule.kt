package com.eva.pexelsapp.di

import android.content.Context
import com.eva.pexelsapp.data.datastore.SearchPreferencesImpl
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.domain.facade.SearchPreferencesFacade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun providesApi(): PexelsApi = PexelsApi.instance

	@Provides
	@Singleton
	fun providesSearchFilter(@ApplicationContext context: Context): SearchPreferencesFacade =
		SearchPreferencesImpl(context)

}