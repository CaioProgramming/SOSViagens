package com.intacta.sosviagens.view.Adapter

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView

import com.intacta.sosviagens.view.activities.Home
import com.intacta.sosviagens.R
import com.intacta.sosviagens.Utils.PermissionRequests
import com.intacta.sosviagens.Utils.Preferences
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter(private val activity: Activity) : PagerAdapter() {

    private var m_dialog: Dialog? = null

    private val titles = arrayOf("Bem-vindo", "Antes de começar...", "Tudo pronto")
    private val descriptions = arrayOf("Bem-vindo ao SOSRodovias, " + "Estamos aqui para tornar sua viagem mais segura!", "Asseguramos sua privacidade, todos os dados utilizados no app serão apenas para ajudá-lo em seu resgate, não se preocupe", "Ao prosseguir você estará concordando com nossas políticas de uso e privacidade")


    override fun getCount(): Int {
        return titles.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fadein = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        val layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.pager_layout, container, false)
        val description = view.findViewById<TextView>(R.id.description)
        val title = view.findViewById<TextView>(R.id.title)
        val card = view.findViewById<CardView>(R.id.card)
        val background = view.findViewById<RelativeLayout>(R.id.background)
        val privacypolicy = view.findViewById<TextView>(R.id.privacy_policy)


        title.text = titles[position]
        description.text = descriptions[position]
        val `in` = AnimationUtils.loadAnimation(activity, R.anim.slide_in_top)

        privacypolicy.setOnClickListener {
            m_dialog = Dialog(activity, R.style.Dialog_No_Border)
            val m_inflater = LayoutInflater.from(activity)
            val m_view = m_inflater.inflate(R.layout.dialog, null)
            m_dialog!!.setContentView(m_view)
            val agreebutton = m_view.findViewById<Button>(R.id.agreebutton)
            agreebutton.setOnClickListener {
                m_dialog!!.dismiss()
                start_app()
            }
            m_dialog!!.show()
            m_view.startAnimation(`in`)
        }






        container.addView(view)
        title.startAnimation(`in`)
        card.startAnimation(fadein)
        return view

    }

    private fun start_app() {
        val ALL_PERMISSIONS = 101

        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE)

        ActivityCompat.requestPermissions(activity, permissions, ALL_PERMISSIONS)
        val preferences = Preferences(activity)
        preferences.setAgree(true)
        val i = Intent(activity, Home::class.java)
        if (PermissionRequests.hasPermissions(activity, *permissions)) {
            activity.startActivity(i)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}
