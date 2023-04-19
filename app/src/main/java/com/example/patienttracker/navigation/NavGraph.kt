package com.example.patienttracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.patienttracker.presentation.patient_details.PatientDetailsScreen
import com.example.patienttracker.presentation.patient_list.PatientListScreen
import com.example.patienttracker.util.Constants.PATIENT_DETAILS_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object PatientList : Screen("patient_list_screen")
    object PatientDetails : Screen(
        "patient_details_screen?$PATIENT_DETAILS_ARGUMENT_KEY=" +
                "{$PATIENT_DETAILS_ARGUMENT_KEY}"
    ) {
        fun passPatientId(patientId: Int? = null): String {
            return "patient_details_screen?$PATIENT_DETAILS_ARGUMENT_KEY=$patientId"
        }
    }
}

@Composable
fun NavGraphSetup(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PatientList.route
    ) {
        composable(
            route = Screen.PatientList.route
        ) {
            PatientListScreen(
                onFabClick = {
                    navController.navigate(Screen.PatientDetails.route)
                },
                onItemClick = { patientId ->
                    navController.navigate(Screen.PatientDetails.passPatientId(patientId))
                }
            )
        }
        composable(
            route = Screen.PatientDetails.route,
            arguments = listOf(navArgument(name = PATIENT_DETAILS_ARGUMENT_KEY) {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            PatientDetailsScreen(
                onBackClick = { navController.navigateUp() },
                onSuccessfulSaving = { navController.navigateUp() }
            )
        }
    }
}