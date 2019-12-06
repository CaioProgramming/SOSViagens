package com.intacta.sosviagens.view.Adapter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView

import com.intacta.sosviagens.model.Beans.Road
import com.intacta.sosviagens.model.Database.DBUtilities
import com.intacta.sosviagens.R
import com.intacta.sosviagens.Utils.Alerts

import java.util.ArrayList


class RecyclerMoreThenOneAdapter(private val mActivity: Activity, private val roadlist: ArrayList<Road>) : RecyclerView.Adapter<RecyclerMoreThenOneAdapter.MyViewHolder>() {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerMoreThenOneAdapter.MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(mActivity)
        view = mInflater.inflate(R.layout.roadcardlayout, parent, false)

        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerMoreThenOneAdapter.MyViewHolder, position: Int) {
        val r = roadlist[position]
        val animation = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in)

        holder.concess.text = r.concessionaria + "(" + r.telefone + ")"
        holder.layout.startAnimation(animation)
        holder.layout.setOnClickListener { ligacao(roadlist[position]) }


    }

    private fun ligacao(road: Road) {
        val dbUtilities = DBUtilities(mActivity)
        dbUtilities.reportcall(road)
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + Uri.encode(road.telefone))
        callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            val dialntent = Intent(Intent.ACTION_DIAL)
            dialntent.data = Uri.parse("tel:" + Uri.encode(road.telefone))
            dialntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mActivity.startActivity(dialntent)
            return
        }
        var a = Alerts(mActivity).CommentAlert(road)
        mActivity.startActivity(callIntent)

    }

    override fun getItemCount(): Int {
        return roadlist.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var concess: TextView
        var layout: LinearLayout

        init {
            concess = itemView.findViewById(R.id.concess)
            layout = itemView.findViewById(R.id.layout)

        }
    }
}
