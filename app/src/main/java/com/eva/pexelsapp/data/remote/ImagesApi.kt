package com.eva.pexelsapp.data.remote

import com.eva.pexelsapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.time.Duration

interface ImagesApi {

	@GET
	@Streaming
	suspend fun downloadImageFromUrl(@Url url: String): Response<ResponseBody>

	companion object {

		private val client = OkHttpClient.Builder()
			.connectTimeout(Duration.ofSeconds(5))
			.build()

		val instance: ImagesApi = Retrofit.Builder()
			.baseUrl(BuildConfig.BASE_URL)
			.client(client)
			.build()
			.create()
	}
}