package com.eva.pexelsapp.presentation.util.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun LifecycleOwner.launchAndRepeatOnLifeCycle(
	state: Lifecycle.State,
	block: suspend CoroutineScope.() -> Unit
) = lifecycleScope.launch {
	repeatOnLifecycle(state = state, block = block)
}
