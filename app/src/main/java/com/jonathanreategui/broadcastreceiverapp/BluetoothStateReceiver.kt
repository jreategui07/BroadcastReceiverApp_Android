package com.jonathanreategui.broadcastreceiverapp

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class BluetoothStateReceiver(
    private val onEnabled: () -> Unit,
    private val onDisabled: () -> Unit
) : BroadcastReceiver() {

    private var registered = false

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            when (state) {
                BluetoothAdapter.STATE_ON -> onEnabled()
                BluetoothAdapter.STATE_OFF -> onDisabled()
            }
        }
    }

    fun register(context: Context) {
        if (!registered) {
            val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            context.registerReceiver(this, filter)
            registered = true
        }
    }

    fun unRegister(context: Context) {
        if (registered) {
            context.unregisterReceiver(this)
            registered = false
        }
    }
}
