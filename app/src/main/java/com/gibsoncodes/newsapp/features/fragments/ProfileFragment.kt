package com.gibsoncodes.newsapp.features.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.gibsoncodes.newsapp.R
import com.gibsoncodes.newsapp.databinding.FragmentProfileBinding
import com.gibsoncodes.newsapp.viewmodels.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {
private val viewModel:ProfileViewModel by viewModel()
    private lateinit var binding:FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.memberName.observe(viewLifecycleOwner, Observer{
          name->name.let {
            if (name != null) {
                if (name.isEmpty()||name.isBlank()){
                       binding.memberName.text="Anonymous"
                    binding.bannerNamePr.text="A"
                }else{
                    binding.memberName.text=name
                    binding.bannerNamePr.text=name[0].toString()
                }
            }else{
                binding.memberName.text="Anonymous"
                binding.bannerNamePr.text="A"
            }
        }
        })

        viewModel.memberEmail.observe(viewLifecycleOwner, Observer {
           email->email.let{
            if (email!=null){
                if (email.isEmpty()|| email.isBlank()){
                    binding.memberEmail.text="anonymous@gmail.com"

                }else{
                    binding.memberEmail.text=email

                }
            }else{
                binding.memberEmail.text="anonymous@gmail.com"
            }
        }
        })
    }

}
