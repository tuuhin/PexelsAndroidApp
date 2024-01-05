package com.eva.pexelsapp.presentation.feature_home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.pexelsapp.databinding.PhotoResultLayoutBinding
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.presentation.util.PhotoResourceComparator

class PhotoResultsViewAdapter(
	private val context: Context
) : PagingDataAdapter<PhotoResource, PhotoResultsViewAdapter.SearchViewHolder>(
	PhotoResourceComparator
) {

	inner class SearchViewHolder(binding: PhotoResultLayoutBinding) :
		RecyclerView.ViewHolder(binding.root) {
		var photoTakenBy = binding.photographerName
		var altText = binding.altText
		var imageView = binding.image
	}

	override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
		val item = getItem(position)
		item?.let {
			holder.photoTakenBy.text = item.photographer
			holder.altText.text = item.alt

			// correct aspect ratio
			val aspectRatio = item.width.toFloat() / item.height
			holder.imageView.minimumHeight = (holder.imageView.width * aspectRatio).toInt()

			// Background color
			val color = Color.parseColor(item.placeHolderColor)
			holder.imageView.setBackgroundColor(color)

			// load the image
			val request = ImageRequest.Builder(context)
				.data(item.sources.medium)
				.target(holder.imageView)
				.build()

			context.imageLoader.enqueue(request)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = PhotoResultLayoutBinding.inflate(inflater, parent, false)
		return SearchViewHolder(binding)
	}
}