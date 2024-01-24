package com.eva.pexelsapp.utils

sealed interface UiEvent {

	data class ShowDialog(val content: String, val onRetry: (() -> Unit)? = null) : UiEvent

	data class ShowSnackBar(val message: String) : UiEvent
}