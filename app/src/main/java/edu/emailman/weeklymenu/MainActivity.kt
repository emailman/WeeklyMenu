package edu.emailman.weeklymenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import edu.emailman.weeklymenu.ui.navigation.WeeklyMenuNavHost
import edu.emailman.weeklymenu.ui.theme.WeeklyMenuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val application = application as WeeklyMenuApplication

        setContent {
            WeeklyMenuTheme {
                WeeklyMenuNavHost(
                    repository = application.repository,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
