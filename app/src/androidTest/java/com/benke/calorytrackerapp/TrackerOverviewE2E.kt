package com.benke.calorytrackerapp

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.benke.calorytrackerapp.navigation.Route
import com.benke.calorytrackerapp.repository.TrackerRepositoryFake
import com.benke.calorytrackerapp.ui.theme.CaloryTrackerTheme
import com.benke.core.domain.model.ActivityLevel
import com.benke.core.domain.model.Gender
import com.benke.core.domain.model.GoalType
import com.benke.core.domain.model.UserInfo
import com.benke.core.domain.preferences.Preferences
import com.benke.core.domain.use_case.FilterOutDigitsUseCase
import com.benke.tracker_domain.model.TrackableFood
import com.benke.tracker_domain.usecase.CalculateMealNutrientsUseCase
import com.benke.tracker_domain.usecase.DeleteTrackedFoodUseCase
import com.benke.tracker_domain.usecase.GetFoodsForDateUseCase
import com.benke.tracker_domain.usecase.SearchFoodUseCase
import com.benke.tracker_domain.usecase.TrackFoodUseCase
import com.benke.tracker_domain.usecase.TrackerUseCases
import com.benke.tracker_presentation.search.SearchScreen
import com.benke.tracker_presentation.search.SearchViewModel
import com.benke.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.benke.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@HiltAndroidTest
class TrackerOverviewE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var repository: TrackerRepositoryFake
    private lateinit var useCases: TrackerUseCases
    private lateinit var preferences: Preferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter",
        "UnusedMaterial3ScaffoldPaddingParameter"
    )
    @Before
    fun setup() {
        preferences = mockk(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 20,
            weight = 80f,
            height = 180,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )
        repository = TrackerRepositoryFake()
        useCases = TrackerUseCases(
            trackFoodUseCase = TrackFoodUseCase(repository),
            searchFoodUseCase = SearchFoodUseCase(repository),
            deleteTrackedFoodUseCase = DeleteTrackedFoodUseCase(repository),
            getFoodsForDateUseCase = GetFoodsForDateUseCase(repository),
            calculateMealNutrientsUseCase = CalculateMealNutrientsUseCase(preferences)
        )
        trackerOverviewViewModel = TrackerOverviewViewModel(preferences, useCases)
        searchViewModel = SearchViewModel(useCases, FilterOutDigitsUseCase())

        composeRule.setContent {
            CaloryTrackerTheme {
                navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Route.TRACKER_OVERVIEW
                    ) {
                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(
                                onNavigateToSearch = { mealName, day, month, year ->
                                    navController.navigate(
                                        Route.SEARCH + "/$mealName" +
                                                "/$day" +
                                                "/$month" +
                                                "/$year"
                                    )
                                },
                                viewModel = trackerOverviewViewModel
                            )
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
                                viewModel = searchViewModel,
                                mealName = mealName,
                                day = day,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                },
                                onShowSnackbar = {}
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun addBreakfast_appearsUnderBreakfast_nutrientsProperlyCalculated() {
        repository.searchResult = listOf(
            TrackableFood(
                name = "banana",
                imageUrl = null,
                caloriesPer100g = 150,
                carbsPer100g = 50,
                proteinsPer100g = 5,
                fatsPer100g = 1
            )
        )

        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        composeRule.onNodeWithText("Add Breakfast").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Breakfast").performClick()
        composeRule.onNodeWithText("Add Breakfast")
            .assertIsDisplayed()
            .performClick()

        assertThat(
            navController.currentDestination?.route?.startsWith(Route.SEARCH)
        ).isTrue()

        composeRule.onNodeWithTag("search_textfield").performTextInput("banana")
        composeRule.onNodeWithContentDescription("Search...").performClick()

        composeRule.onNodeWithText("Carbs").performClick()
        composeRule.onNodeWithContentDescription("Amount").performTextInput(addedAmount.toString())
        composeRule.onNodeWithContentDescription("Track").performClick()

        assertThat(
            navController.currentDestination?.route?.startsWith(Route.TRACKER_OVERVIEW)
        ).isTrue()

        composeRule.onAllNodesWithText(expectedCarbs.toString()).onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText(expectedProtein.toString()).onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText(expectedCalories.toString()).onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText(expectedFat.toString()).onFirst().assertIsDisplayed()
    }
}