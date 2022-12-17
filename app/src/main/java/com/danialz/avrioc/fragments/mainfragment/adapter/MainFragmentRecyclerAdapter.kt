package com.danialz.avrioc.fragments.mainfragment.adapter

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
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.danialz.avrioc.AppConstants
import com.danialz.avrioc.R
import com.danialz.avrioc.data.GenericVideosAndImagesModel
import com.danialz.avrioc.databinding.ItemGalleryBinding

class MainFragmentRecyclerAdapter(
    private var context: Context,
    private var hashMap: HashMap<String, ArrayList<GenericVideosAndImagesModel>>,
    val onItemSelected: (ArrayList<GenericVideosAndImagesModel>) -> Unit
) :
    RecyclerView.Adapter<MainFragmentRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_gallery,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    override fun getItemCount(): Int = hashMap.size

    inner class ViewHolder(val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(position: Int) {

            val keys = hashMap.keys.toList()
            binding.count.text = "(" + hashMap[keys[position]]!!.size.toString() + ")"
            binding.folderName.text = keys[position]


            Glide.with(context).load(hashMap[keys[position]]!![0].uri).centerCrop()
                .into(binding.image)


            binding.constraintMainItem.setOnClickListener {
                onItemSelected.invoke(hashMap[keys[position]]!!)
            }


        }
    }

}