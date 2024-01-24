package com.eva.pexelsapp.di

import android.content.Context
import com.eva.pexelsapp.core.downloader.PhotoDownloaderImpl
import com.eva.pexelsapp.core.wallpaper.WallpaperUtilImpl
import com.eva.pexelsapp.data.datastore.SearchPreferencesImpl
import com.eva.pexelsapp.data.remote.PexelsApi
import com.eva.pexelsapp.domain.facade.PhotoDownloaderFacade
import com.eva.pexelsapp.domain.facade.SearchPreferencesFacade
import com.eva.pexelsapp.domain.facade.WallpaperUtilFacade
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

	@Provides
	@Singleton
	fun providesDownloadManager(@ApplicationContext context: Context): PhotoDownloaderFacade =
		PhotoDownloaderImpl(context = context)

	@Provides
	@Singleton
	fun providesWallperSetter(@ApplicationContext context: Context): WallpaperUtilFacade =
		WallpaperUtilImpl(context)

}