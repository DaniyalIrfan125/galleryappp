package com.danialz.avrioc.fragments.gallerydetailslistfragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.danialz.avrioc.AppConstants
import com.danialz.avrioc.R
import com.danialz.avrioc.SharedViewModel
import com.danialz.avrioc.fragments.gallerydetailslistfragment.adapter.GalleryDetailsListRecyclerAdapter
import kotlinx.android.synthetic.main.gallery_details_list_fragment.*

class GalleryDetailsListFragment : Fragment() {


    private val viewModel: GalleryDetailsListViewModel by viewModels()
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gallery_details_list_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        observingDataFromFragment()

    }

    // observing data from fragments
    private fun observingDataFromFragment() {
        sharedViewModel.arrayListOfGenericData.observe(viewLifecycleOwner) {
            it?.let {
                recyclerGallery.adapter = GalleryDetailsListRecyclerAdapter(requireContext(), it) {

                    // if data type video then open through intent else navigate to show image fragment
                        if (it.dataType == AppConstants.DataType.VIDEO) {
                            playVideo(it.uri!!)
                        } else {
                            sharedViewModel.imageViewUri.value = it.uri
                            findNavController().navigate(R.id.action_galleryDetailsListFragment_to_showImageFragment)
                        }


                }
            }
        }
    }


    //function to open video through intent
    private fun playVideo(pathStr: Uri) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            pathStr
        )
        intent.setDataAndType(
            pathStr,
            "video/*"
        )
        startActivity(intent)
    }


}