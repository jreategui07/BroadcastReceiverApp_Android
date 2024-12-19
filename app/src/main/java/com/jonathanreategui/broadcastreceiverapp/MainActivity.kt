package com.jonathanreategui.broadcastreceiverapp

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jonathanreategui.broadcastreceiverapp.ui.theme.BroadcastReceiverAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BroadcastReceiverAppTheme {
                val context = LocalContext.current

                var airplaneModeText by remember { mutableStateOf("Toggle airplane mode.") }
                var bluetoothText by remember { mutableStateOf("Toggle bluetooth.") }
                var batterySaverText by remember { mutableStateOf("Toggle Battery save.") }
                var wifiStateText by remember { mutableStateOf("Toggle Wi-Fi.") }

                setupAirplaneModeReceiver(context, onAirplaneModeChanged = { status ->
                    airplaneModeText = status
                })

                setupBluetoothReceiver(context, onBluetoothStatusChanged = { status ->
                    bluetoothText = status
                })

                setupBatterySaverReceiver(context, onBatterySaverStatusChanged = { status ->
                    batterySaverText = status
                })

                setupWifiStateReceiver(context, onWifiStateChanged = { status ->
                    wifiStateText = status
                })

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(
                        airplaneModeText = airplaneModeText,
                        bluetoothText = bluetoothText,
                        batterySaverText = batterySaverText,
                        wifiStateText = wifiStateText
                    )
                }
            }
        }
    }

    @Composable
    private fun setupAirplaneModeReceiver(
        context: Context,
        onAirplaneModeChanged: (String) -> Unit
    ) {
        DisposableEffect(context, Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val airplaneModeReceiver = AirplaneModeReceiver(
                onEnabled = { onAirplaneModeChanged("Airplane mode: enabled.") },
                onDisabled = { onAirplaneModeChanged("Airplane mode: disabled.") }
            )
            airplaneModeReceiver.register(context)
            onDispose {
                airplaneModeReceiver.unRegister(context)
            }
        }
    }

    @Composable
    private fun setupBluetoothReceiver(
        context: Context,
        onBluetoothStatusChanged: (String) -> Unit
    ) {
        DisposableEffect(context, BluetoothAdapter.ACTION_STATE_CHANGED) {
            val bluetoothReceiver = BluetoothStateReceiver(
                onEnabled = { onBluetoothStatusChanged("Bluetooth: enabled.") },
                onDisabled = { onBluetoothStatusChanged("Bluetooth: disabled.") }
            )
            bluetoothReceiver.register(context)
            onDispose {
                bluetoothReceiver.unRegister(context)
            }
        }
    }

    @Composable
    private fun setupBatterySaverReceiver(
        context: Context,
        onBatterySaverStatusChanged: (String) -> Unit
    ) {
        DisposableEffect(context) {
            val batterySaverReceiver = BatterySaverReceiver(
                onEnabled = { onBatterySaverStatusChanged("Battery saver: enabled.") },
                onDisabled = { onBatterySaverStatusChanged("Battery saver: disabled.") }
            )
            batterySaverReceiver.register(context)
            onDispose {
                batterySaverReceiver.unregister(context)
            }
        }
    }

    @Composable
    private fun setupWifiStateReceiver(
        context: Context,
        onWifiStateChanged: (String) -> Unit
    ) {
        DisposableEffect(context) {
            val wifiStateReceiver = WifiStateReceiver(
                onEnabled = { onWifiStateChanged("Wi-Fi: enabled.") },
                onDisabled = { onWifiStateChanged("Wi-Fi: disabled.") }
            )
            wifiStateReceiver.register(context)
            onDispose {
                wifiStateReceiver.unregister(context)
            }
        }
    }
}

@Composable
fun Description(text: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun MainContent(
    airplaneModeText: String,
    bluetoothText: String,
    batterySaverText: String,
    wifiStateText: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Description(airplaneModeText)
        Description(bluetoothText)
        Description(batterySaverText)
        Description(wifiStateText)
    }
}


@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    BroadcastReceiverAppTheme {
        MainContent(
            airplaneModeText = "Airplane mode: disabled.",
            bluetoothText = "Bluetooth: enabled.",
            batterySaverText = "Battery saver: disabled",
            wifiStateText = "Wi-Fi: enabled"
        )
    }
}
