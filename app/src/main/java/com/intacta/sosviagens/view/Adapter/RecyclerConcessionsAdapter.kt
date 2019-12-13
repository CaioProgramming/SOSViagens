package com.intacta.sosviagens.view.Adapter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.intacta.sosviagens.R
import com.intacta.sosviagens.Utils.Alerts
import com.intacta.sosviagens.databinding.ConcessionLayoutBinding
import com.intacta.sosviagens.model.Beans.Concession
import java.util.*


class RecyclerConcessionsAdapter(private val mActivity: Activity, private val concesslist: ArrayList<Concession>) : RecyclerView.Adapter<RecyclerConcessionsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(mActivity.baseContext)
        view = mInflater.inflate(R.layout.concession_layout, parent, false)

        val concessionlistbinding = DataBindingUtil.inflate<ConcessionLayoutBinding>(LayoutInflater.from(parent.context),
                R.layout.concession_layout, parent, false)

        return MyViewHolder(concessionlistbinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(concesslist[position])




    }


    override fun getItemCount(): Int {
       return concesslist.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(val binding: ConcessionLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
          fun bind(concession: Concession){
              binding.setConcession(concession)
              binding.executePendingBindings()
          }
    }
}
