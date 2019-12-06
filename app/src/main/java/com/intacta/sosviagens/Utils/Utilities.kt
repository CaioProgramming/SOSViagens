package com.intacta.sosviagens.Utils

import android.app.Activity
import android.content.res.Configuration


object Utilities {
    val RC_SIGN_IN = 123
    var calltitle = "Números de emergência"
    fun Isnight(activity: Activity): Boolean {
        return (activity.resources.configuration.uiMode == Configuration.UI_MODE_NIGHT_YES)
    }
    var REQUEST_CHECK_SETTINGS = 100


}
