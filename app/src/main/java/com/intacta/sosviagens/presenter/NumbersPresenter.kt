package com.intacta.sosviagens.presenter

 import android.view.View
 import androidx.appcompat.widget.SearchView
 import androidx.core.view.ViewCompat
 import androidx.recyclerview.widget.RecyclerView
import com.intacta.sosviagens.model.contracts.presentercontracts
import com.intacta.sosviagens.model.Database.NumbersDB
import com.intacta.sosviagens.view.fragments.NumbersFragment
 import io.reactivex.Completable
 import io.reactivex.subjects.CompletableSubject
 import kotlinx.android.synthetic.main.concessions_layout.*


class NumbersPresenter(val numbersFragment: NumbersFragment):  presentercontracts, SearchView.OnQueryTextListener{


    val recyclerView: RecyclerView = numbersFragment.concessions


    init {
        numbersFragment.searchview.queryHint = "Pesquise números de emergência e serviços de socorro"
        numbersFragment.title.text = "Números de emergência"
        numbersFragment.searchview.setOnQueryTextListener(this)


        fadeOut(numbersFragment.swipeindicator).andThen(fadeIn(numbersFragment.title)
                .andThen(fadeIn(numbersFragment.searchview))).andThen(fadeOut(numbersFragment.result)).andThen(fadeOut(numbersFragment.progressbar)).subscribe()

        load()

    }




    fun fadeIn(view: View): Completable {
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe {
            ViewCompat.animate(view)
                    .setDuration(500)
                    .alpha(1f)
                    .withEndAction {
                        animationSubject.onComplete()
                    }
        }
    }
    fun fadeOut(view: View): Completable {
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe {
            ViewCompat.animate(view)
                    .setDuration(1500)
                    .alpha(0f)
                    .withEndAction {
                        animationSubject.onComplete()
                        view.visibility = View.GONE
                    }
        }
    }




    override fun load() {

        val numbersDB = NumbersDB.build(this).load()
        fadeOut(numbersFragment.progressbar).andThen(fadeIn(numbersFragment.concessions)).subscribe()


    }

    override fun search(search: String) {
        val numbersDB = NumbersDB.build(this).search(search)
        //To change body of created functions use File | Settings | File Templates.
    }



    override fun onQueryTextSubmit(query: String?): Boolean {
        search(query!!)

        return false//To change body of created functions use File | Settings | File Templates.
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        fadeIn( numbersFragment.progressbar)
        return false//To change body of created functions use File | Settings | File Templates.
    }




}
