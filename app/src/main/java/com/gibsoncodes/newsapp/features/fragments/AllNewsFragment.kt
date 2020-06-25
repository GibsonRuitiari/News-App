package com.gibsoncodes.newsapp.features.fragments


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.gibsoncodes.newsapp.R
import com.gibsoncodes.newsapp.adapters.NewsItemGenericAdapter
import com.gibsoncodes.newsapp.common.RecyclerViewListener
import com.gibsoncodes.newsapp.common.Resource
import com.gibsoncodes.newsapp.common.ShowSnackbar
import com.gibsoncodes.newsapp.common.Status
import com.gibsoncodes.newsapp.databinding.FragmentAllNewsBinding
import com.gibsoncodes.newsapp.features.activities.DetailsActivity
import com.gibsoncodes.newsapp.model.ArticlePresentation
import com.gibsoncodes.newsapp.viewmodels.AllNewsViewModel
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout


class AllNewsFragment : Fragment() {
private lateinit var binding: FragmentAllNewsBinding
    private lateinit var adapter:NewsItemGenericAdapter
    private lateinit var musicAdapter:NewsItemGenericAdapter
    private lateinit var technologyAdapter:NewsItemGenericAdapter
    private val viewModel:AllNewsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_all_news,container,false)
       return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



         val politicsObserver= Observer<Resource<List<ArticlePresentation>>>{
         when(it.status){
             Status.ERROR-> handleError(it.message)
             Status.LOADING-> handleLoading()
             Status.SUCCESS-> showPoliticsData(it.data)
         }
        }
        val technologyObserver= Observer<Resource<List<ArticlePresentation>>>{
            when(it.status){
                Status.ERROR-> handleError(it.message)
                Status.LOADING-> handleLoading()
                Status.SUCCESS-> showTechnologyData(it.data)
            }
        }
        val sportsObserver= Observer<Resource<List<ArticlePresentation>>>{
            when(it.status){
                Status.ERROR-> handleError(it.message)
                Status.LOADING-> handleLoading()
                Status.SUCCESS-> showSportsData(it.data)
            }
        }
        adapter =NewsItemGenericAdapter(activity?.applicationContext!!,object: RecyclerViewListener{
            override fun onClick(position: Int) {
                val article= adapter.getClickedArticle(position)
                sceneTransitionIntent(article)
            }
        },getString(R.string.politics_))
        musicAdapter = NewsItemGenericAdapter(activity?.applicationContext!!,object: RecyclerViewListener{
            override fun onClick(position: Int) {
                val article= adapter.getClickedArticle(position)
                sceneTransitionIntent(article)
            }
        },getString(R.string.music))

        technologyAdapter=NewsItemGenericAdapter(activity?.applicationContext!!,object: RecyclerViewListener{
            override fun onClick(position: Int) {
                val article= adapter.getClickedArticle(position)
                sceneTransitionIntent(article)

            }
        },getString(R.string.tech_))

        viewModel.technologyArticles.observe(viewLifecycleOwner,technologyObserver)
        viewModel.sportsArticles.observe(viewLifecycleOwner,politicsObserver)
        viewModel.musicArticles.observe(viewLifecycleOwner,sportsObserver)

        val technologySNapHelper= LinearSnapHelper()
        val technologyLinearLayoutManager = ZoomRecyclerLayout(activity?.applicationContext!!)
        technologyLinearLayoutManager.orientation= LinearLayoutManager.HORIZONTAL

        val snapHelper= LinearSnapHelper()
        val politicsLinearLayoutManager = ZoomRecyclerLayout(activity?.applicationContext!!)
        politicsLinearLayoutManager.orientation= LinearLayoutManager.HORIZONTAL

        val sportsSnapHelper= LinearSnapHelper()
        val sportsLinearLayoutManager = ZoomRecyclerLayout(activity?.applicationContext!!)
        sportsLinearLayoutManager.orientation= LinearLayoutManager.HORIZONTAL

        binding.politicsRv.layoutManager = politicsLinearLayoutManager
        snapHelper.attachToRecyclerView(binding.politicsRv)
       binding.politicsRv.isNestedScrollingEnabled=false


        binding.musicRv.layoutManager= sportsLinearLayoutManager
        technologySNapHelper.attachToRecyclerView(binding.musicRv)
        binding.musicRv.isNestedScrollingEnabled=false

        binding.techRv.layoutManager= technologyLinearLayoutManager
        sportsSnapHelper.attachToRecyclerView(binding.techRv)
        binding.techRv.isNestedScrollingEnabled=false

        binding.politicsRv.adapter = adapter
        binding.musicRv.adapter=musicAdapter
        binding.techRv.adapter=technologyAdapter

    }

    private fun handleError(message:String?){
        binding.loadingLayout.visibility=View.VISIBLE
        binding.progress.visibility=View.GONE
        binding.errorMessage.visibility=View.VISIBLE
        message?.let {
            binding.errorMessage.text=message


        }
    }
    private fun handleLoading(){
      binding.loadingLayout.visibility=View.VISIBLE
        binding.progress.visibility=View.VISIBLE
        binding.errorMessage.visibility=View.GONE
    }
    private fun showSportsData(data:List<ArticlePresentation>?){
        Timber.e("Size of the list: ${data?.size}")
        data?.let {
            musicAdapter.submitList(it)
        }
    }
    private fun showPoliticsData(data:List<ArticlePresentation>?){
        Timber.e("Size of the list: ${data?.size}")
        data?.let {
            adapter.submitList(it)
        }
    }
    private fun sceneTransitionIntent(article:ArticlePresentation){
        if(activity!=null){
            val intent=Intent(activity, DetailsActivity::class.java)
            intent.putExtra("ArticleId",article)
            val view:View= activity?.findViewById<ImageView>(R.id.newsImg)!!
            val imageViewPair= Pair.create(view,"imageBg")
            val activityOptions= ActivityOptionsCompat.makeSceneTransitionAnimation( activity!!,imageViewPair)
            ActivityCompat.startActivity(activity!!,intent,activityOptions.toBundle())
        }
    }

    private fun showTechnologyData(data:List<ArticlePresentation>?){
        Timber.e("Size of the list: ${data?.size}")
        data?.let {
            technologyAdapter.submitList(it)
        }
    }

}
