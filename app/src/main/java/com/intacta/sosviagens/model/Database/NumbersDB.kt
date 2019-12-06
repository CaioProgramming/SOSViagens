package com.intacta.sosviagens.model.Database

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.intacta.sosviagens.model.Beans.Number
import com.intacta.sosviagens.presenter.NumbersPresenter
import com.intacta.sosviagens.view.Adapter.RecyclerNumbersAdapter
import kotlinx.android.synthetic.main.concessions_layout.*


class NumbersDB(val numbersPresenter: NumbersPresenter):ValueEventListener{
    override fun onCancelled(p0: DatabaseError) {

    }




    override fun onDataChange(snapshot: DataSnapshot) {
        val numberslit:ArrayList<Number> = ArrayList()
        for (d in snapshot.children){
            val n = d.getValue<Number>(Number::class.java)
            if (n != null){
                numberslit.add(n)
            }
        }
        val activity = numbersPresenter.numbersFragment.requireActivity()
        val numberadapter = RecyclerNumbersAdapter(activity, numberslit)
        val llm = GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false)
        numberadapter.notifyDataSetChanged()
        numbersPresenter.recyclerView.adapter = numberadapter
        numbersPresenter.recyclerView.layoutManager = llm
        numbersPresenter.numbersFragment.progressbar.visibility = View.GONE



    }


    fun load(){
        val databasereference: Query = FirebaseDatabase.getInstance().reference.child("numbers").orderByChild("tipo")
        databasereference.keepSynced(true)
        databasereference.addListenerForSingleValueEvent(this)
    }

    fun search(pesquisa: String){
        val databasereference: Query = FirebaseDatabase.getInstance().reference.child("numbers").orderByChild("tipo")
        databasereference.keepSynced(true)
        databasereference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val numberslit:ArrayList<Number> = ArrayList()
                for (d in snapshot.children){
                    val n = d.getValue<Number>(Number::class.java)
                    if (n != null && n.ident!!.contains(pesquisa,true) ||
                            n!!.telefone!!.contains(pesquisa,true) ||
                            n.tipo!!.contains(pesquisa,true)){
                        numberslit.add(n)
                    }
                }
                val activity = numbersPresenter.numbersFragment.requireActivity()
                val numberadapter = RecyclerNumbersAdapter(activity, numberslit)
                val llm = GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false)
                numberadapter.notifyDataSetChanged()
                numbersPresenter.recyclerView.adapter = numberadapter
                numbersPresenter.recyclerView.layoutManager = llm

            }
        })
    }

    companion object {
        fun build(numbersPresenter: NumbersPresenter): NumbersDB{

            return NumbersDB(numbersPresenter)
        }
    }

}