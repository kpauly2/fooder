package tech.pauly.fooder.food

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.pauly.fooder.data.domain.FoodRepository
import tech.pauly.fooder.data.domain.model.FoodError
import tech.pauly.fooder.data.domain.model.Macros
import javax.inject.Inject

@HiltViewModel
class FoodSearchViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private var currentSearch: String? = null

    private val _foodSearchUiState = MutableStateFlow<FoodSearchUiState>(FoodSearchUiState.Empty)
    val foodSearchUiState = _foodSearchUiState.asStateFlow()

    val recentSearches = foodRepository.recentSearches.stateIn(
        scope = viewModelScope,
        initialValue = listOf(),
        started = SharingStarted.WhileSubscribed(5_000),
    )

    // we use the whole item here when we only really need the id to allow the VM to combine the
    // original food item data with the detail data since the detail response doesn't return everything
    private var currentDetailFood: FoodUiItem? = null
    private val _foodDetailsUiState = MutableStateFlow<FoodDetailsUiState>(FoodDetailsUiState.Empty)
    val foodDetailsUiState: StateFlow<FoodDetailsUiState> = _foodDetailsUiState.asStateFlow()

    fun onSearch(query: String) = viewModelScope.launch {
        currentSearch = query
        if (currentSearch.isNullOrEmpty()) {
            // clear list if user submitted empty string
            _foodSearchUiState.update { FoodSearchUiState.Empty }
        } else {
            // set loading indicator
            _foodSearchUiState.update { FoodSearchUiState.Loading }
            // get foods for user query, show error if necessary, otherwise show success state
            foodRepository.getFoodsForQuery(query).onFailure { error ->
                _foodSearchUiState.update {
                    FoodSearchUiState.Error(error)
                }
            }.onSuccess { foodList ->
                _foodSearchUiState.update {
                    FoodSearchUiState.Success(FoodListUiState(foodList.list.map { food ->
                        FoodUiItem(
                            name = food.name,
                            image = food.image,
                            id = food.id,
                            macros = food.macros
                        )
                    }))
                }
            }
        }
    }

    fun onClickFoodCard(food: FoodUiItem) = viewModelScope.launch {
        currentDetailFood = food
        _foodDetailsUiState.update {
            FoodDetailsUiState.Loading
        }
        foodRepository.getFoodDetails(food.id).onFailure { error ->
            _foodDetailsUiState.update {
                FoodDetailsUiState.Error(error)
            }
        }.onSuccess { foodDetails ->
            _foodDetailsUiState.update {
                FoodDetailsUiState.Success(
                    FoodDetailsUiItem(
                        id = food.id,
                        name = food.name,
                        image = food.image,
                        weight = foodDetails.weight,
                        calories = foodDetails.calories,
                        food.macros
                    )
                )
            }
        }
    }

    fun onDismissBottomSheet() = viewModelScope.launch {
        _foodDetailsUiState.update {
            FoodDetailsUiState.Empty
        }
    }
}

@Stable
sealed interface FoodSearchUiState {
    data object Empty : FoodSearchUiState
    data object Loading : FoodSearchUiState
    data class Success(val foodList: FoodListUiState) : FoodSearchUiState
    data class Error(val error: FoodError) : FoodSearchUiState

}

@Stable
data class FoodListUiState(
    val list: List<FoodUiItem>
)

@Stable
sealed interface FoodDetailsUiState {
    data object Empty : FoodDetailsUiState
    data object Loading : FoodDetailsUiState
    data class Success(val foodDetails: FoodDetailsUiItem) : FoodDetailsUiState
    data class Error(val error: FoodError) : FoodDetailsUiState
}

@Stable
open class FoodUiItem(
    val id: String,
    val name: String,
    val image: String?,
    val macros: Macros?
)

@Stable
data class FoodDetailsUiItem(
    val id: String,
    val name: String,
    val image: String?,
    val weight: Int,
    val calories: Int,
    val macros: Macros?
)