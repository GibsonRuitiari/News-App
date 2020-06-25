package com.gibsoncodes.newsapp.features.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.gibsoncodes.newsapp.R
import com.gibsoncodes.newsapp.adapters.NewsSourcesAdapter
import com.gibsoncodes.newsapp.common.*
import com.gibsoncodes.newsapp.databinding.ActivityNewsSourcesBinding
import com.gibsoncodes.newsapp.model.SourcePresentation
import com.gibsoncodes.newsapp.viewmodels.SourcesViewModel
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.activity_news_sources.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsSources : AppCompatActivity() {
private lateinit var binding:ActivityNewsSourcesBinding
    private val viewModel:SourcesViewModel by viewModel()
    private val sharedPref by inject<AppPreferences>()
    private val sharedPrefList = mutableListOf<SourcePresentation>()
    private val sourcesListLocalCopy= mutableListOf<SourcePresentation>()
    private val adapter:NewsSourcesAdapter by lazy {
        NewsSourcesAdapter(object:RecyclerViewListener{
            override fun onClick(position: Int) {
             if (sourcesListLocalCopy[position] in sharedPrefList){
                 sharedPrefList.remove(sourcesListLocalCopy[position])
                 ShowSnackbar(binding.sourcesCord,"Item removed: ${sourcesListLocalCopy[position].name}")
             }else{
                 sharedPrefList.add(sourcesListLocalCopy[position])
                 ShowSnackbar(binding.sourcesCord,"Item added: ${sourcesListLocalCopy[position].name}")

             }
            }
        },this@NewsSources)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=DataBindingUtil.setContentView(this@NewsSources,
           R.layout.activity_news_sources)
        binding.lottieAnim.setAnimation(resources.openRawResource(R.raw.newspaper_spinner),null)
        val observer= Observer<List<SourcePresentation>> {
            sourcesListLocalCopy.addAll(it)
                    adapter.submitList(it)

                }


        setSupportActionBar(binding.NewsSourcesToolbar)
        val gridLayoutManager=GridLayoutManager(this@NewsSources,3)

      viewModel.sourcesList.observe(this,observer)
        binding.sourcesRv.let {
            it.layoutManager=gridLayoutManager
            it.addItemDecoration(GridItemDecoration(dpToPx(6),3,true))
            it.adapter=ScaleInAnimationAdapter(adapter)
        }
        val coroutine = CoroutineScope(Dispatchers.Main)
        binding.doneBtn.setOnClickListener {
            if(sharedPrefList.isNotEmpty()){
                binding.doneBtn.animate().alpha(0f)
                    .setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
                    .setListener(object: AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator?) {
                            binding.doneBtn.visibility=View.GONE
                            binding.spinnerLayout.visibility=View.VISIBLE
                            coroutine.launch {
                                sharedPref.saveNewsSourcesPreferences(AppPreferences.savedSourcesList,sharedPrefList)
                                sharedPref.isSourcesListSaved(AppPreferences.isSourcesListSaved,true)
                                delay(2500)
                                startActivity(Intent(this@NewsSources,MainActivity::class.java))
                                finish()
                            }
                        }
                    })
            }else{
                ShowSnackbar(binding.sourcesCord,"No source choosen please choose a source!")
            }
        }
    }
    private fun dpToPx(dp:Int):Int{
        val resources= resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp.toFloat(),resources.displayMetrics))
    }
}
