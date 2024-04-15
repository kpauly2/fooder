package tech.pauly.fooder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions


@Composable
fun rememberAppState(): FooderAppState {
    val navController = rememberNavController()
    return remember { FooderAppState(navController = navController) }
}

@Stable
class FooderAppState(
    val navController: NavHostController
) {
    val topLevelDestinations = TopLevelDestination.entries
    val currentDestination: NavDestination? = navController.currentDestination

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val options = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        when (topLevelDestination) {
            TopLevelDestination.FOOD_SEARCH -> {
                navController.navigateToFoodSearch(options)
            }

            TopLevelDestination.RECIPES -> {
                navController.navigateToRecipes(options)
            }
        }
    }
}