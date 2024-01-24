package com.eva.pexelsapp.core.workers

object WorkParameters {
	//	WORK DATA KEYS
	const val INPUT_URI_KEY = "INPUT_IMAGE_URL_KEY"
	const val INPUT_IMAGE_ID_KEY = "INPUT_IMAGE_ID_KEY"
	const val SUCCESS_KEY = "SUCCESS_KEY"
	const val ERROR_KEY = "ERROR_KEY"

	//	 WORK DATA VALUES
	const val URI_NOT_FOUND = "INPUT DATA URI IS NOT FOUND"
	const val IMAGE_ID_NOT_FOUND = "INPUT DATA IMAGE ID NOT FOUND"

	// TAGS
	const val WORKER_TAGS = "IMAGE_DOWNLOAD_WORKER"
}