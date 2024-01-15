package com.eva.pexelsapp.presentation.feature_collections.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.dispose
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.pexelsapp.R
import com.eva.pexelsapp.databinding.CollectionMediaResultsBinding
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.presentation.util.PhotoResourceComparator

private typealias CollectionMediaSelectCallback = (CollectionMediaAdapter.PhotoViewHolder, PhotoResource) -> Unit

class CollectionMediaAdapter(
	private val context: Context
) : PagingDataAdapter<PhotoResource, CollectionMediaAdapter.PhotoViewHolder>(PhotoResourceComparator) {


	private var _onMediaSelectCallback: CollectionMediaSelectCallback? = null

	inner class PhotoViewHolder(binding: CollectionMediaResultsBinding) :
		RecyclerView.ViewHolder(binding.root) {
		val container = binding.containerMedia
		val photoTakenBy = binding.mediaPhotographerName
		val image = binding.mediaImage
		val overlay = binding.gradientOverlay
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = CollectionMediaResultsBinding.inflate(inflater, parent, false)
		return PhotoViewHolder(binding)
	}

	override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
		val item = getItem(position)
		item?.let { res ->
			//Photographer name
			holder.photoTakenBy.text = context.getString(R.string.by_photographer, res.photographer)

			val color = Color.parseColor(res.placeHolderColor)
			holder.image.setBackgroundColor(color)

			holder.image.transitionName = context.getString(R.string.transition_photo, res.id)

			// load the image
			val request = ImageRequest.Builder(context)
				.data(res.sources.medium)
				.target(
					onSuccess = { drawable ->
						holder.image.setImageDrawable(drawable)
						holder.overlay.visibility = View.VISIBLE
					},
				).build()

			// enqueue image request
			context.imageLoader.enqueue(request)

			holder.container.setOnClickListener {
				_onMediaSelectCallback?.invoke(holder, res)
			}
		}
	}

	override fun onViewDetachedFromWindow(holder: PhotoViewHolder) {
		super.onViewDetachedFromWindow(holder)
		holder.image.dispose()
	}

	fun onMediaSelect(callback: CollectionMediaSelectCallback) {
		_onMediaSelectCallback = callback
	}

}