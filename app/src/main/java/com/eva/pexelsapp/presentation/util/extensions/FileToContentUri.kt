package com.eva.pexelsapp.presentation.util.extensions

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun File.toContentUri(context: Context): Uri =
	FileProvider.getUriForFile(context, "${context.packageName}.provider", this)