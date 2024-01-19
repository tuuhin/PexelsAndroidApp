package com.eva.pexelsapp.data.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.eva.pexelsapp.SearchFilterOrientationOptions
import com.eva.pexelsapp.SearchFilterProtoMessage
import com.eva.pexelsapp.SearchFilterSizeOptions
import com.eva.pexelsapp.domain.enums.OrientationOptions
import com.eva.pexelsapp.domain.enums.SizeOptions
import com.eva.pexelsapp.domain.facade.SearchPreferencesFacade
import com.eva.pexelsapp.domain.models.SearchFilters
import com.eva.pexelsapp.utils.AppConstants
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream


private val Context.searchFilter: DataStore<SearchFilterProtoMessage> by dataStore(
	fileName = AppConstants.SEARCH_DATA_STORE_FILE,
	serializer = object : Serializer<SearchFilterProtoMessage> {

		override val defaultValue: SearchFilterProtoMessage
			get() = SearchFilterProtoMessage.getDefaultInstance()

		override suspend fun readFrom(input: InputStream): SearchFilterProtoMessage {
			return try {
				SearchFilterProtoMessage.parseFrom(input)
			} catch (e: InvalidProtocolBufferException) {
				throw CorruptionException(e.message ?: "Cannot read proto file", e)
			}
		}

		override suspend fun writeTo(t: SearchFilterProtoMessage, output: OutputStream) {
			output.use(t::writeTo)
		}
	},
)

class SearchPreferencesImpl(
	private val context: Context
) : SearchPreferencesFacade {

	override val readSearchFiltersAsFlow: Flow<SearchFilters>
		get() = context.searchFilter.data.map { protoc ->
			val orientation = protoc.asDomainOrientation
			val sizeOption = protoc.asDomainSize
			SearchFilters(orientation = orientation, sizeOption = sizeOption)
		}

	override val readSearchFilter: SearchFilters
		get() = runBlocking { readSearchFiltersAsFlow.first() }

	override suspend fun setOrientationOption(option: OrientationOptions?) {

		val orientation = option?.let(OrientationOptions::toProtoType)
			?: SearchFilterOrientationOptions.ALL

		context.searchFilter.updateData { protoc ->
			protoc.toBuilder()
				.setOrientation(orientation)
				.build()
		}
	}

	override suspend fun setSizeOption(option: SizeOptions?) {

		val sizeOption = option?.let(SizeOptions::toProtoType)
			?: SearchFilterSizeOptions.All

		context.searchFilter.updateData { protoc ->
			protoc.toBuilder()
				.setSize(sizeOption)
				.build()
		}
	}

}