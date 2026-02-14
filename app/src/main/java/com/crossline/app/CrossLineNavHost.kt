package com.crossline.app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crossline.app.ui.dashboard.DashboardScreen
import com.crossline.app.ui.diet.BodyCheckScreen
import com.crossline.app.ui.diet.DietScreen
import com.crossline.app.ui.diet.DietViewModel
import com.crossline.app.ui.diet.MealLogScreen
import com.crossline.app.ui.diet.PivotCameraScreen
import com.crossline.app.ui.moneytrack.MoneyTrackScreen
import com.crossline.app.ui.pickup.PickupScreen

object Routes {
    const val DASHBOARD = "dashboard"
    const val DIET = "diet"
    const val DIET_MEAL_LOG = "diet/meal_log"
    const val DIET_PIVOT_CAMERA = "diet/pivot_camera"
    const val DIET_BODY_CHECK = "diet/body_check"
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

        // Diet session
        composable(Routes.DIET) {
            DietScreen(
                onBack = { navController.popBackStack() },
                onNavigateToMealLog = { navController.navigate(Routes.DIET_MEAL_LOG) },
                onNavigateToPivotCamera = { navController.navigate(Routes.DIET_PIVOT_CAMERA) },
                onNavigateToBodyCheck = { navController.navigate(Routes.DIET_BODY_CHECK) }
            )
        }
        composable(Routes.DIET_MEAL_LOG) {
            val viewModel = hiltViewModel<DietViewModel>()
            MealLogScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.DIET_PIVOT_CAMERA) {
            PivotCameraScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.DIET_BODY_CHECK) {
            val viewModel = hiltViewModel<DietViewModel>()
            BodyCheckScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Other sessions
        composable(Routes.MONEY_TRACK) {
            MoneyTrackScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.PICKUP) {
            PickupScreen(onBack = { navController.popBackStack() })
        }
    }
}
