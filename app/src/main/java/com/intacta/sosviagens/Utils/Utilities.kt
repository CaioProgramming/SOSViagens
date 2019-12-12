package com.intacta.sosviagens.Utils

import android.app.Activity
import android.content.res.Configuration
import android.view.View
import androidx.core.view.ViewCompat
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject


object Utilities {
    val RC_SIGN_IN = 123
    var calltitle = "Números de emergência"
    fun Isnight(activity: Activity): Boolean {

        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
    var REQUEST_CHECK_SETTINGS = 100


    fun fadeIn(view: View): Completable {
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe {
            ViewCompat.animate(view)
                    .setDuration(1500)
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
}
