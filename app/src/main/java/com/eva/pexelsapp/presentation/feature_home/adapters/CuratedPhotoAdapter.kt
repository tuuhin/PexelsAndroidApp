package com.eva.pexelsapp.presentation.feature_home.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.pexelsapp.R
import com.eva.pexelsapp.databinding.CarouselPhotosLayoutBinding
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.presentation.util.PhotoResourceComparator
import com.google.android.material.math.MathUtils.lerp

class CuratedPhotoAdapter(
	private val context: Context
) : PagingDataAdapter<PhotoResource, CuratedPhotoAdapter.PhotoViewHolder>(PhotoResourceComparator) {

	inner class PhotoViewHolder(binding: CarouselPhotosLayoutBinding) :
		RecyclerView.ViewHolder(binding.root) {
		val container = binding.carouselItemContainer
		val imageOverlay = binding.carouselImageOverlay
		val image = binding.carouselImage
		val takenBy = binding.photographerName
	}

	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): PhotoViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = CarouselPhotosLayoutBinding.inflate(inflater, parent, false)
		return PhotoViewHolder(binding)
	}

	override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
		val item = getItem(position)

		holder.container.setOnMaskChangedListener { maskRect: RectF ->
			holder.takenBy.translationX = maskRect.left
			holder.takenBy.alpha = lerp(1f, 0f, maskRect.left)
		}

		item?.let {
			holder.takenBy.text = context.getString(R.string.by_photographer, item.photographer)
			val color = Color.parseColor(item.placeHolderColor)
			holder.image.setBackgroundColor(color)
			val request = ImageRequest.Builder(context)
				.data(item.sources.portrait)
				.target(onSuccess = {
					holder.imageOverlay.visibility = View.VISIBLE
					holder.image.setImageDrawable(it)
				})
				.build()
			context.imageLoader.enqueue(request)
		}
	}

}