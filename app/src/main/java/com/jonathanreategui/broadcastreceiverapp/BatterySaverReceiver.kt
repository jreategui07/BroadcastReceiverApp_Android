package com.jonathanreategui.broadcastreceiverapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.PowerManager

class BatterySaverReceiver(
    private val onEnabled: () -> Unit,
    private val onDisabled: () -> Unit
) : BroadcastReceiver() {

    private var registered = false

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action == PowerManager.ACTION_POWER_SAVE_MODE_CHANGED) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (powerManager.isPowerSaveMode) {
                onEnabled()
            } else {
                onDisabled()
            }
        }
    }

    fun register(context: Context) {
        if (!registered) {
            val filter = IntentFilter(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
            context.registerReceiver(this, filter)
            registered = true
        }
    }

    fun unregister(context: Context) {
        if (registered) {
            context.unregisterReceiver(this)
            registered = false
        }
    }
}
