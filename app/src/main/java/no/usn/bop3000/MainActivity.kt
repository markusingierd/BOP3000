package no.usn.bop3000

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import no.usn.bop3000.ui.screens.HomeScreen
import no.usn.bop3000.ui.theme.BopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BopTheme {HomeScreen()}
        }
    }
}
