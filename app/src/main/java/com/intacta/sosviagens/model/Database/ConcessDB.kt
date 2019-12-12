package com.intacta.sosviagens.model.Database

import com.google.firebase.database.*
import com.intacta.sosviagens.model.Beans.Concession
import com.intacta.sosviagens.model.Beans.Road

class ConcessDB(val road: Road){
   /* override fun onCancelled(p0: DatabaseError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        for (d in snapshot.children){
           val concession = d.getValue<Concession>(Concession::class.java)
            if (concession != null){
                concession.key = d.key!!
                road.concessions!!.add(concession)

            }
        }
        print("${road.concessions!!.size} Concessions in ${road.rodovia} ")

    }

    fun load(){
        val concessreference = FirebaseDatabase.getInstance()
                .reference.child("rodovias-test").child(road.id!!)
                .child("concessionarias").addValueEventListener(this)


    }
*/



}