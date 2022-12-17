package com.danialz.avrioc.data

import android.net.Uri

data class GenericVideosAndImagesModel(
    val dataType: String,
    val uri: Uri? = null,
    val fileName: String? = null,
    val videoDuration: Int? = null,
    val videoSize: Int? = null,
    var filePath: String? = null,
    var FolderName: String? = null,
    var numberOfFiles: Int = 0
)