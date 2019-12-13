package com.intacta.sosviagens.model.Database

import android.app.Activity
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.intacta.sosviagens.R
import com.intacta.sosviagens.model.Beans.Number
import com.intacta.sosviagens.model.Beans.Section
import com.intacta.sosviagens.presenter.NumbersPresenter
import com.intacta.sosviagens.view.Adapter.RecyclerSectionsAdapter
import kotlinx.android.synthetic.main.searchview_layout.*


class NumbersDB(val numbersPresenter: NumbersPresenter):ValueEventListener{
    override fun onCancelled(p0: DatabaseError) {

    }


    var activity:Activity?= null

    init {
        activity = numbersPresenter.numbersFragment.activity
    }


    override fun onDataChange(snapshot: DataSnapshot) {
        val emergencylist:ArrayList<Number> = ArrayList()
        val assunrancelist:ArrayList<Number> = ArrayList()
        val governmentlist:ArrayList<Number> = ArrayList()
        for (d in snapshot.children){
            val n = d.getValue<Number>(Number::class.java)
            if (n != null){
                when {
                    n.tipo.equals("Emergência") -> {
                        Log.println(Log.INFO,"Numbers", "added ${n.ident} to emergency list")
                        emergencylist.add(n)

                    }
                    n.tipo.equals("Seguradora") -> {
                        Log.println(Log.INFO,"Numbers", "added ${n.ident} to assurance list")

                        assunrancelist.add(n)

                    }
                    else -> {
                        Log.println(Log.INFO,"Numbers", "added ${n.ident} to government list")

                        governmentlist.add(n)
                    }
                }
            }
        }
        val sections:ArrayList<Section> = ArrayList()

        sections.add(Section("Números públicos",emergencylist))
        sections.add(Section("Seguradoras",assunrancelist))
        sections.add(Section("Órgãos reguladores",governmentlist))




        val activity = numbersPresenter.numbersFragment.requireActivity()
        val sectionsAdapter = RecyclerSectionsAdapter(activity, sections)
        val llm = GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false)
        sectionsAdapter.notifyDataSetChanged()
        numbersPresenter.recyclerView.adapter = sectionsAdapter
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

                val emergencylist:ArrayList<Number> = ArrayList()
                val assunrancelist:ArrayList<Number> = ArrayList()
                val governmentlist:ArrayList<Number> = ArrayList()
                for (d in snapshot.children){
                    val n = d.getValue<Number>(Number::class.java)
                    if (n != null && n.ident!!.contains(pesquisa,true) ||
                            n!!.telefone!!.contains(pesquisa,true) ||
                            n.tipo!!.contains(pesquisa,true)){
                        when {
                            n.tipo.equals("Emergência") -> {
                                Log.println(Log.INFO,"Numbers", "added ${n.ident} to emergency list")
                                emergencylist.add(n)

                            }
                            n.tipo.equals("Seguradora") -> {
                                Log.println(Log.INFO,"Numbers", "added ${n.ident} to assurance list")

                                assunrancelist.add(n)

                            }
                            else -> {
                                Log.println(Log.INFO,"Numbers", "added ${n.ident} to government list")

                                governmentlist.add(n)
                            }
                        }

                    }
                }
                val sections:ArrayList<Section> = ArrayList()


                sections.add(Section("Números publicos",emergencylist))
                sections.add(Section("Seguradoras",assunrancelist))
                sections.add(Section("Órgãos reguladores",governmentlist))

                val activity = numbersPresenter.numbersFragment.requireActivity()
                val numberadapter = RecyclerSectionsAdapter(activity, sections)
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