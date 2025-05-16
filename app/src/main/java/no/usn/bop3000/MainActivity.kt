package no.usn.bop3000

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import no.usn.bop3000.ui.components.AppNavHost
import no.usn.bop3000.ui.theme.BopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            BopTheme {
                AppNavHost(navController = navController)
            }
        }
    }
}
