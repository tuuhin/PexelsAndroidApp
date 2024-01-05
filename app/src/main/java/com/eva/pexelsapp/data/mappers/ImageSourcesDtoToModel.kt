package com.eva.pexelsapp.data.mappers

import com.eva.pexelsapp.data.remote.dto.ImageSourcesDto
import com.eva.pexelsapp.domain.models.ImagesUrls

fun ImageSourcesDto.toUrls(): ImagesUrls = ImagesUrls(
	landScape = landScape,
	large = large,
	large2x = large2x,
	medium = medium,
	original = original,
	portrait = portrait,
	small = small,
	tiny = tiny
)