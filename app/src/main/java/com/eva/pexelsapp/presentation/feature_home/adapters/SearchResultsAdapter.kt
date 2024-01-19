package com.eva.pexelsapp.presentation.feature_home.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.dispose
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.pexelsapp.R
import com.eva.pexelsapp.databinding.SearchResultsLayoutBinding
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.presentation.util.PhotoResourceComparator

private typealias OnSearchSelectCallBack = (SearchResultsAdapter.PhotoViewHolder, PhotoResource) -> Unit

class SearchResultsAdapter(
	private val context: Context,
) : PagingDataAdapter<PhotoResource, SearchResultsAdapter.PhotoViewHolder>(PhotoResourceComparator) {

	private var _onSearchSelect: OnSearchSelectCallBack? = null

	inner class PhotoViewHolder(binding: SearchResultsLayoutBinding) :
		RecyclerView.ViewHolder(binding.root) {
		val container = binding.container
		val photoTakenBy = binding.photographerName
		val altText = binding.altText
		val imageView = binding.image
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = SearchResultsLayoutBinding.inflate(inflater, parent, false)
		return PhotoViewHolder(binding)
	}

	override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
		val item = getItem(position)
		item?.let { resource ->

			holder.photoTakenBy.text =
				context.getString(R.string.by_photographer, resource.photographer)
			holder.altText.text = item.alt

			// Background color
			val color = Color.parseColor(item.placeHolderColor)
			holder.imageView.setBackgroundColor(color)

			// transition name
			holder.imageView.transitionName =
				context.getString(R.string.transition_photo, resource.id)

			// load the image
			val request = ImageRequest.Builder(context)
				.data(resource.sources.medium)
				.target(holder.imageView)
				.build()

			context.imageLoader.enqueue(request)

			holder.container.setOnClickListener {
				_onSearchSelect?.invoke(holder, item)
			}
		}
	}

	override fun onViewDetachedFromWindow(holder: PhotoViewHolder) {
		super.onViewDetachedFromWindow(holder)
		// Cancels the request if there is one
		holder.imageView.dispose()
	}

	fun onSearchSelect(callback: OnSearchSelectCallBack) {
		_onSearchSelect = callback
	}

}