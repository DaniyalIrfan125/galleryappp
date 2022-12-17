package com.danialz.avrioc

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danialz.avrioc.data.GenericVideosAndImagesModel

class SharedViewModel : ViewModel() {


    var arrayListOfGenericData = MutableLiveData<ArrayList<GenericVideosAndImagesModel>>()
    var imageViewUri = MutableLiveData<Uri>()

}