package com.danialz.avrioc.fragments.gallerydetailslistfragment.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.danialz.avrioc.AppConstants
import com.danialz.avrioc.R
import com.danialz.avrioc.data.GenericVideosAndImagesModel
import com.danialz.avrioc.databinding.ItemGalleryDetailsBinding


class GalleryDetailsListRecyclerAdapter(
    private var context: Context,
    private var arrayListGenericVideosAndImagesModel: ArrayList<GenericVideosAndImagesModel>,
    val onItemSelected: (GenericVideosAndImagesModel) -> Unit
) :
    RecyclerView.Adapter<GalleryDetailsListRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_gallery_details,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    override fun getItemCount(): Int = arrayListGenericVideosAndImagesModel.size

    inner class ViewHolder(val binding: ItemGalleryDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {


            Glide.with(context).load(arrayListGenericVideosAndImagesModel[position].uri)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        binding.imgVideoType.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (arrayListGenericVideosAndImagesModel[position].dataType == AppConstants.DataType.VIDEO) {
                            binding.imgVideoType.visibility = View.VISIBLE
                        } else {
                            binding.imgVideoType.visibility = View.GONE
                        }

                        return false
                    }
                })
                .centerCrop().into(binding.image)

            binding.relativeMain.setOnClickListener {
                onItemSelected.invoke(arrayListGenericVideosAndImagesModel[position])
            }

        }

    }


}