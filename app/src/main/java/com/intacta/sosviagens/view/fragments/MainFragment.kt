package com.intacta.sosviagens.view.fragments


import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

import com.intacta.sosviagens.R
import com.intacta.sosviagens.Utils.PermissionRequests
import com.intacta.sosviagens.presenter.MainPresenter
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.searchview_layout.*
import kotlinx.android.synthetic.main.searchview_layout.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,R.layout.fragment_main,container,false)


        binding.root.concessions.postDelayed({
            var mainPresenter:MainPresenter = MainPresenter(this)

        },1000)
        return binding.root
    }





    var mMap:GoogleMap? = null














}
