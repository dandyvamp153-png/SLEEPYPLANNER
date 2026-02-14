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
import com.crossline.app.ui.moneytrack.business.AiBusinessScreen
import com.crossline.app.ui.moneytrack.business.AiBusinessViewModel
import com.crossline.app.ui.moneytrack.investment.InvestmentScreen
import com.crossline.app.ui.moneytrack.investment.InvestmentViewModel
import com.crossline.app.ui.moneytrack.study.StudyScreen
import com.crossline.app.ui.moneytrack.study.StudyViewModel
import com.crossline.app.ui.pickup.PickupScreen

object Routes {
    const val DASHBOARD = "dashboard"
    // Diet
    const val DIET = "diet"
    const val DIET_MEAL_LOG = "diet/meal_log"
    const val DIET_PIVOT_CAMERA = "diet/pivot_camera"
    const val DIET_BODY_CHECK = "diet/body_check"
    // MoneyTrack
    const val MONEY_TRACK = "money_track"
    const val MONEY_TRACK_STUDY = "money_track/study"
    const val MONEY_TRACK_AI_BUSINESS = "money_track/ai_business"
    const val MONEY_TRACK_INVESTMENT = "money_track/investment"
    // Pickup
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
            MealLogScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Routes.DIET_PIVOT_CAMERA) {
            PivotCameraScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.DIET_BODY_CHECK) {
            val viewModel = hiltViewModel<DietViewModel>()
            BodyCheckScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }

        // MoneyTrack session
        composable(Routes.MONEY_TRACK) {
            MoneyTrackScreen(
                onBack = { navController.popBackStack() },
                onNavigateToStudy = { navController.navigate(Routes.MONEY_TRACK_STUDY) },
                onNavigateToAiBusiness = { navController.navigate(Routes.MONEY_TRACK_AI_BUSINESS) },
                onNavigateToInvestment = { navController.navigate(Routes.MONEY_TRACK_INVESTMENT) }
            )
        }
        composable(Routes.MONEY_TRACK_STUDY) {
            val viewModel = hiltViewModel<StudyViewModel>()
            StudyScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Routes.MONEY_TRACK_AI_BUSINESS) {
            val viewModel = hiltViewModel<AiBusinessViewModel>()
            AiBusinessScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Routes.MONEY_TRACK_INVESTMENT) {
            val viewModel = hiltViewModel<InvestmentViewModel>()
            InvestmentScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }

        // Pickup session
        composable(Routes.PICKUP) {
            PickupScreen(onBack = { navController.popBackStack() })
        }
    }
}
