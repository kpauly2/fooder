package tech.pauly.fooder.food

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.size.Size
import tech.pauly.fooder.data.domain.model.FoodError
import tech.pauly.fooder.data.local.model.RecentSearch


@Composable
fun FoodSearchRoute(
    searchActive: Boolean,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: FoodSearchViewModel = hiltViewModel(),
) {
    val screenUiState by viewModel.foodSearchUiState.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()
    val foodDetailsUiState by viewModel.foodDetailsUiState.collectAsStateWithLifecycle()
    FoodSearchScreen(
        searchUiState = screenUiState,
        foodDetailsUiState = foodDetailsUiState,
        searchActive = searchActive,
        recentSearches = recentSearches,
        onShowSnackbar = onShowSnackbar,
        onSearch = viewModel::onSearch,
        onClickFoodCard = viewModel::onClickFoodCard,
        onDismissBottomSheet = viewModel::onDismissBottomSheet
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchScreen(
    searchUiState: FoodSearchUiState,
    foodDetailsUiState: FoodDetailsUiState,
    searchActive: Boolean,
    recentSearches: List<RecentSearch>,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onSearch: (String) -> Unit,
    onClickFoodCard: (FoodUiItem) -> Unit,
    onDismissBottomSheet: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        FoodSearchBar(
            active = searchActive,
            recentSearches = recentSearches,
            onSearch = onSearch
        )
        Spacer(modifier = Modifier.size(16.dp))
        when (searchUiState) {
            is FoodSearchUiState.Empty -> FoodSearchEmpty()
            is FoodSearchUiState.Loading -> FoodSearchLoading()
            is FoodSearchUiState.Error -> FoodSearchError(searchUiState.error, onShowSnackbar)
            is FoodSearchUiState.Success -> {
                val sheetState = rememberModalBottomSheetState()

                FoodList(searchUiState, onClickFoodCard = {
                    onClickFoodCard(it)
                })

                FoodDetailsBottomSheet(foodDetailsUiState, sheetState, onDismiss = {
                    onDismissBottomSheet()
                })
            }
        }

    }
}

@Composable
fun FoodSearchLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 72.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp))
    }
}

@Composable
fun FoodSearchError(
    error: FoodError,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    LaunchedEffect(true) {
        onShowSnackbar(error.message, null)
    }
}

@Composable
fun FoodList(success: FoodSearchUiState.Success, onClickFoodCard: (FoodUiItem) -> Unit) {
    val state = rememberLazyStaggeredGridState()
    LazyVerticalStaggeredGrid(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
        columns = StaggeredGridCells.Fixed(2),
        state = state,
    ) {
        items(items = success.foodList.list, key = { it.id }) { food ->
            FoodCard(food, onClickFoodCard = onClickFoodCard)
        }
    }
}
