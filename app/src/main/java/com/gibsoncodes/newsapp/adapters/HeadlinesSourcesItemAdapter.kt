package com.gibsoncodes.newsapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gibsoncodes.newsapp.common.CornerType
import com.gibsoncodes.newsapp.common.RecyclerViewListener
import com.gibsoncodes.newsapp.common.RoundedCornerTransformation
import com.gibsoncodes.newsapp.databinding.HeadlinesSourceItemBinding
import com.gibsoncodes.newsapp.model.ArticlePresentation
import com.squareup.picasso.Picasso
import java.lang.ref.WeakReference
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class HeadlinesSourcesItemAdapter(private val context:Context,private val listener:RecyclerViewListener)
    :ListAdapter<ArticlePresentation,HeadlinesSourcesItemAdapter.HeadlinesViewHolder>(DIFF_UTIL) {

   init {
       setHasStableIds(true)
   }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    fun getClickedArticle(position: Int):ArticlePresentation{
        return getItem(position)
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    companion object{
        val DIFF_UTIL=object: DiffUtil.ItemCallback<ArticlePresentation>(){
            override fun areContentsTheSame(
                oldItem: ArticlePresentation,
                newItem: ArticlePresentation
            ): Boolean {
                return oldItem.equals(newItem)
            }

            override fun areItemsTheSame(
                oldItem: ArticlePresentation,
                newItem: ArticlePresentation
            ): Boolean {
                return oldItem.title== newItem.title
            }
        }
    }
    inner class HeadlinesViewHolder(private val binding:HeadlinesSourceItemBinding):RecyclerView.ViewHolder(binding.root),
       View.OnClickListener{
       override fun onClick(p0: View?) {
           weakReference.get()?.onClick(adapterPosition)
       }
       private val weakReference:WeakReference<RecyclerViewListener> = WeakReference(listener)
       init {
           binding.headlinesContainer.setOnClickListener(this)
       }
       @SuppressLint("SetTextI18n")
       @RequiresApi(Build.VERSION_CODES.O)
       fun bindTo(articlePresentation: ArticlePresentation){
           binding.articlesPres= articlePresentation
           binding.headlinesTitle.text= articlePresentation.title
           Picasso.with(context)
               .load(articlePresentation.urlToImage)
               .fit()
               .centerCrop()
               .transform(RoundedCornerTransformation(8,0,CornerType.ALL))
               .into(binding.headlinesImg)
           val zoneDateTime= ZonedDateTime.parse(articlePresentation.publishedAt)
           val localDateTime= LocalDateTime.from(zoneDateTime)
           val hours= ChronoUnit.HOURS.between(localDateTime,LocalDateTime.now())
           val hoursPublished:Long
           if (hours>=24){
               hoursPublished= Math.abs(hours/24)
              if (hoursPublished.toInt() == 1){

                binding.headlinesDate.text= "published $hoursPublished ago"
              } else binding.headlinesDate.text="published $hoursPublished days ago"

           }else if (hours>=1){
               hoursPublished=hours
               if (hoursPublished.toInt()==1){
                   binding.headlinesDate.text= "published  $hoursPublished hr ago"
               }else binding.headlinesDate.text= "published $hoursPublished hrs ago"

           }else{
               val minutes= ChronoUnit.MINUTES.between(localDateTime,LocalDateTime.now())
               binding.headlinesDate.text="published $minutes mins ago"
           }

       }
   }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HeadlinesViewHolder {
      return HeadlinesViewHolder(HeadlinesSourceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: HeadlinesViewHolder,
        position: Int
    ) {
        holder.bindTo(getItem(position))
    }


}