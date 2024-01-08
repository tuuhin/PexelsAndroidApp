package com.eva.pexelsapp.presentation.util

import androidx.recyclerview.widget.DiffUtil
import com.eva.pexelsapp.domain.models.PhotoCollection

object PhotoCollectionComparator : DiffUtil.ItemCallback<PhotoCollection>() {
	override fun areItemsTheSame(oldItem: PhotoCollection, newItem: PhotoCollection): Boolean {
		return oldItem.id == newItem.id
	}

	override fun areContentsTheSame(oldItem: PhotoCollection, newItem: PhotoCollection): Boolean {
		return oldItem == newItem
	}
}