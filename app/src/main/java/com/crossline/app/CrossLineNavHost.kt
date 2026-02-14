package com.crossline.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.crossline.app.ui.pickup.PickupRecordListScreen
import com.crossline.app.ui.pickup.PickupScreen
import com.crossline.app.ui.pickup.PickupViewModel
import com.crossline.app.ui.pickup.SilentCameraScreen

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
    const val PICKUP_CAMERA = "pickup/camera"
    const val PICKUP_RECORDS = "pickup/records"
}

@Composable
fun CrossLineNavHost(
    onEmergencySwitchReady: ((() -> Unit) -> Unit)? = null
) {
    val navController = rememberNavController()

    // Register emergency switch: flip phone -> jump to MoneyTrack
    LaunchedEffect(Unit) {
        onEmergencySwitchReady?.invoke {
            // Clear backstack and navigate to MoneyTrack (stealth)
            navController.navigate(Routes.MONEY_TRACK) {
                popUpTo(Routes.DASHBOARD) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

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

        // Pickup session (encrypted, AI-isolated)
        composable(Routes.PICKUP) {
            val viewModel = hiltViewModel<PickupViewModel>()
            PickupScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToCamera = { navController.navigate(Routes.PICKUP_CAMERA) },
                onNavigateToRecords = { navController.navigate(Routes.PICKUP_RECORDS) }
            )
        }
        composable(Routes.PICKUP_CAMERA) {
            SilentCameraScreen(
                onBack = { navController.popBackStack() },
                onPhotoTaken = { /* Wire to PickupViewModel when CameraX capture is ready */ }
            )
        }
        composable(Routes.PICKUP_RECORDS) {
            val viewModel = hiltViewModel<PickupViewModel>()
            PickupRecordListScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
