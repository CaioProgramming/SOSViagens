package com.intacta.sosviagens.view.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView

import com.intacta.sosviagens.R

import java.util.ArrayList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intacta.sosviagens.Utils.Utilities
import com.intacta.sosviagens.model.Beans.Section

class RecyclerSectionsAdapter(private val activity: Activity, private val sections: ArrayList<Section>) : RecyclerView.Adapter<RecyclerSectionsAdapter.MyViewHolder>() {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(activity.baseContext)
        view = mInflater.inflate(R.layout.sections_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val s = sections[position]
        holder.title.text = s.title



        val numberadapter = RecyclerNumbersAdapter(activity, s.items.sortedWith(compareBy({ it.ident})))
        val llm = GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false)
        numberadapter.notifyDataSetChanged()
        holder.items.adapter = numberadapter
        holder.items.layoutManager = llm

        Utilities.fadeIn(holder.card).subscribe()





    }


    override fun getItemCount(): Int {
        return sections.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var items: RecyclerView
        var card: LinearLayout

        init {
            title = itemView.findViewById(R.id.titlesection)
            items = itemView.findViewById(R.id.numbers)
            card = itemView.findViewById(R.id.card)

        }
    }
}
