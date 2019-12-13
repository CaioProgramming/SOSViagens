package com.intacta.sosviagens.model.Beans

import com.intacta.sosviagens.model.Database.ConcessDB
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject

open class Road {
    var rodovia: String? = null
    var concessionarias: ArrayList<Concession>? = null
    var id: String? = null


    fun findconcessions(): Completable{
        var concessDB = ConcessDB(this)
        var subject = CompletableSubject.create()
        return  subject.doOnSubscribe {
            concessDB.load()

        }
    }


}
