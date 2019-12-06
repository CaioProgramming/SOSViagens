package com.intacta.sosviagens.Utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import com.intacta.sosviagens.view.activities.Splash.Companion.MY_PERMISSIONS_CALL
import com.intacta.sosviagens.view.activities.Splash.Companion.MY_PERMISSIONS_REQUEST_LOCATION


class PermissionRequests(private val activity: Activity) {

    fun Location_permission(): Boolean {
        if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            checkLocationPermission()
            return false
        }
    }

    fun Call_permission(): Boolean {
        if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            checkCallPermission()
            return false
        }
    }

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(activity)
                        .setTitle("Localização necessária")
                        .setMessage("É necessário que você permita o acesso a sua localização")
                        .setPositiveButton("Ok") { dialogInterface, i ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(activity,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    MY_PERMISSIONS_REQUEST_LOCATION)
                        }
                        .create()
                        .show()


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        } else {
            return true
        }
    }

    fun checkCallPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(activity)
                        .setTitle("Permissão para ligações")
                        .setMessage("É necessário que você permita que realizemos ligações para ajudá-lo")
                        .setPositiveButton("Ok") { dialogInterface, i ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(activity,
                                    arrayOf(Manifest.permission.CALL_PHONE),
                                    MY_PERMISSIONS_CALL)
                        }
                        .create()

                        .show()


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        MY_PERMISSIONS_CALL)
            }
            return false
        } else {
            return true
        }
    }

    companion object {


        fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
            if (context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
            }
            return true
        }
    }


}
