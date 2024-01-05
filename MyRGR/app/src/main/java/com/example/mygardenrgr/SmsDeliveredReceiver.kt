package com.example.mygardenrgr

// SmsDeliveredReceiver.kt
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SmsDeliveredReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                // El mensaje fue entregado exitosamente
            }
            Activity.RESULT_CANCELED -> {
                // Entrega cancelada
            }
            // Otros códigos de resultado posibles
        }
    }
}