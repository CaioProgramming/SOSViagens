package com.intacta.sosviagens.view.Adapter



import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.intacta.sosviagens.R
import com.intacta.sosviagens.model.Beans.Number
import io.rmiri.skeleton.SkeletonGroup

class RecyclerNumbersAdapter(private val mActivity: Activity, private val numberlist: List<Number>) : RecyclerView.Adapter<RecyclerNumbersAdapter.MyViewHolder>() {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(mActivity.baseContext)
        view = mInflater.inflate(R.layout.roadsuggest, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val timer = object : CountDownTimer(500, 100) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                holder.skeletonGroup.setShowSkeleton(false)
                holder.skeletonGroup.finishAnimation()
            }
        }.start()
        val n = numberlist[position]

           val animation = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in)
            holder.road.text = n.ident
            holder.concess.text = n.telefone
            println(n.tipo)

            holder.layout.setOnClickListener { ligacao(n.telefone!!) }
            holder.layout.startAnimation(animation)


        if (n.tipo == "Seguradora") {
            holder.type.setImageDrawable(mActivity.getDrawable(R.drawable.ic_shield))
        } else if (n.tipo == "Órgão regulador") {
            holder.type.setImageDrawable(mActivity.getDrawable(R.drawable.ic_university_with_a_flag))
        } else {
            holder.type.setImageDrawable(mActivity.getDrawable(R.drawable.ic_error_black_24dp))

        }


    }

    private fun ligacao(number: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + Uri.encode(number.trim { it <= ' ' }))
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
            dialntent.data = Uri.parse("tel:" + Uri.encode(number.trim { it <= ' ' }))
            dialntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mActivity.startActivity(dialntent)
            return
        }
        mActivity.startActivity(callIntent)
    }

    override fun getItemCount(): Int {
        return if (numberlist.size == 0) {
            0

        } else {
            numberlist.size
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var road: TextView
        var concess: TextView
        var type: ImageView
        var layout: RelativeLayout
        var skeletonGroup: SkeletonGroup

        init {
            road = itemView.findViewById(R.id.road)
            concess = itemView.findViewById(R.id.concess)
            type = itemView.findViewById(R.id.type)
            layout = itemView.findViewById(R.id.layout)
            skeletonGroup = itemView.findViewById(R.id.skeleton_group)

        }
    }
}