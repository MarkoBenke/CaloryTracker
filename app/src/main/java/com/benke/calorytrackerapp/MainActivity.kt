package com.benke.calorytrackerapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.benke.calorytrackerapp.navigation.Route
import com.benke.calorytrackerapp.ui.theme.CaloryTrackerTheme
import com.benke.core.domain.preferences.Preferences
import com.benke.onboarding_presentation.activity.ActivityScreen
import com.benke.onboarding_presentation.age.AgeScreen
import com.benke.onboarding_presentation.gender.GenderScreen
import com.benke.onboarding_presentation.goal.GoalScreen
import com.benke.onboarding_presentation.height.HeightScreen
import com.benke.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.benke.onboarding_presentation.weight.WeightScreen
import com.benke.onboarding_presentation.welcome.WelcomeScreen
import com.benke.tracker_presentation.search.SearchScreen
import com.benke.tracker_presentation.tracker_overview.TrackerOverviewScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences

    @SuppressLint(
        "UnusedMaterialScaffoldPaddingParameter",
        "UnusedMaterial3ScaffoldPaddingParameter"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shouldShowOnBoarding = preferences.loadShouldShowOnBoarding()

        setContent {
            CaloryTrackerTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = if (shouldShowOnBoarding) Route.WELCOME else Route.TRACKER_OVERVIEW
                    ) {
                        composable(Route.WELCOME) {
                            WelcomeScreen(onNextClick = {
                                navController.navigate(Route.GENDER)
                            })
                        }
                        composable(Route.AGE) {
                            AgeScreen(
                                onShowSnackbar = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message = it.asString(context))
                                    }
                                },
                                onNextClick = {
                                    navController.navigate(Route.HEIGHT)
                                }
                            )
                        }
                        composable(Route.GENDER) {
                            GenderScreen(onNextClick = {
                                navController.navigate(Route.AGE)
                            })
                        }
                        composable(Route.HEIGHT) {
                            HeightScreen(
                                onShowSnackbar = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message = it.asString(context))
                                    }
                                },
                                onNextClick = {
                                    navController.navigate(Route.WEIGHT)
                                }
                            )
                        }
                        composable(Route.WEIGHT) {
                            WeightScreen(
                                onShowSnackbar = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message = it.asString(context))
                                    }
                                },
                                onNextClick = {
                                    navController.navigate(Route.ACTIVITY)
                                }
                            )
                        }
                        composable(Route.NUTRIENT_GOAL) {
                            NutrientGoalScreen(
                                onShowSnackbar = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message = it.asString(context))
                                    }
                                },
                                onNextClick = {
                                    navController.navigate(Route.TRACKER_OVERVIEW) {
                                        popUpTo(navController.graph.id)
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                        composable(Route.ACTIVITY) {
                            ActivityScreen(
                                onNextClick = {
                                    navController.navigate(Route.GOAL)
                                }
                            )
                        }
                        composable(Route.GOAL) {
                            GoalScreen(
                                onNextClick = {
                                    navController.navigate(Route.NUTRIENT_GOAL)
                                }
                            )
                        }

                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(onNavigateToSearch = { mealName, day, month, year ->
                                navController.navigate(
                                    Route.SEARCH + "/$mealName" +
                                            "/$day" +
                                            "/$month" +
                                            "/$year"
                                )
                            })
                        }
                        composable(
                            route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                            arguments = listOf(
                                navArgument("mealName") {
                                    type = NavType.StringType
                                },
                                navArgument("dayOfMonth") {
                                    type = NavType.IntType
                                },
                                navArgument("month") {
                                    type = NavType.IntType
                                },
                                navArgument("year") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            val mealName = it.arguments?.getString("mealName")!!
                            val day = it.arguments?.getInt("dayOfMonth")!!
                            val month = it.arguments?.getInt("month")!!
                            val year = it.arguments?.getInt("year")!!
                            SearchScreen(
                                onShowSnackbar = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message = it.asString(context))
                                    }
                                },
                                mealName = mealName,
                                day = day,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}