package com.intacta.sosviagens.model.Database

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.intacta.sosviagens.Utils.Alerts
import com.intacta.sosviagens.model.Beans.Road
import com.intacta.sosviagens.presenter.MainPresenter
import com.intacta.sosviagens.view.Adapter.RecyclerAdapter
import kotlinx.android.synthetic.main.concessions_layout.*

class SosDB(val mainPresenter: MainPresenter):ValueEventListener {



    override fun onCancelled(p0: DatabaseError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val concessionslist:ArrayList<Road> = ArrayList()
        for (d in dataSnapshot.children) {
            val r = d.getValue<Road>(Road::class.java)
            println("key ${d.key}")
            if (r != null) {
                r.id = d.key
                concessionslist.add(r)

            }
        }
        print("founded ${concessionslist.size} concesssions ")

        if (concessionslist.size == 0){
            load()
            return
        }

        val activity = mainPresenter.mainFragment.activity
        val llm = GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false)
        val adapter = RecyclerAdapter(activity!!, concessionslist)
        adapter.notifyDataSetChanged()
        mainPresenter.mainFragment.concessions.layoutManager = llm
        mainPresenter.mainFragment.concessions.adapter = adapter


    }


    companion object{
        fun build(mainPresenter: MainPresenter): SosDB{
            return SosDB(mainPresenter)
        }
    }


    fun load(){
        val databasereference: Query
        databasereference = FirebaseDatabase.getInstance().reference.child("rodovias").orderByChild("rodovia")
        databasereference.addListenerForSingleValueEvent(this)
    }

    fun pesquisa(pesquisa: String): Boolean{
        print("Buscando ${pesquisa.capitalize()}...")
        if (pesquisa.isBlank()){
            load()
            return true
        }

        if(pesquisa.contains("rua",true) || pesquisa.contains("avenida",true)){
            print("Não está em uma rodovia")
            return false

        }
        val databasereference: Query
        databasereference = FirebaseDatabase.getInstance().reference.child("rodovias").orderByChild("rodovia")
        databasereference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

                Log.println(Log.ERROR,"Firebase error", "Erro no banco de dados ${p0.message}")

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val concessionslist:ArrayList<Road> = ArrayList()
                for (d in dataSnapshot.children) {
                    val r = d.getValue<Road>(Road::class.java)
                    println("key ${d.key}")
                    if (r != null && r.rodovia!!.contains(pesquisa,true) || r!!.concessionaria!!.contains(pesquisa,true)) {
                        r.id = d.key
                        concessionslist.add(r)

                    }
                }
                print("founded ${concessionslist.size} concesssions ")

                if (concessionslist.size == 0){
                    var alerts = Alerts(mainPresenter.activity!!).noConcess(pesquisa)
                    return
                }

                val activity = mainPresenter.mainFragment.activity
                val llm = GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false)
                val adapter = RecyclerAdapter(activity!!, concessionslist)
                adapter.notifyDataSetChanged()
                mainPresenter.mainFragment.concessions.layoutManager = llm
                mainPresenter.mainFragment.concessions.adapter = adapter            }
        })

        return true

    }
}