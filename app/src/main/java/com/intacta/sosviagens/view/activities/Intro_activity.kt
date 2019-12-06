package com.intacta.sosviagens.view.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle

import com.intacta.sosviagens.view.Adapter.ViewPagerAdapter
import com.intacta.sosviagens.Utils.Preferences

import androidx.appcompat.app.AppCompatActivity
import com.intacta.sosviagens.R

class Intro_activity : AppCompatActivity() {

    private var viewpager: androidx.viewpager.widget.ViewPager? = null
    private var tabs: com.google.android.material.tabs.TabLayout? = null
    private var skip: android.widget.Button? = null
    private val m_dialog: Dialog? = null

    internal var activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_activity)
        this.skip = findViewById(R.id.skip)
        this.tabs = findViewById(R.id.tabs)
        this.viewpager = findViewById(R.id.viewpager)

        val adapter = ViewPagerAdapter(this)
        viewpager!!.adapter = adapter
        tabs!!.setupWithViewPager(viewpager)
        skip!!.setOnClickListener {
            val preferences = Preferences(activity)
            if (preferences.agreestate()) {
                val i = Intent(activity, Home::class.java)
                activity.startActivity(i)
            } else {
                val dialog = AlertDialog.Builder(activity).setTitle("Atenção")
                        .setMessage("Você concorda com nossos termos de privacidade?")
                        .setPositiveButton("Sim") { dialog, which ->
                            preferences.setAgree(true)
                            start_app()
                        }
                dialog.show()
            }
        }


    }

    private fun start_app() {
        val preferences = Preferences(activity)
        preferences.setAgree(true)
        val i = Intent(activity, Home::class.java)
        activity.startActivity(i)
    }
}
