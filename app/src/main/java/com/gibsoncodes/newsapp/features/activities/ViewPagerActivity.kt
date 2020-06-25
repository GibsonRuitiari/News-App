package com.gibsoncodes.newsapp.features.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.gibsoncodes.newsapp.R
import com.gibsoncodes.newsapp.adapters.ViewPagerAdapter
import com.gibsoncodes.newsapp.common.AppPreferences
import com.gibsoncodes.newsapp.databinding.ActivityViewPagerBinding
import com.gibsoncodes.newsapp.model.ViewPagerPresentation
import org.koin.android.ext.android.inject
import kotlin.math.abs

class ViewPagerActivity : AppCompatActivity() {
private lateinit var binding:ActivityViewPagerBinding
    private val adapter by lazy { ViewPagerAdapter() }
    private val sharedPref:AppPreferences by inject()
    private lateinit var presentationList:List<ViewPagerPresentation>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this@ViewPagerActivity,R.layout.activity_view_pager)
        binding.viewPager.let {
            it.clipChildren=false
            it.clipToPadding=false
            it.offscreenPageLimit=1
        }
        binding.viewPager.orientation=ViewPager2.ORIENTATION_HORIZONTAL
        presentationList= listOf(
            ViewPagerPresentation(resources.openRawResource(R.raw.politics),getString(R.string.politics),getString(R.string.politics_text)),
            ViewPagerPresentation(resources.openRawResource(R.raw.business),getString(R.string.business),getString(R.string.business_text)),
            ViewPagerPresentation(resources.openRawResource(R.raw.music),getString(R.string.music),getString(R.string.music_text)),
            ViewPagerPresentation(resources.openRawResource(R.raw.sports),getString(R.string.sports),getString(R.string.sport_text)))
        adapter.submitList(presentationList)
        binding.viewPager.adapter=adapter
        binding.viewPager.setPageTransformer(ViewPager2.PageTransformer { page, position ->
            page.translationX= -62* abs(position)
            page.scaleY= 1-(0.25f * (position))
        })
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position==presentationList.size-1){
                    binding.proceedBtn.animate().alpha(1f).setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()).setListener(object:
                        AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator?) {
                            binding.proceedBtn.visibility= View.VISIBLE
                        }
                    })
                    binding.skipBtn.animate().alpha(0f).setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()).setListener(object:AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator?) {
                            binding.skipBtn.visibility=View.GONE
                        }
                    })
                }else{
                    binding.proceedBtn.animate().alpha(0f).setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()).setListener(object:AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator?) {
                            binding.proceedBtn.visibility=View.GONE
                        }
                    })
                    binding.skipBtn.animate().alpha(1f).setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()).setListener(object:AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator?) {
                            binding.skipBtn.visibility=View.VISIBLE
                        }
                    })

                }
            }
        })
        binding.skipBtn.setOnClickListener {
            sharedPref.saveShowViewPagerStatus(AppPreferences.viewPagerStatus,true)
            startActivity(Intent(this@ViewPagerActivity,SignUpActivity::class.java))
            finish()
        }
        binding.proceedBtn.setOnClickListener {
            sharedPref.saveShowViewPagerStatus(AppPreferences.viewPagerStatus,true)
            startActivity(Intent(this@ViewPagerActivity,SignUpActivity::class.java))
            finish()
        }

    }


    override fun onBackPressed() {
        if (binding.viewPager.currentItem==0)
        super.onBackPressed()
        else{
            binding.viewPager.currentItem=binding.viewPager.currentItem-1
        }
    }
}
