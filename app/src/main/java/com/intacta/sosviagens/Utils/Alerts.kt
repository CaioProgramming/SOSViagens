package com.intacta.sosviagens.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.provider.Settings
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog

import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.intacta.sosviagens.model.Beans.Comment
import com.intacta.sosviagens.model.Beans.Road
import com.intacta.sosviagens.model.Beans.Suggest
import com.intacta.sosviagens.model.Database.DBUtilities
import com.intacta.sosviagens.R

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Objects

import hyogeun.github.com.colorratingbarlib.ColorRatingBar

import com.intacta.sosviagens.Utils.Utilities.RC_SIGN_IN
import de.mateware.snacky.Snacky

class Alerts(private val activity: Activity){



    private val dia: String
        get() {
            val datenow = Calendar.getInstance().time
            @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat("dd/MM/yyyy")
            return df.format(datenow)
        }

    private fun checkuser(): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return user != null
    }


    fun noConcess(address: String) {
        val reference = FirebaseDatabase.getInstance().getReference("suggestion")
                .orderByChild("rodovia").equalTo(address)

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists() && !address.contains("Rua",true) || !address.contains("avenida",true)) {
                    val builder = AlertDialog.Builder(activity)
                    builder.setMessage("Esse endereço $address não está no nosso sistema, caso você acredite que deveria estar, aperte Ok")
                            .setPositiveButton("Ok") { dialog, which ->
                                val dbUtilities = DBUtilities(activity)
                                val suggest = Suggest(dia,address,"android")
                                dbUtilities.SendSuggest(suggest)
                            }
                            .setNegativeButton("Cancelar", null)
                    val dialog = builder.create()
                    dialog.show()
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.resources.getColor(R.color.black))
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.resources.getColor(R.color.colorAccent))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }


    fun buildAlertMessageNoGps() {
        /*val dialog = Dialog(activity,R.style.Dialog_No_Border)
        //val layoutBinder = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(dialog.getContext()),R.layout.alert_layout,null,false);
        dialog.setContentView(R.layout.alert_layout)
        dialog.setCanceledOnTouchOutside(true)
        val title = dialog.findViewById<TextView>(R.id.atitle)
        val message = dialog.findViewById<TextView>(R.id.message)
        val button = dialog.findViewById<Button>(R.id.defaultbutton)

        title.text = "GPS desativado"
        message.text = "Ative seu GPS para que possamos ajudar a encontrar números de emrgência"
        button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        */

        Snacky.builder().setActivity(activity).error().setText("Erro ao obter localização GPS desativado")
                .setAction("OK") {
                    activity.startActivity( Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE).show()


    }

    fun CommentAlert(id: String) {
        if (checkuser()) {
            val myDialog = BottomSheetDialog(Objects.requireNonNull(activity))
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            myDialog.setCanceledOnTouchOutside(true)
            myDialog.setContentView(R.layout.comment_dialog)
            if (!myDialog.isShowing) {
                myDialog.show()
            }
            val title:TextView = myDialog.findViewById(R.id.title)!!
            val rating:ColorRatingBar = myDialog.findViewById(R.id.rating)!!
            val comment:EditText = myDialog.findViewById(R.id.comment)!!
            val save:Button = myDialog.findViewById(R.id.save)!!

            title.text = "Avalie seu atendimento"

            save.setOnClickListener { Salvar(comment,rating, id, myDialog) }

        } else {
            val providers = listOf(AuthUI.IdpConfig.GoogleBuilder().build())
            activity.startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(), RC_SIGN_IN)
        }


    }

    private fun Salvar(comment: EditText,rating: ColorRatingBar, id: String, dialog: BottomSheetDialog) {


        val user = FirebaseAuth.getInstance().currentUser
        val username = user!!.displayName
        val comnt = comment.text.toString()
        val oppinion = rating.rating.toString()
        //val tempo = time.text.toString() + timetype!!.selectedItem.toString()
        val dia = dia
        val cb = Comment(comnt,username!!,dia,oppinion,id)

        val commentsDB = DBUtilities(activity)
        commentsDB.SendComment(cb)
        dialog.dismiss()
    }


}



