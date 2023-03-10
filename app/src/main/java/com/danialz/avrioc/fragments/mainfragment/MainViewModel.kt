package com.danialz.avrioc.fragments.mainfragment

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.danialz.avrioc.AppConstants
import com.danialz.avrioc.data.GenericVideosAndImagesModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val allVideosAndImages_ =
        MutableLiveData<HashMap<String, ArrayList<GenericVideosAndImagesModel>>>()
    val allVideosAndImages: LiveData<HashMap<String, ArrayList<GenericVideosAndImagesModel>>>
        get() = allVideosAndImages_


    //fetching image and videos together using zip operator of flow
    fun fetchImagesAndVideos() {
        viewModelScope.launch {
            queryVideos()
                .zip(queryImages()) { videosList, imagesList ->

                    //once combine result of images and videos then adding in list and returning in collect function
                    val genericVideosAndImagesModel = mutableListOf<GenericVideosAndImagesModel>()

                    genericVideosAndImagesModel.addAll(imagesList)
                    genericVideosAndImagesModel.addAll(videosList)

                    return@zip genericVideosAndImagesModel
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    //once after collecting data then seprating method is called
                    separateFolderFiles(it)
                }
        }
    }


    //separating lists on the basis of folder names
    private fun separateFolderFiles(list: List<GenericVideosAndImagesModel>) {
        val hashmap = HashMap<String, ArrayList<GenericVideosAndImagesModel>>()

        if (list.isNotEmpty()) {

            //adding separately all images and all videos as per requirement
            hashmap["All Images"] =
                ArrayList(list.filter { s -> s.dataType == AppConstants.DataType.IMAGE })
            hashmap["All Videos"] =
                ArrayList(list.filter { s -> s.dataType == AppConstants.DataType.VIDEO })

            list.forEach {
                if (hashmap.containsKey(it.FolderName)) {
                    hashmap[it.FolderName!!]?.add(it)
                } else {
                    val arrayList = ArrayList<GenericVideosAndImagesModel>()
                    arrayList.add(it)
                    hashmap[it.FolderName!!] = arrayList
                }

            }

        }

        allVideosAndImages_.value = hashmap
    }

    //fetching videos
    private suspend fun queryVideos(): Flow<List<GenericVideosAndImagesModel>> {
        val videosWithFolderList = mutableListOf<GenericVideosAndImagesModel>()

        withContext(Dispatchers.IO) {

            val projection = arrayOf(
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
            )

            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            getApplication<Application>().contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->


                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)


                Log.i(TAG, "Found ${cursor.count} videos")
                while (cursor.moveToNext()) {

                    // Here we'll use the column indexes that we found above.
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(nameColumn)
                    val duration = cursor.getInt(durationColumn)
                    val size = cursor.getInt(sizeColumn)

                    val bucketName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    // Stores column values and the contentUri in a local object
                    // that represents the media file.
                    val videoModel =
                        GenericVideosAndImagesModel(
                            AppConstants.DataType.VIDEO,
                            contentUri,
                            displayName,
                            duration,
                            size,
                            null,
                            bucketName
                        )
                    videosWithFolderList += videoModel


                    // For debugging, we'll output the image objects we create to logcat.
                    Log.v(TAG, "Added video: $videoModel")
                }
            }
        }

        Log.v(TAG, "Found ${videosWithFolderList.size} videos")
        return flow { emit(videosWithFolderList) }
    }

    //fetching all images
    private suspend fun queryImages(): Flow<List<GenericVideosAndImagesModel>> {
        val imagesList = mutableListOf<GenericVideosAndImagesModel>()

        withContext(Dispatchers.IO) {

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA
            )


            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            getApplication<Application>().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)


                Log.i(TAG, "Found ${cursor.count} images")
                while (cursor.moveToNext()) {

                    // Here we'll use the column indexs that we found above.
                    val id = cursor.getLong(idColumn)

                    val displayName = cursor.getString(displayNameColumn)

                    val bucketName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))

                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    val image =
                        GenericVideosAndImagesModel(
                            AppConstants.DataType.IMAGE,
                            contentUri,
                            displayName,
                            null,
                            null,
                            null,
                            bucketName
                        )
                    imagesList += image

                    // For debugging, we'll output the image objects we create to logcat.
                    Log.v("TAG", "Added image: $image")
                }
            }
        }

        Log.v(TAG, "Found ${imagesList.size} images")
        return flow { emit(imagesList) }
    }


}

private const val TAG = "MainActivityViewModel"