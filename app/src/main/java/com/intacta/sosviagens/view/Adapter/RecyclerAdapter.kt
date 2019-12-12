package com.intacta.sosviagens.view.Adapter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView

import com.intacta.sosviagens.model.Beans.Road
import com.intacta.sosviagens.R

import java.util.ArrayList
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.intacta.sosviagens.Utils.Alerts
import io.rmiri.skeleton.SkeletonGroup


class RecyclerAdapter(private val mActivity: Activity, private val roadlist: ArrayList<Road>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(mActivity.baseContext)
        view = mInflater.inflate(R.layout.roadsuggest, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.MyViewHolder, position: Int) {
        val r = roadlist[position]
        val timer = object : CountDownTimer(500, 100) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                holder.skeletonGroup.setShowSkeleton(false)
                holder.skeletonGroup.finishAnimation()
            }
        }.start()
        if (r.rodovia == "Concessionárias registradas") {
            holder.concess.visibility = View.GONE
            holder.road.textSize = 24f
            holder.layout.setBackgroundColor(Color.TRANSPARENT)
        } else {
            val animation = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in)
            holder.road.text = r.rodovia
            holder.concess.setText(String.format("%s(%s)", r.concessionaria, r.telefone))
            holder.layout.startAnimation(animation)
            holder.layout.setOnClickListener { ligacao(roadlist[position]) }
        }


    }

    private fun ligacao(road: Road) {

       // println("ligando para " + road.rodovia + road.concessionaria + road.id)
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + Uri.encode(road.telefone))
        callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            val dialntent = Intent(Intent.ACTION_DIAL)
            dialntent.data = Uri.parse("tel:" + Uri.encode(road.telefone))
            dialntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mActivity.startActivity(dialntent)
            return
        }
        val a = Alerts(mActivity).CommentAlert(road)
        mActivity.startActivity(callIntent)


    }

    override fun getItemCount(): Int {
       return roadlist.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var road: TextView
        var concess: TextView

        var layout: RelativeLayout
        var skeletonGroup: SkeletonGroup

        init {
            road = itemView.findViewById(R.id.road)
            concess = itemView.findViewById(R.id.concess)
            layout = itemView.findViewById(R.id.layout)
            skeletonGroup = itemView.findViewById(R.id.skeleton_group)

        }
    }
}
