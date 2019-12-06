package com.intacta.sosviagens.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.intacta.sosviagens.view.Adapter.RecyclerMoreThenOneAdapter
import com.intacta.sosviagens.model.Beans.Comment
import com.intacta.sosviagens.model.Beans.Road
import com.intacta.sosviagens.model.Beans.Suggest
import com.intacta.sosviagens.model.Database.DBUtilities
import com.intacta.sosviagens.R

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Objects
import  kotlinx.android.synthetic.main.alert_layout.*

import hyogeun.github.com.colorratingbarlib.ColorRatingBar

import com.intacta.sosviagens.Utils.Utilities.RC_SIGN_IN

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
                if (!dataSnapshot.exists() && !address.contains("Rua") || !address.contains("rua")) {
                    val builder = AlertDialog.Builder(activity)
                    builder.setMessage("Esse endereço $address não está no nosso sistema, caso você acredite que deveria estar, aperte Ok")
                            .setPositiveButton("Ok") { dialog, which ->
                                val dbUtilities = DBUtilities(activity)
                                val suggest = Suggest()
                                suggest.rodovia = address
                                suggest.setDia(dia)
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


        val dialog = Dialog(activity,R.style.Dialog_No_Border)
        //val layoutBinder = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(dialog.getContext()),R.layout.alert_layout,null,false);
        dialog.setContentView(R.layout.alert_layout)
        val title = dialog.findViewById<TextView>(R.id.atitle)
        val message = dialog.findViewById<TextView>(R.id.message)
        val button = dialog.findViewById<Button>(R.id.defaultbutton)
        val icon = dialog.findViewById<ImageView>(R.id.icon)

        title.text = "GPS desativado"
        message.text = "Ative seu GPS para que possamos ajudar a encontrar números de emrgência"
        button.setOnClickListener {
            dialog.dismiss()
            activity.startActivity( Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }

        dialog.show()




    }

    fun CommentAlert(road: Road) {
        val out = AnimationUtils.loadAnimation(activity, R.anim.fui_slide_out_left)
        val `in` = AnimationUtils.loadAnimation(activity, R.anim.fui_slide_in_right)
        if (checkuser()) {
            val myDialog = BottomSheetDialog(Objects.requireNonNull(activity), R.style.Dialog_No_Border)
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            myDialog.setContentView(R.layout.comment_dialog)
            if (!myDialog.isShowing) {
                myDialog.show()
            }
            //save!!.setOnClickListener { Salvar(comment!!, cortesy!!, time!!, rating!!, road, myDialog) }

        } else {
            val providers = listOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.GoogleBuilder().build())
            activity.startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(), RC_SIGN_IN)
        }


    }

    private fun Salvar(comment: EditText, cortesy: EditText, time: EditText, rating: ColorRatingBar, road: Road, dialog: BottomSheetDialog) {
        val user = FirebaseAuth.getInstance().currentUser
        val username = user!!.displayName
        val comnt = comment.text.toString()
        val oppinion = rating.rating.toString()
        //val tempo = time.text.toString() + timetype!!.selectedItem.toString()
        val dia = dia
        //val cb = Comment(comnt, username!!, dia, cortesy.text.toString(), tempo, oppinion, road.id!!)
        /*cb.rodovia = road.rodovia
        cb.concessionaria = road.concessionaria
        val commentsDB = DBUtilities(activity)
        commentsDB.SendComment(cb)*/
        dialog.dismiss()
    }


    fun MoreConcess(roads: ArrayList<Road>) {
        val options = ArrayList(roads)
        val myDialog = BottomSheetDialog(Objects.requireNonNull(activity), R.style.Dialog_No_Border)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setContentView(R.layout.morethenonelayout)
        if (myDialog.isShowing) {
            println("Alerta sendo exibido")
            return

        }
        val roadssuggestion = myDialog.findViewById<RecyclerView>(R.id.roads)

        val cancelar = myDialog.findViewById<Button>(R.id.cancel)
        cancelar!!.setOnClickListener { myDialog.dismiss() }
        if (roadssuggestion!!.childCount > 0) {
            myDialog.show()
            return
        }
        val recyclerMoreThenOneAdapter = RecyclerMoreThenOneAdapter(activity, options)
        val llm = GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false)

        roadssuggestion.adapter = recyclerMoreThenOneAdapter
        roadssuggestion.layoutManager = llm

        myDialog.setOnDismissListener { options.clear() }
        myDialog.show()

    }




}



