package com.intacta.sosviagens.presenter


import android.app.Activity
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import com.google.android.gms.maps.SupportMapFragment
import com.intacta.sosviagens.R
import com.intacta.sosviagens.Utils.Utilities.fadeIn
import com.intacta.sosviagens.Utils.Utilities.fadeOut
import com.intacta.sosviagens.model.contracts.presentercontracts
import com.intacta.sosviagens.model.Database.SosDB
import com.intacta.sosviagens.model.MapConfigure
import com.intacta.sosviagens.view.fragments.MainFragment
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.searchview_layout.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainPresenter(val mainFragment: MainFragment) : presentercontracts, SearchView.OnQueryTextListener{


    var activity:Activity? = null
    private var mapConfigure: MapConfigure? = null

    override fun load() {
      SosDB.build(this).load()
    }



    init {
        activity = mainFragment.activity
        mainFragment.searchview.setOnQueryTextListener(this)
        fadeOut(mainFragment.title).andThen(fadeIn(mainFragment.swipeindicator)).andThen(fadeIn(mainFragment.searchview))
                .andThen(fadeIn(mainFragment.progressbar))
                .subscribe()

        mainFragment.title.visibility = View.GONE

        val mapFragment: SupportMapFragment = mainFragment.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapConfigure = MapConfigure(mapFragment,this)


    }

    override fun search(search: String) {
        mapConfigure!!.address(search)
        val sosDB = SosDB(this)

        if (!sosDB.pesquisa(search)){
             if (!mainFragment.isAdded){
                 Log.println(Log.INFO,"roads","fragment is not added")
                 return
             }
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
        return false
    }
}
