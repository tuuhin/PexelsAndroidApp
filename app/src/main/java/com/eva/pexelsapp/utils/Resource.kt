package com.eva.pexelsapp.utils

sealed class Resource<out T> {
	data class Success<T>(val data: T, val message: String = "") : Resource<T>()

	data object Loading : Resource<Nothing>()

	data class Error<T>(val error: Throwable? = null, val message: String = "") : Resource<T>()
}