package com.danialz.avrioc.fragments.mainfragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.danialz.avrioc.R
import com.danialz.avrioc.SharedViewModel
import com.danialz.avrioc.fragments.mainfragment.adapter.MainFragmentRecyclerAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        implementingClickListener()
        observingDataFromViewModel()

    }

    private fun observingDataFromViewModel() {

        viewModel.allVideosAndImages.observe(viewLifecycleOwner) {
            it?.let {

                if (it.isNotEmpty()) {
                    shimmerMain.visibility = View.GONE
                    recyclerGallery.visibility = View.VISIBLE
                    btnFetchData.visibility = View.GONE

                    recyclerGallery.adapter = MainFragmentRecyclerAdapter(requireContext(), it) {
                        it.let {
                            sharedViewModel.arrayListOfGenericData.value = it
                            findNavController().navigate(R.id.action_mainFragment_to_galleryDetailsListFragment)
                        }
                    }
                }
            }
        }
    }

    private fun implementingClickListener() {
        btnFetchData.setOnClickListener {
            getRunTimePermissions()
        }
    }


    //method to get runtime permissions
    private fun getRunTimePermissions() {

        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            ).withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {


                    if (report.areAllPermissionsGranted()) {
                        shimmerMain.visibility = View.VISIBLE
                        recyclerGallery.visibility = View.GONE
                        btnFetchData.visibility = View.GONE
                        viewModel.fetchImagesAndVideos()
                    } else {
                        shimmerMain.visibility = View.GONE
                        recyclerGallery.visibility = View.GONE
                        btnFetchData.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT)
                            .show()
                    }


                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {

                    token.continuePermissionRequest()

                }
            }).check()


    }


}