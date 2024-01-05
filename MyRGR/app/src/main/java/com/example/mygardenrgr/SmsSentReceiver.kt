package com.example.mygardenrgr

// SmsSentReceiver.kt
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager

class SmsSentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                // El mensaje fue enviado exitosamente
            }
            SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                // Fallo genérico
            }
            SmsManager.RESULT_ERROR_NO_SERVICE -> {
                // Sin servicio
            }
            // Otros códigos de resultado posibles
        }
    }
}