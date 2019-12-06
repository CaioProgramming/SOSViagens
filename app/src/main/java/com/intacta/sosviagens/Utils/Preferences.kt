package com.intacta.sosviagens.Utils

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {
    internal var mySharedPreferences: SharedPreferences

    init {
        mySharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE)
    }

    fun setAgree(state: Boolean) {
        val editor = mySharedPreferences.edit()
        editor.putBoolean("Terms", state)
        editor.apply()
    }

    fun agreestate(): Boolean {
        return mySharedPreferences.getBoolean("Terms", false)
    }


}
