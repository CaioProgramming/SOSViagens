package com.intacta.sosviagens.view.activities

import androidx.appcompat.app.AppCompatActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import com.intacta.sosviagens.R

import com.intacta.sosviagens.Utils.PermissionRequests
import com.intacta.sosviagens.Utils.Preferences


/*Tela inicial de carregamento para apresentar
        aos usuários e verificar se são novos
        e já foi concecida a permissão de localização*/


class Splash : AppCompatActivity() {
    internal var requests: PermissionRequests? = null
    internal var activity: Activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val timer = object : CountDownTimer(5000, 100) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {

                val preferences = Preferences(activity)
                if (preferences.agreestate()) {
                    val i = Intent(activity, Home::class.java)
                    startActivity(i)
                    activity.finish()
                } else {
                    val i = Intent(activity, Intro_activity::class.java)
                    startActivity(i)
                    activity.finish()
                }
            }
        }.start()


    }

    companion object {
        val MY_PERMISSIONS_REQUEST_LOCATION = 99
        val MY_PERMISSIONS_CALL = 1
    }
}
