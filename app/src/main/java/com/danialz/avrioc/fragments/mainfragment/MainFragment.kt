package com.danialz.avrioc.fragments.mainfragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import com.danialz.avrioc.R
import com.danialz.avrioc.SharedViewModel
import com.danialz.avrioc.data.*
import com.danialz.avrioc.fragments.mainfragment.adapter.MainFragmentRecyclerAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getRunTimePermissions()
    }

    private fun getRunTimePermissions() {

        //runtime permission to get data so need to ask READ_EXTERNAL_STORAGE permission for SDK, less than android 10. and for
        // sdk > 10 read external storage permission not needed
//
//        Dexter.withContext(requireContext())
//            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//            .withListener(object : PermissionListener {
//                override fun onPermissionGranted(response: PermissionGrantedResponse) {
//
//                    Toast.makeText(requireContext(),"Permission granted",Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onPermissionDenied(response: PermissionDeniedResponse) {
//                    Toast.makeText(requireContext(),"Permission Denied",Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permission: PermissionRequest?,
//                    token: PermissionToken?
//                ) {
//                }
//            }).check()

        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                    viewModel.fetchImagesAndVideos()

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }).check()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]


        viewModel.allVideosAndImages.observe(viewLifecycleOwner) {
            it?.let {

                recyclerGallery.adapter = MainFragmentRecyclerAdapter(requireContext(), it) {
                    it.let {
                        sharedViewModel.arrayListOfGenericData.value = it
                        findNavController().navigate(R.id.action_mainFragment_to_galleryDetailsListFragment)
                    }
                }

                Toast.makeText(requireContext(), "data came", Toast.LENGTH_SHORT).show()
                Log.d("value", it.toString())
            }
        }
    }




}