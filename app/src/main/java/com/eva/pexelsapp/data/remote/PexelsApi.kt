package com.eva.pexelsapp.data.remote

import com.eva.pexelsapp.BuildConfig
import com.eva.pexelsapp.data.remote.dto.PaginatedCollectionMediaDto
import com.eva.pexelsapp.data.remote.dto.PaginatedCollectionWrapperDto
import com.eva.pexelsapp.data.remote.dto.PaginatedPhotoWrapperDto
import com.eva.pexelsapp.data.remote.dto.PhotoResourceDto
import com.eva.pexelsapp.data.remote.interceptor.AuthenticationInterceptor
import com.eva.pexelsapp.domain.enums.SortOrder
import com.eva.pexelsapp.utils.AppConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PexelsApi {

	/**
	 * This endpoint enables you to search Pexels for any topic that you would like.
	 * For example your query could be something.jpg broad like Nature, Tigers, People.
	 * Or it could be something.jpg specific like Group of people working
	 */
	@GET("/v1/search")
	suspend fun searchPhoto(
		@Query("query") query: String,
		@Query("orientation") orientation: String? = null,
		@Query("size") size: String? = null,
		@Query("color") color: String? = null,
		@Query("page") page: Int = AppConstants.API_INITIAL_PAGE,
		@Query("per_page") perPage: Int = AppConstants.API_PER_PAGE_SIZE,
	): PaginatedPhotoWrapperDto

	/**
	 * This endpoint enables you to receive real-time photos curated by the Pexels team.
	 */
	@GET("/v1/curated")
	suspend fun curatedPhotos(
		@Query("page") page: Int = AppConstants.API_INITIAL_PAGE,
		@Query("per_page") perPage: Int = AppConstants.API_PER_PAGE_SIZE,
	): PaginatedPhotoWrapperDto

	/**
	 * This endpoint returns all featured collections on Pexels.
	 */
	@GET("/v1/collections/featured")
	suspend fun featuredCollections(
		@Query("page") page: Int = AppConstants.API_INITIAL_PAGE,
		@Query("per_page") perPage: Int = AppConstants.API_PER_PAGE_SIZE,
	): PaginatedCollectionWrapperDto

	/**
	 * This endpoint returns all of your collections.
	 */
	@GET("/v1/collections")
	suspend fun photoCollections(
		@Query("page") page: Int = AppConstants.API_INITIAL_PAGE,
		@Query("per_page") perPage: Int = AppConstants.API_PER_PAGE_SIZE,
	): PaginatedCollectionWrapperDto

	/**
	 * This endpoint returns all the media (photos and videos) within a single collection.
	 * You can filter to only receive photos or videos using the type parameter.
	 */
	@GET("/v1/collections/{id}")
	suspend fun getCollectionMedia(
		@Path("id") collectionId: String,
		@Query("sort") sort: String? = SortOrder.ASC.order,
		@Query("type") type: String = AppConstants.COLLECTION_MEDIA_TYPE,
		@Query("page") page: Int = AppConstants.API_INITIAL_PAGE,
		@Query("per_page") perPage: Int = AppConstants.COLLECTION_MEDIA_PER_PAGE,
	): PaginatedCollectionMediaDto

	/**
	 * Retrieve a specific Photo from its id.
	 */
	@GET("/v1/photos/{id}")
	suspend fun getPhotoFromId(
		@Path("id") id: Int
	): PhotoResourceDto?


	companion object {


		private val moshiBuilder = Moshi.Builder()
			.addLast(KotlinJsonAdapterFactory())
			.build()

		private val okHttpClient = OkHttpClient.Builder()
			.addInterceptor(AuthenticationInterceptor)
			.build()

		val instance: PexelsApi = Retrofit.Builder()
			.baseUrl(BuildConfig.BASE_URL)
			.addConverterFactory(MoshiConverterFactory.create(moshiBuilder))
			.client(okHttpClient)
			.build()
			.create(PexelsApi::class.java)
	}

}