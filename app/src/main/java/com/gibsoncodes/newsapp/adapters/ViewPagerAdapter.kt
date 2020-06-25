package com.gibsoncodes.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gibsoncodes.newsapp.databinding.VpPresentationBinding
import com.gibsoncodes.newsapp.model.ViewPagerPresentation

class ViewPagerAdapter: ListAdapter<ViewPagerPresentation, ViewPagerAdapter.ViewPagerHolder>(DiffCallback){
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<ViewPagerPresentation>(){
            override fun areContentsTheSame(oldItem: ViewPagerPresentation, newItem: ViewPagerPresentation): Boolean {
                return oldItem==newItem
            }

            override fun areItemsTheSame(oldItem: ViewPagerPresentation, newItem: ViewPagerPresentation): Boolean {
                return oldItem.inputStream== newItem.inputStream
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    inner class ViewPagerHolder(private val binding:VpPresentationBinding): RecyclerView.ViewHolder(binding.root){
        fun bindTo(viewPagerPresentation: ViewPagerPresentation){
            binding.vpPresentation= viewPagerPresentation
            binding.category.text= viewPagerPresentation.category
            binding.categoryText.text= viewPagerPresentation.categoryText
            binding.lottieAnimation.setAnimation(viewPagerPresentation.inputStream,null)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.ViewPagerHolder {
        return ViewPagerHolder(VpPresentationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewPagerHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

}