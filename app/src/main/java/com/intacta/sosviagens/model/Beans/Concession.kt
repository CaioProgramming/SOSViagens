package com.intacta.sosviagens.model.Beans

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import com.intacta.sosviagens.Utils.Alerts

class Concession(var key: String, var name: String,val number:String,var road: Road){
    fun ligacao(activity: Activity) {
        // println("ligando para " + road.rodovia + road.concessionaria + road.id)
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + Uri.encode(number))
        callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            val dialntent = Intent(Intent.ACTION_DIAL)
            dialntent.data = Uri.parse("tel:" + Uri.encode(number))
            dialntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(dialntent)
            return
        }
        val a = Alerts(activity).CommentAlert(road.id!!)
        activity.startActivity(callIntent)


    }

}

