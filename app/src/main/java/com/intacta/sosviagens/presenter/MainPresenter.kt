package com.intacta.sosviagens.presenter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
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
import com.intacta.sosviagens.model.contracts.presentercontracts
import com.intacta.sosviagens.model.Database.SosDB
import com.intacta.sosviagens.model.MapConfigure
import com.intacta.sosviagens.view.fragments.MainFragment
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import de.mateware.snacky.Snacky
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import kotlinx.android.synthetic.main.concessions_layout.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.IOException
import java.util.*

class MainPresenter(val mainFragment: MainFragment) : presentercontracts, SearchView.OnQueryTextListener{


    var activity:Activity? = null
    var mapConfigure: MapConfigure? = null
    var mMap:GoogleMap? = null
    private var locationRequest: LocationRequest? = null
    var  manager:LocationManager?= null

    override fun load() {
        val sosDB = SosDB.build(this).load()
    }



    init {
        activity = mainFragment.activity
        mainFragment.searchview.setOnQueryTextListener(this)
        fadeOut(mainFragment.title).andThen(fadeIn(mainFragment.swipeindicator)).andThen(fadeIn(mainFragment.searchview))
                .andThen(fadeIn(mainFragment.progressbar))
                .subscribe()

        mainFragment.title.visibility = View.GONE

        val mapFragment: SupportMapFragment = mainFragment.getChildFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
        mapConfigure = MapConfigure(mapFragment,this)


    }

    override fun search(search: String) {
        mapConfigure!!.address(search)
        val sosDB = SosDB(this)

        if (!sosDB.pesquisa(search)){
            fadeOut(mainFragment.progressbar).andThen(
            fadeIn(mainFragment.result))
                    .subscribe()
        }else{
            fadeOut(mainFragment.progressbar)
                    .andThen(fadeOut(mainFragment.result)
                            .andThen(fadeIn(mainFragment.concessions)))
                    .subscribe()
        }

    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { search(it) }
         return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (mainFragment.sliding_layout.panelState != SlidingUpPanelLayout.PanelState.EXPANDED){
            mainFragment.sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED

        }
        mainFragment.result.text = ""
        return false//To change body of created functions use File | Settings | File Templates.
    }



    fun fadeIn(view: View): Completable {
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe {
            ViewCompat.animate(view)
                    .setDuration(1500)
                    .alpha(1f)
                    .withEndAction {
                        animationSubject.onComplete()
                    }
        }
    }
    fun fadeOut(view: View): Completable {
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe {
            ViewCompat.animate(view)
                    .setDuration(1500)
                    .alpha(0f)
                    .withEndAction {
                        animationSubject.onComplete()
                        view.visibility = View.GONE
                    }
        }
    }













}