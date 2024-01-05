package com.eva.pexelsapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers


@HiltAndroidApp
class PexelsApp : Application(), ImageLoaderFactory {
	override fun onCreate() {
		super.onCreate()
		DynamicColors.applyToActivitiesIfAvailable(this)
	}

	override fun newImageLoader(): ImageLoader {

		val memoryCache = MemoryCache.Builder(this)
			.maxSizeBytes(2048)
			.maxSizePercent(.1)
			.build()

		val diskCache = DiskCache.Builder()
			.directory(cacheDir)
			.cleanupDispatcher(Dispatchers.Default)
			.build()

		val debugLogger = DebugLogger()

		return ImageLoader(this).newBuilder()
			.crossfade(true)
			.crossfade(1500)
			.decoderDispatcher(Dispatchers.Default)
			.memoryCachePolicy(CachePolicy.DISABLED)
			.diskCachePolicy(CachePolicy.ENABLED)
			.memoryCache(memoryCache)
			.diskCache(diskCache)
			.logger(debugLogger)
			.build()
	}

}