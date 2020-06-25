package com.gibsoncodes.newsapp.features.activities
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gibsoncodes.newsapp.R
import com.gibsoncodes.newsapp.common.BottomNavigationBehaviour
import com.gibsoncodes.newsapp.databinding.ActivityMainBinding
import com.gibsoncodes.newsapp.features.fragments.AllNewsFragment
import com.gibsoncodes.newsapp.features.fragments.HeadlinesFragment
import com.gibsoncodes.newsapp.features.fragments.ProfileFragment
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso

//TODO: add a swipe refresh layout
//TODO: check for internet connectivity
//TODO: find a work around to prevent the creation of multiple instances of the fragments
//TODO: fix the window exit transition
// note the app size is large due to the resources i.e pictures bundled with app
class MainActivity:AppCompatActivity() {
   private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity,
            R.layout.activity_main)

        val headlinesFragment= HeadlinesFragment()
        supportFragmentManager.beginTransaction().add(R.id.containerView,headlinesFragment).commit()
        Picasso.with(this@MainActivity)
            .load(R.drawable.app_bg)
            .fit() // memory management
            .centerCrop()
            .into(binding.mainImageBg)
        initCollapsingToolbar()
        val layoutParams:CoordinatorLayout.LayoutParams= binding.navView.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior= BottomNavigationBehaviour()
        setSupportActionBar(binding.mainNewsTb)
        binding.mainNewsTb.title= getString(R.string.app_name)
        binding.mainNewsTb.setTitleTextColor(Color.WHITE)
        binding.navView.setOnNavigationItemSelectedListener { item->
            var fragment: Fragment? = null
            when(item.itemId){
                R.id.navigation_headlines->{
                    fragment= HeadlinesFragment()
                }
                R.id.navigation_all_news->{
                    fragment= AllNewsFragment()
                }
                R.id.navigation_profile->{
                    fragment= ProfileFragment()
                }
            }
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.containerView,it)
                    .addToBackStack(null)
                    .commit()

            }
            true
        }
        binding.navView.setOnNavigationItemReselectedListener {
            item->
            var fragment:Fragment?=null
            when(item.itemId) {
                R.id.navigation_headlines -> {
                    fragment = HeadlinesFragment()
                }
                R.id.navigation_all_news -> {
                    fragment = AllNewsFragment()
                }
                R.id.navigation_profile -> {
                    fragment = ProfileFragment()
                }
            }
                fragment?.let {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerView, it)
                        .addToBackStack(null)
                        .commit() }

        }

    }
    private fun initCollapsingToolbar(){
        binding.collapsingMain.title=" "
        binding.mainNewsAppBar.setExpanded(true)
        binding.mainNewsAppBar.addOnOffsetChangedListener(object:AppBarLayout.OnOffsetChangedListener{
            var showing=false
            var scrollRange=-1
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange==-1){
                    scrollRange= appBarLayout?.totalScrollRange!!
                }
                if (scrollRange+verticalOffset==0){
                    binding.collapsingMain.title=getString(R.string.latest)
                    binding.collapsingMain.setCollapsedTitleTextColor(Color.WHITE)
                    showing=true
                }else if (showing){
                    binding.collapsingMain.title=" "
                    showing=false
                }
            }
        })
    }
}