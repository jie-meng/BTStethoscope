package com.thoughtworks.btstethoscope.utils

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Vibrator


object VibrateUtils {

    fun vibrate(context: Context, ms: Long) {
        val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator?
        vibrator!!.vibrate(ms)
    }
}