package com.intacta.sosviagens.view.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import com.intacta.sosviagens.R
import com.intacta.sosviagens.presenter.NumbersPresenter

class NumbersFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
// Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,R.layout.fragment_numbers,container,false)


        binding.root.postDelayed({
            val numbersPresenter = NumbersPresenter(this)

         },100)
        return binding.root
    }

}
