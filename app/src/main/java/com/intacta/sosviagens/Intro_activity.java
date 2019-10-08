package com.intacta.sosviagens;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

 import com.google.android.material.tabs.TabLayout;
import com.intacta.sosviagens.Adapter.ViewPagerAdapter;
import com.intacta.sosviagens.Utils.Preferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import de.mateware.snacky.Snacky;

public class Intro_activity extends AppCompatActivity {

    private androidx.viewpager.widget.ViewPager viewpager;
    private com.google.android.material.tabs.TabLayout tabs;
    private android.widget.Button skip;
    private Dialog m_dialog;

    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_activity);
        this.skip = findViewById(R.id.skip);
        this.tabs = findViewById(R.id.tabs);
        this.viewpager = findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewpager.setAdapter(adapter);
        tabs.setupWithViewPager(viewpager);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Preferences preferences = new Preferences(activity);
                if (preferences.agreestate()){
                    final Intent i = new Intent(activity, Home.class);
                    activity.startActivity(i);
                }else{
                   AlertDialog.Builder dialog = new AlertDialog.Builder(activity).setTitle("Atenção")
                           .setMessage("Você concorda com nossos termos de privacidade?")
                           .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   preferences.setAgree(true);
                                   start_app();
                               }
                           });
                   dialog.show();
                }
            }
        });


    }

    private void start_app() {
        Preferences preferences = new Preferences(activity);
        preferences.setAgree(true);
        final Intent i = new Intent(activity, Home.class);
        activity.startActivity(i);
    }
}
