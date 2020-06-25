package com.gibsoncodes.newsapp.adapters

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gibsoncodes.newsapp.common.RecyclerViewListener
import com.gibsoncodes.newsapp.databinding.NewsSourcesItemBinding
import com.gibsoncodes.newsapp.model.SourcePresentation
import java.lang.ref.WeakReference

class NewsSourcesAdapter(private val listener:RecyclerViewListener,private val context:Context):ListAdapter<SourcePresentation,NewsSourcesAdapter.ViewHolder>(NewsSourcesDiffUtil) {
    init{
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    companion object{
        val NewsSourcesDiffUtil= object: DiffUtil.ItemCallback<SourcePresentation>(){
            override fun areContentsTheSame(oldItem: SourcePresentation, newItem: SourcePresentation): Boolean {
                return oldItem==newItem
            }

            override fun areItemsTheSame(oldItem: SourcePresentation, newItem: SourcePresentation): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    inner class ViewHolder(private val binding:NewsSourcesItemBinding):RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{
        override fun onClick(p0: View?) {
            when{
                binding.dot.visibility==View.VISIBLE-> binding.dot.visibility=View.GONE
                binding.dot.visibility==View.GONE -> binding.dot.visibility=View.VISIBLE
            }
            weakReference.get()?.onClick(adapterPosition)
        }
        init {
            binding.preferencesContainer.setOnClickListener(this)

        }
        fun bindTo(src: SourcePresentation){
            binding.sources=src
            binding.sourcesName.text= src.name
            binding.dot.text= Html.fromHtml("&#8226")
            binding.dot.setTextColor(getRandomColor())
            binding.executePendingBindings()
        }
        private fun getRandomColor():Int{
            var returnColor= Color.GRAY
            val arrayId =context.resources.getIdentifier("mdcolor_400","array",
                context.packageName)
            if (arrayId!=0){
                val typedArray = context.resources.obtainTypedArray(arrayId)
                val randomColorIndex= (Math.random()* typedArray.length()).toInt()
                returnColor= typedArray.getColor(randomColorIndex,Color.GRAY)
                typedArray.recycle()
            }
            return returnColor
        }
        private val weakReference: WeakReference<RecyclerViewListener> = WeakReference(listener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsSourcesAdapter.ViewHolder {
      return ViewHolder(NewsSourcesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: NewsSourcesAdapter.ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


}