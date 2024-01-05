package com.eva.pexelsapp.presentation.util

import androidx.recyclerview.widget.DiffUtil
import com.eva.pexelsapp.domain.models.PhotoResource

object PhotoResourceComparator : DiffUtil.ItemCallback<PhotoResource>() {
	override fun areItemsTheSame(oldItem: PhotoResource, newItem: PhotoResource): Boolean {
		return oldItem.id == newItem.id
	}

	override fun areContentsTheSame(oldItem: PhotoResource, newItem: PhotoResource): Boolean {
		return oldItem == newItem
	}
}