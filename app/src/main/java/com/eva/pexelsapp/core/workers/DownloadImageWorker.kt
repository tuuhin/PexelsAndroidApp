package com.eva.pexelsapp.core.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.eva.pexelsapp.data.remote.ImagesApi
import com.eva.pexelsapp.domain.models.PhotoResource
import com.eva.pexelsapp.presentation.util.extensions.toContentUri
import com.eva.pexelsapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.FileFilter
import java.io.FileOutputStream
import java.time.Duration
import java.util.UUID

private const val WORKER_TAG = "DOWNLOAD_IMAGE_WORKER"

class DownloadImageWorker(
	private val context: Context,
	params: WorkerParameters
) : CoroutineWorker(appContext = context, params = params) {

	override suspend fun doWork(): Result {
		val imageUrl = inputData.getString(WorkParameters.INPUT_URI_KEY)
			?: return Result.failure(
				workDataOf(WorkParameters.ERROR_KEY to WorkParameters.URI_NOT_FOUND)
			)

		val imageId = inputData.getInt(WorkParameters.INPUT_IMAGE_ID_KEY, -1)

		if (imageId == -1) return Result.failure(
			workDataOf(WorkParameters.ERROR_KEY to WorkParameters.IMAGE_ID_NOT_FOUND)
		)

		val imageName = "cache-image-$imageId.png"

		val fileFilter = FileFilter { file -> file.isFile && file.path.endsWith("$imageId.png") }

		return withContext(Dispatchers.IO) {
			try {

				val directory = File(context.cacheDir, "cached_images")
					.apply(File::mkdirs)

				directory.listFiles(fileFilter)
					?.firstOrNull()
					?.let { file ->
						if (file.exists()) {
							Log.d(WORKER_TAG, "FOUND_IN_CACHE")
							val fileUri = file.toContentUri(context)
							return@withContext Result.success(
								workDataOf(WorkParameters.SUCCESS_KEY to fileUri.toString())
							)
						}
					}

				Log.d(WORKER_TAG, "NOT_FOUND_IN_CACHE")

				val response = ImagesApi.instance.downloadImageFromUrl(imageUrl)

				response.body()?.let { body ->

					val bytes = body.bytes()
					val file = File(directory, imageName)

					val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

					FileOutputStream(file).use { outputStream ->
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
					}

					val fileUri = file.toContentUri(context)
					Result.success(
						workDataOf(WorkParameters.SUCCESS_KEY to fileUri.toString())
					)

				} ?: Result.failure(
					workDataOf(WorkParameters.ERROR_KEY to "NO BODY RESPONSE FOUND")
				)
			} catch (e: HttpException) {
				e.printStackTrace()
				val serverError = e.code() in 500..599
				// retry if there is a server error
				if (serverError) Result.retry()
				Result.failure(
					workDataOf(WorkParameters.ERROR_KEY to e.localizedMessage)
				)
			} catch (e: Exception) {
				Result.failure(
					workDataOf(WorkParameters.ERROR_KEY to e.localizedMessage)
				)
			}
		}
	}

	companion object {

		private val constrains = Constraints.Builder()
			.setRequiredNetworkType(NetworkType.CONNECTED)
			.build()

		fun downloadCacheImage(context: Context, photo: PhotoResource): UUID {

			val photoId = "CACHE_IMAGE_FOR_${photo.id}"

			val workData = workDataOf(
				WorkParameters.INPUT_URI_KEY to photo.sources.portrait,
				WorkParameters.INPUT_IMAGE_ID_KEY to photo.id
			)

			val worker = OneTimeWorkRequestBuilder<DownloadImageWorker>()
				.setInputData(workData)
				.setConstraints(constrains)
				.addTag(tag = WorkParameters.WORKER_TAGS)
				.setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofSeconds(5))
				.build()

			WorkManager.getInstance(context)
				.enqueueUniqueWork(photoId, ExistingWorkPolicy.REPLACE, worker)

			return worker.id
		}

		suspend fun cancelWorkerIfNotComplete(
			context: Context,
			workId: UUID,
			onlyCancelIfFinished: Boolean = false
		) {
			withContext(Dispatchers.IO) {
				val workManger = WorkManager.getInstance(context)
				if (!onlyCancelIfFinished) {
					workManger.cancelWorkById(workId)
					return@withContext
				}
				val workInfo = workManger.getWorkInfoById(workId).get()
				val isWorkDone = workInfo.state.isFinished
				if (isWorkDone) workManger.cancelWorkById(workId)
			}
		}

		suspend fun observeWorkerState(
			context: Context,
			workerId: UUID,
			onUriReceived: suspend (Resource<String>) -> Unit,
		) = WorkManager.getInstance(context)
			.getWorkInfoByIdFlow(workerId)
			.cancellable()
			.collect { workInfo ->
				if (workInfo == null) return@collect
				Log.d(WORKER_TAG, "WORKER_STATE-> ${workInfo.state}")
				Log.d(WORKER_TAG, "WORKER_DATA -> ${workInfo.outputData}")

				val successData = workInfo.outputData.getString(WorkParameters.SUCCESS_KEY)
				val failedData = workInfo.outputData.getString(WorkParameters.ERROR_KEY)

				when (workInfo.state) {
					WorkInfo.State.SUCCEEDED -> successData?.let {
						onUriReceived(Resource.Success(data = it))
					}

					WorkInfo.State.FAILED -> failedData?.let {
						onUriReceived(Resource.Error(message = it))
					}

					WorkInfo.State.RUNNING -> onUriReceived(Resource.Loading)

					else -> {}
				}
			}
	}
}