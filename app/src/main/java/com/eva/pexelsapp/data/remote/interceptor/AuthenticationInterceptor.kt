package com.eva.pexelsapp.data.remote.interceptor

import android.util.Log
import com.eva.pexelsapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

object AuthenticationInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {

		val request = chain.request()
			.newBuilder()
			.addHeader("Authorization", BuildConfig.API_KEY)
			.build()

		Log.d("REQUEST_URL", request.url.toString())

		return chain.proceed(request)
	}
}