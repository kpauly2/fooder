package tech.pauly.fooder.data.network

import arrow.core.Either
import tech.pauly.fooder.data.domain.model.FoodError
import tech.pauly.fooder.data.network.model.EdamamFoodDetails
import tech.pauly.fooder.data.network.model.EdamamFoodId
import tech.pauly.fooder.data.network.model.EdamamFoodNetworkResponse

interface FoodDataSource {
    suspend fun getFoodList(query: String): Either<FoodError, EdamamFoodNetworkResponse>
    suspend fun getFoodDetails(foodId: EdamamFoodId): Either<FoodError, EdamamFoodDetails>
}