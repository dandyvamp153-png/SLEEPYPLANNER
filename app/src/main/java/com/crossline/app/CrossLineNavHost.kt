package com.crossline.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crossline.app.ui.dashboard.DashboardScreen
import com.crossline.app.ui.diet.DietScreen
import com.crossline.app.ui.moneytrack.MoneyTrackScreen
import com.crossline.app.ui.pickup.PickupScreen

object Routes {
    const val DASHBOARD = "dashboard"
    const val DIET = "diet"
    const val MONEY_TRACK = "money_track"
    const val PICKUP = "pickup"
}

@Composable
fun CrossLineNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateToDiet = { navController.navigate(Routes.DIET) },
                onNavigateToMoneyTrack = { navController.navigate(Routes.MONEY_TRACK) },
                onNavigateToPickup = { navController.navigate(Routes.PICKUP) }
            )
        }
        composable(Routes.DIET) {
            DietScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.MONEY_TRACK) {
            MoneyTrackScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.PICKUP) {
            PickupScreen(onBack = { navController.popBackStack() })
        }
    }
}
