package com.intacta.sosviagens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.intacta.sosviagens.Utils.PermissionRequests;
import com.intacta.sosviagens.Utils.Preferences;



/*Tela inicial de carregamento para apresentar
        aos usuários e verificar se são novos
        e já foi concecida a permissão de localização*/


public class Splash extends AppCompatActivity {
    PermissionRequests requests;
    Activity activity = this;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);





        CountDownTimer timer = new CountDownTimer(5000,100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                Preferences preferences = new Preferences(activity);
                if (preferences.agreestate()) {
                    Intent i = new Intent(activity,Home.class);
                    startActivity(i);
                    activity.finish();
                } else {
                    Intent i = new Intent(activity,Intro_activity.class);
                    startActivity(i);
                    activity.finish();
                }
            }
        }.start();




    }
}
