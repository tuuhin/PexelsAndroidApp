package com.eva.pexelsapp.data.remote

import com.eva.pexelsapp.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ImagesApi {

	@GET
	@Streaming
	suspend fun downloadImageFromUrl(@Url url: String): Response<ResponseBody>

	companion object {
		val instance: ImagesApi = Retrofit.Builder()
			.baseUrl(BuildConfig.BASE_URL)
			.build()
			.create(ImagesApi::class.java)
	}
}