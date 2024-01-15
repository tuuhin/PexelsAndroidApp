package com.eva.pexelsapp.presentation.feature_home.adapters

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
import com.eva.pexelsapp.databinding.CollectionLayoutBinding
import com.eva.pexelsapp.domain.models.PhotoCollection
import com.eva.pexelsapp.presentation.util.PhotoCollectionComparator

private typealias CollectionSelectCallBack = (CollectionPhotoAdapter.CollectionViewHolder, PhotoCollection) -> Unit

class CollectionPhotoAdapter(
	private val context: Context,
) : PagingDataAdapter<PhotoCollection, CollectionPhotoAdapter.CollectionViewHolder>(
	PhotoCollectionComparator
) {

	private var onCollectionSelect: CollectionSelectCallBack? = null

	inner class CollectionViewHolder(binding: CollectionLayoutBinding) :
		RecyclerView.ViewHolder(binding.root) {
		val image = binding.collectionImage
		val title = binding.collectionTitle
		val overlay = binding.gradientOverlay
		val container = binding.collectionContainer
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = CollectionLayoutBinding.inflate(inflater, parent, false)
		return CollectionViewHolder(binding)
	}

	override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
		val item = getItem(position)
		item?.let { model ->
			// Collection Title
			holder.title.text = model.title

			model.resource?.let { res ->
				// Background color
				val color = Color.parseColor(res.placeHolderColor)
				holder.image.setBackgroundColor(color)

				// load the image
				val request = ImageRequest.Builder(context)
					.data(res.sources.medium)
					.target(
						onSuccess = { drawable ->
							holder.image.setImageDrawable(drawable)
							holder.overlay.visibility = View.VISIBLE
						},
					).build()

				context.imageLoader.enqueue(request)
			}

			holder.container.setOnClickListener {
				onCollectionSelect?.invoke(holder, item)
			}
		}
	}

	override fun onViewDetachedFromWindow(holder: CollectionViewHolder) {
		super.onViewDetachedFromWindow(holder)
		holder.image.dispose()
	}

	fun onCollectionSelect(callback: CollectionSelectCallBack) {
		onCollectionSelect = callback
	}
}