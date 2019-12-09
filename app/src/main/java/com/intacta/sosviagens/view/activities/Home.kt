package com.intacta.sosviagens.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.intacta.sosviagens.R
import com.intacta.sosviagens.Utils.Utilities.REQUEST_CHECK_SETTINGS
import com.intacta.sosviagens.view.Adapter.ViewPagerNavAdapter

import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {



    /*private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home ->{
                val mainfr = MainFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, mainfr, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true

            }
            R.id.navigation_call -> {
                    val numfr = NumbersFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, numfr, fragment.javaClass.getSimpleName())
                            .commit()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }*/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        /*navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home*/
        val adapter = ViewPagerNavAdapter(supportFragmentManager)
        view_pager.adapter = adapter
        pages.setupWithViewPager(view_pager)
        setupTabIcons()

    }
    private fun setupTabIcons(){
        pages.getTabAt(0)?.setIcon(R.drawable.ic_near_me_black_24dp)
        pages.getTabAt(1)?.setIcon(R.drawable.ic_emergency_hours)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }
    }




}
