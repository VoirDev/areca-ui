package dev.voir.areca.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            //statusBarStyle = SystemBarStyle.light(scrim = Color.Blue, darkScrim = Color.Red.value)
        )
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}
