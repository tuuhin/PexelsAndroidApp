package com.eva.pexelsapp.presentation.feature_home.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.pexelsapp.databinding.CollectionMediaLayoutBinding
import com.eva.pexelsapp.domain.models.PhotoCollection
import com.eva.pexelsapp.presentation.util.PhotoCollectionComparator

class CollectionPhotoAdapter(
	private val context: Context
) : PagingDataAdapter<PhotoCollection, CollectionPhotoAdapter.ViewHolder>(PhotoCollectionComparator) {

	inner class ViewHolder(binding: CollectionMediaLayoutBinding) :
		RecyclerView.ViewHolder(binding.root) {
		val collectionImage = binding.collectionImage
		val collectionTitle = binding.collectionTitle
		val overlay = binding.gradientOverlay
		val collectionContainer = binding.collectionContainer
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = getItem(position)
		item?.let { model ->
			holder.collectionTitle.text = model.title

			model.resource?.let { res ->
				// Background color
				val color = Color.parseColor(res.placeHolderColor)
				holder.collectionImage.setBackgroundColor(color)
				// card elevation
				holder.collectionContainer.elevation = 2f
				// load the image
				val request = ImageRequest.Builder(context)
					.data(res.sources.landScape)
					.target(
						onSuccess = { drawable ->
							holder.collectionImage.setImageDrawable(drawable)
							holder.overlay.visibility = View.VISIBLE
						},
					).build()

				context.imageLoader.enqueue(request)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = CollectionMediaLayoutBinding.inflate(inflater, parent, false)
		return ViewHolder(binding)
	}
}