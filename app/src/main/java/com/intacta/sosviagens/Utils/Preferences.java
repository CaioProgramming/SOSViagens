package com.intacta.sosviagens.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    SharedPreferences mySharedPreferences;

    public Preferences(Context context) {
        mySharedPreferences = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }
    public void setAgree(boolean state) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("Terms", state);
        editor.apply();
    }

    public boolean agreestate() {
        return mySharedPreferences.getBoolean("Terms", false);
    }



}
