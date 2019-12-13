package com.intacta.sosviagens.model

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.location.*
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.maps.model.MapStyleOptions
import com.intacta.sosviagens.Utils.Alerts
import com.intacta.sosviagens.Utils.PermissionRequests
import com.intacta.sosviagens.Utils.Utilities
import com.intacta.sosviagens.Utils.Utilities.Isnight
import com.intacta.sosviagens.presenter.MainPresenter
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.searchview_layout.*
import java.io.IOException
import java.util.*


class MapConfigure(mapFragment: SupportMapFragment, val mainPresenter: MainPresenter):
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult>, OnMapReadyCallback{

    var mFusedLocationClient:FusedLocationProviderClient? = null
    var activity: Activity? = null
    var mMap: GoogleMap? = null
    private var  manager:LocationManager?= null
    private var locationRequest: LocationRequest? = null
    private var requests: PermissionRequests? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    init {
        activity = mainPresenter.activity
        mapFragment.getMapAsync(this)
        manager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        requests = PermissionRequests(activity!!)


        mGoogleApiClient = GoogleApiClient.Builder(activity!!)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build()
        mGoogleApiClient!!.connect()


    }
    override fun onLocationChanged(p0: Location?) {
        p0?.let { address(it) } //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        actuallocation() //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
       var Alerts = Alerts(activity!!).buildAlertMessageNoGps() //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResult(locationSettingsResult: LocationSettingsResult) {
        val status = locationSettingsResult.status
        when (status.statusCode) {
            LocationSettingsStatusCodes.SUCCESS -> {
                actuallocation()
            }

            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                //  GPS turned off, Show the user a dialog
                try {
                    status.startResolutionForResult(activity, 100)
                } catch (e: IntentSender.SendIntentException) {
                    Snacky.builder().setActivity(activity).setText("Erro ${e.message}").error().show()

                }

            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                Snacky.builder().setActivity(activity).setText("Servicos de localização indisponíveis").error().show()


            }
        }
    }


    private fun showDefaultLocation() {

        mMap!!.setMinZoomPreference(20f)
        val saoPaulo = LatLng(-23.533773, -46.625290)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(saoPaulo))
        //toolbar.setTitle(getString(R.string.default_location));


    }

    private fun address(location: Location) {
        //Método que retorna o nome da rua em que usário se encontra
        try {
            val geocoder = Geocoder(activity, Locale.getDefault())
            val addresses: List<Address>
            println("lat:" + location.latitude + "long:" + location.longitude)
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val address = addresses[0]
            println("Endereco $address")
            var street: String? = address.thoroughfare
            if (street == null) {
                street = address.featureName
            }
            println("Endereco atual ${street}" )





            if (mainPresenter.mainFragment.searchview != null){
                mainPresenter.mainFragment.searchview.queryHint = street

            }
            mainPresenter.search(street!!)
        } catch (e: IOException) {
            Snacky.builder().setActivity(activity).error().setText("Erro  ${e.message!!}")
        }

    }


    fun address(location: String){
        val geoCoder = Geocoder(activity, Locale.getDefault())
        try {
            val addresses = geoCoder.getFromLocationName(location, 5)
            if (addresses.size > 0) {
                val lat = addresses[0].latitude
                val lon = addresses[0].longitude
                Log.d("lat-long", "$lat$lon")
                val user = LatLng(lat, lon)
                val cameraPosition = CameraPosition.Builder()
                        .target(user) // Sets the center of the map to location user
                        .zoom(15f) // Sets the zoom
                        .build() // Creates a CameraPosition from the builder

                mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        } catch (e: IOException) {
            print("Endereço não encontrado ${e.message}")
            e.printStackTrace()
        }
    }



    fun actuallocation() {
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mainPresenter.activity!!)
        mFusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // GPS location can be null if GPS is switched off
                    if (location != null) {
                        address(location)
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("MapDemoActivity", "Error trying to get last GPS location ${e.message}")
                    e.printStackTrace()
                }

    }

    private fun configuremap(){

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        requests!!.checkLocationPermission()



        locationRequest = LocationRequest.create()
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest!!.setInterval(30 * 1000)
        locationRequest!!.setFastestInterval(5 * 1000)
        if (mFusedLocationClient!!.lastLocation.isComplete){
            address(mFusedLocationClient!!.lastLocation.result!!)

        }


        requests!!.checkCallPermission();
        if (requests!!.checkLocationPermission()) {
            manager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5000F, this)

        }


    }





    fun mapstyle(googleMap: GoogleMap) {
        Log.d("Map styling", "Is night mode? ${Utilities.Isnight(activity!!)}")
        if (Isnight(activity!!)) {
            Log.d("Map styling", "Try to set night mode map style")

            try {
                val success: Boolean = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                activity, com.intacta.sosviagens.R.raw.night_map))
                if (!success) {
                    Log.d("Map styling", "Style parsing failed")
                }

            } catch (e: NotFoundException) {
                Log.d("Map styling", "Map style not found ${e.message}")
            }
        }else{
            Log.d("Map styling", "Try to set day mode map style")

            try {
                val success: Boolean = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                activity, com.intacta.sosviagens.R.raw.day_map))
                if (!success) {
                    Log.d("Map styling", "Style parsing failed")
                }

            } catch (e: NotFoundException) {
                Log.d("Map styling", "Map style not found ${e.message}")
            }
        }
    }


    fun permitted():Boolean {
        print("can access location? ${(ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)}")
        return  (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
    override fun onMapReady(mMap: GoogleMap?) {
        this.mMap = mMap


        mapstyle(mMap!!)


        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.isTrafficEnabled = true
        this.mMap = mMap
        if (permitted()){
            actuallocation()
            mMap.isMyLocationEnabled = true
        }else{
            showDefaultLocation()
            requests!!.Location_permission()
        }
        configuremap()
//To change body of created functions use File | Settings | File Templates.
    }



    override fun onConnected(p0: Bundle?) {
        actuallocation()//To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        print("Conexão suspensa") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        print("Falha na conexão") //To change body of created functions use File | Settings | File Templates.
    }


}