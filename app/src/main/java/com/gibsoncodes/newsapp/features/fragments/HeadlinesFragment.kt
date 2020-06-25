package com.gibsoncodes.newsapp.features.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gibsoncodes.newsapp.R
import com.gibsoncodes.newsapp.adapters.HeadlinesSourcesItemAdapter
import com.gibsoncodes.newsapp.common.RecyclerViewListener
import com.gibsoncodes.newsapp.common.Resource
import com.gibsoncodes.newsapp.common.Status
import com.gibsoncodes.newsapp.databinding.FragmentHeadlinesBinding
import com.gibsoncodes.newsapp.features.activities.DetailsActivity
import com.gibsoncodes.newsapp.model.ArticlePresentation
import com.gibsoncodes.newsapp.viewmodels.HeadlinesBySourceViewModel
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HeadlinesFragment : Fragment() {
    private  lateinit var binding:FragmentHeadlinesBinding
    private val viewModel:HeadlinesBySourceViewModel by viewModel()
    private val adapter:HeadlinesSourcesItemAdapter by lazy{ HeadlinesSourcesItemAdapter(activity?.applicationContext!!,object: RecyclerViewListener{
        override fun onClick(position: Int) {
            val article = adapter.getClickedArticle(position)
            if(activity!=null){
                val intent=Intent(activity,DetailsActivity::class.java)
                intent.putExtra("ArticleId",article)
               val view:View= activity?.findViewById<ImageView>(R.id.headlinesImg)!!
                val imageViewPair= Pair.create(view,"imageBg")
                 val activityOptions= ActivityOptionsCompat.makeSceneTransitionAnimation( activity!!,imageViewPair)
               ActivityCompat.startActivity(activity!!,intent,activityOptions.toBundle())
            }
        }
    })}

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_headlines,container,false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val observer= Observer<Resource<List<ArticlePresentation>>>{

            when(it.status){
                Status.SUCCESS-> updateData(it.data)
                Status.ERROR-> showError(it.message)
                Status.LOADING-> showLoading()
            }
        }
        binding.progressHeadlines.setAnimation(activity?.resources?.openRawResource(R.raw.newspaper_spinner),null)
        viewModel.headlinesBySourcesList.observe(viewLifecycleOwner,observer)
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
        binding.headlinesRv.layoutManager= linearLayoutManager
        binding.headlinesRv.itemAnimator= SlideInUpAnimator(OvershootInterpolator(1f))
        binding.headlinesRv.adapter= adapter


    }

    private fun updateData(data:List<ArticlePresentation>?){
        Timber.e("Size of the list: ${data?.size}")
        data?.let {
            binding.spinner.visibility=View.GONE
            adapter.submitList(it)
        }
    }
    private fun showError(message:String?){
        binding.spinner.visibility=View.VISIBLE
        binding.progressHeadlines.visibility=View.GONE
        binding.errorMessage.visibility=View.VISIBLE
        message?.let {
            binding.errorMessage.text=message
        }

    }
    private fun showLoading(){
        binding.spinner.visibility=View.VISIBLE
        binding.progressHeadlines.visibility=View.VISIBLE
        binding.errorMessage.visibility=View.GONE
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HeadlinesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HeadlinesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
