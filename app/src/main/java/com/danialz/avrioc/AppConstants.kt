package com.danialz.avrioc

import androidx.annotation.StringDef

object AppConstants {


    @StringDef(DataType.IMAGE,DataType.VIDEO)
    annotation class DataType {
        companion object {
            const val IMAGE = "img"
            const val VIDEO = "video"
        }
    }
}