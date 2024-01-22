package com.eva.pexelsapp.utils

sealed interface UiEvent {

	data class ShowDialog(val content: String) : UiEvent
}