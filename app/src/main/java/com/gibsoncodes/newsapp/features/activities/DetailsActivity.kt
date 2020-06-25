package com.gibsoncodes.newsapp.features.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.transition.Transition
import com.gibsoncodes.newsapp.R
import com.gibsoncodes.newsapp.common.CornerType
import com.gibsoncodes.newsapp.common.RoundedCornerTransformation
import com.gibsoncodes.newsapp.databinding.ActivityDetailsBinding
import com.gibsoncodes.newsapp.model.ArticlePresentation
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class DetailsActivity : AppCompatActivity() {
private lateinit var binding:ActivityDetailsBinding
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_details)
        val article:ArticlePresentation= intent.getParcelableExtra("ArticleId")
        val imageView= findViewById<ImageView>(R.id.imageDetail)
        ViewCompat.setTransitionName(imageView,"imageBg")
        binding.description.text= article.description
        binding.timeText.text=article.publishedAt
        binding.title.text=article.title
        binding.readMoreBtn.setOnClickListener {
            val webPage = Uri.parse(article.url)
            val intent= Intent(Intent.ACTION_VIEW,webPage)
            val title:String = resources.getString(R.string.view_with)
            val chooser= Intent.createChooser(intent,title)
            if(intent.resolveActivity(packageManager)!=null){
                startActivity(chooser)
            }

        }
        val span:Spannable= SpannableString(binding.description.text)
        span.setSpan(RelativeSizeSpan(1.5f),0,1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.description.text=span
        val zoneDateTime= ZonedDateTime.parse(article.publishedAt)
        val localDateTime= LocalDateTime.from(zoneDateTime)
        val hours= ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now())
        val hoursPublished:Long
        if (hours>=24){
            hoursPublished= Math.abs(hours/24)
            if (hoursPublished.toInt() == 1){
                binding.timeText.text= "published $hoursPublished ago"
            } else binding.timeText.text= "published $hoursPublished days ago"

        }else if (hours>=1){
            hoursPublished=hours
            if (hoursPublished.toInt()==1){
                binding.timeText.text= "published  $hoursPublished hr ago"
            }else binding.timeText.text= "published $hoursPublished hrs ago"

        }else{
            val minutes= ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now())
            binding.timeText.text="published $minutes mins ago"
        }

        if (article.source?.name!=null){
            binding.sourceText.text= article.source.name
        }else binding.sourceText.text=""

        setSupportActionBar(binding.toolbarDetail)

        val transition= window.sharedElementEnterTransition
        transition.addListener(object : android.transition.Transition.TransitionListener{
            override fun onTransitionResume(p0: android.transition.Transition?) {
            }

            override fun onTransitionPause(p0: android.transition.Transition?) {
            }

            override fun onTransitionCancel(p0: android.transition.Transition?) {
            }

            override fun onTransitionStart(p0: android.transition.Transition?) {
            }

            override fun onTransitionEnd(p0: android.transition.Transition?) {
                Picasso.with(this@DetailsActivity)
                    .load(article.urlToImage)
                    .fit()
                    .noPlaceholder()
                    .centerCrop()
                    .transform(RoundedCornerTransformation(10,0, CornerType.ALL))
                    .noFade()
                    .into(binding.imageDetail)
                transition.removeListener(this)
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@DetailsActivity,MainActivity::class.java))
        finish()
    }
}
