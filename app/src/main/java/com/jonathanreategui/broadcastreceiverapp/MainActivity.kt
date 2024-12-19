package com.jonathanreategui.broadcastreceiverapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.jonathanreategui.broadcastreceiverapp.ui.theme.BroadcastReceiverAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BroadcastReceiverAppTheme {
                val context = LocalContext.current
                var text by remember { mutableStateOf("Toggle airplane mode to see the event update.") }
                DisposableEffect(context, Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                    val receiver = AirplaneModeReceiver(
                        onEnabled = { text = "Airplane mode enabled." },
                        onDisabled = { text = "Airplane mode disabled." }
                    )
                    receiver.register(context)
                    onDispose {
                        receiver.unRegister(context)
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Description(text = text)
                }
            }
        }
    }
}

@Composable
fun Description(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BroadcastReceiverAppTheme {
        Description("Android")
    }
}
