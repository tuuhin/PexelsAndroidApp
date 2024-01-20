package com.eva.pexelsapp.presentation.feature_home.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eva.pexelsapp.R
import com.eva.pexelsapp.databinding.LoadStateLayoutBinding

class CommonFooterAdapter(
	private val context: Context,
) : LoadStateAdapter<CommonFooterAdapter.ViewHolder>() {

	private var _onErrorRetry: (() -> Unit)? = null

	inner class ViewHolder(binding: LoadStateLayoutBinding) :
		RecyclerView.ViewHolder(binding.root) {
		private val _errorText = binding.errorText
		private val _errorLayout = binding.errorLayout
		private val _retryButton = binding.retryButton
		private val _loadLayout = binding.loadingMode
		private val _endOfPage = binding.endOfPagination

		fun bindLoadState(state: LoadState) {
			when (state) {
				is LoadState.Error -> {
					_endOfPage.isVisible = state.endOfPaginationReached
					_errorText.text = state.error.localizedMessage
						?: context.getString(R.string.unknown_error_message)

					_retryButton.setOnClickListener { _onErrorRetry?.invoke() }
				}

				LoadState.Loading -> _endOfPage.isVisible = false
				is LoadState.NotLoading ->
					_endOfPage.isVisible = state.endOfPaginationReached

			}
			_loadLayout.isVisible = loadState is LoadState.Loading
			_errorLayout.isVisible = loadState is LoadState.Error
		}
	}

	override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
		Log.d("STATE", "$loadState")
		holder.bindLoadState(loadState)
	}

	override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = LoadStateLayoutBinding.inflate(inflater, parent, false)
		return ViewHolder(binding)
	}

	fun onRetryCallback(callback: () -> Unit) {
		_onErrorRetry = callback
	}
}