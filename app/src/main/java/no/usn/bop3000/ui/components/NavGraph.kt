package no.usn.bop3000.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import no.usn.bop3000.ui.screens.AddPointScreen
import no.usn.bop3000.ui.screens.PincodeScreen
import no.usn.bop3000.ui.screens.HomeScreen
import no.usn.bop3000.ui.screens.TrailScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("trail") {
            TrailScreen(navController)
        }
        composable("addpoint") {
            AddPointScreen(navController = navController)
        }
        composable("pincode?redirectTo={target}") { backStackEntry ->
            val redirectTo = backStackEntry.arguments?.getString("target") ?: "home"
            PincodeScreen(
                navController = navController,
                onSuccess = {
                    navController.navigate(redirectTo)
                }
            )
        }
    }
}