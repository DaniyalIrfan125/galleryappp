package com.danialz.avrioc.fragments.showimagefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.danialz.avrioc.R
import com.danialz.avrioc.SharedViewModel
import kotlinx.android.synthetic.main.show_image_fragment.*

class ShowImageFragment : Fragment() {

    private val viewModel: ShowImageViewModel by viewModels()
    lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.show_image_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.imageViewUri.observe(viewLifecycleOwner) {
            it?.let {
                Glide.with(requireContext()).load(it)
                    .fitCenter().into(imageView)
            }
        }

    }
}