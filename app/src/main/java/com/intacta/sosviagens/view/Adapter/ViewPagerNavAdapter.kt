package com.intacta.sosviagens.view.Adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.intacta.sosviagens.view.fragments.MainFragment
import com.intacta.sosviagens.view.fragments.NumbersFragment

class ViewPagerNavAdapter(private var manager: FragmentManager): FragmentPagerAdapter(manager) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> MainFragment()
            else -> NumbersFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Home"
            else -> return "Emergência"

        }
    }


}