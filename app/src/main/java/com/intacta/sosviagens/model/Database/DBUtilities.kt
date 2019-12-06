package com.intacta.sosviagens.model.Database

import android.app.Activity

import com.google.firebase.database.FirebaseDatabase
import com.intacta.sosviagens.model.Beans.Comment
import com.intacta.sosviagens.model.Beans.Road
import com.intacta.sosviagens.model.Beans.Suggest

import java.util.Calendar
import de.mateware.snacky.Snacky

class DBUtilities(private val activity: Activity) {

    fun SendComment(comment: Comment) {
        val commentsReference = FirebaseDatabase.getInstance().getReference("comments")
        commentsReference.push().setValue(comment).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Snacky.builder().setActivity(activity).success().setText("Comentário enviado com sucesso!").show()
            }
        }

    }

    fun reportcall(road: Road) {
        val datenow = Calendar.getInstance().time
        FirebaseDatabase.getInstance().getReference("rodovias").child(road.id!!)
                .child("calls").push().setValue(datenow.toString())
    }


    fun SendSuggest(suggest: Suggest) {

        if (!suggest.rodovia!!.contains("Rodovia") ||!suggest.rodovia!!.contains("rodovia")){

            return
        }


        val suggestions = FirebaseDatabase.getInstance().getReference("suggestion")
        suggestions.push().setValue(suggest).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Snacky.builder().setActivity(activity).success().setText("Sugestão enviada com sucesso!").show()
            }
        }
    }
}
