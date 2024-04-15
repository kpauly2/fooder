package tech.pauly.fooder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import tech.pauly.fooder.R
import tech.pauly.fooder.ui.FooderIcons
import tech.pauly.fooder.food.FoodSearchRoute
import tech.pauly.fooder.recipe.RecipesRoute

const val ROUTE_FOOD_SEARCH = "route_food_search"
const val ROUTE_RECIPES = "route_recipes"
const val /**/ARG_SEARCH_ACTIVE = "arg_close_search"

@Composable
fun FooderNavHost(
    appState: FooderAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val navController = appState.navController
    NavHost(navController = navController, startDestination = ROUTE_FOOD_SEARCH) {
        foodSearchScreen(onShowSnackbar = onShowSnackbar)
        recipesScreen()
    }
}

fun NavController.navigateToFoodSearch(navOptions: NavOptions) =
    navigate(ROUTE_FOOD_SEARCH, navOptions)

fun NavGraphBuilder.foodSearchScreen(onShowSnackbar: suspend (String, String?) -> Boolean) {
    composable(
        route = ROUTE_FOOD_SEARCH,
        arguments = listOf(
            navArgument(ARG_SEARCH_ACTIVE) {
                defaultValue = false
                type = NavType.BoolType
            },
        ),
    ) {
        FoodSearchRoute(it.arguments?.getBoolean(ARG_SEARCH_ACTIVE) ?: false, onShowSnackbar)
    }
}

fun NavController.navigateToRecipes(navOptions: NavOptions) =
    navigate(ROUTE_RECIPES, navOptions)

fun NavGraphBuilder.recipesScreen() {
    composable(
        route = ROUTE_RECIPES
    ) {
        RecipesRoute()
    }
}

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    FOOD_SEARCH(
        selectedIcon = FooderIcons.Search,
        unselectedIcon = FooderIcons.SearchOutline,
        iconTextId = R.string.title_food_search,
        titleTextId = R.string.title_food_search,
    ),
    RECIPES(
        selectedIcon = FooderIcons.Recipes,
        unselectedIcon = FooderIcons.RecipesOutline,
        iconTextId = R.string.title_recipes,
        titleTextId = R.string.title_recipes,
    )
}
