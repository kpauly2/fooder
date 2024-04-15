package tech.pauly.fooder.data.domain.model

import arrow.core.NonEmptyList
import arrow.optics.optics
import kotlinx.serialization.Serializable
import tech.pauly.fooder.data.network.model.EdamamFoodId
import tech.pauly.fooder.data.network.model.Gram
import tech.pauly.fooder.data.network.model.Kcal
import tech.pauly.fooder.roundTo2Decimal


// Domain models for Food

@Serializable
sealed interface FoodError {
    val message: String

    class Basic(override val message: String) : FoodError
    class Unknown(override val message: String = "Unknown error") : FoodError
    class NoResults(override val message: String = "No results for given query") : FoodError
    data class Exception(val exception: kotlin.Exception) : FoodError {
        override val message: String = exception.message.orEmpty()
    }
}

@optics
data class FoodList(
    val list: NonEmptyList<Food>
) {
    companion object
}

@Serializable
@optics
data class Food(
    val name: String,
    val image: String?,
    val id: EdamamFoodId,
    val macros: Macros?
) {
    companion object
}

@Serializable
@optics
data class FoodDetails(
    val weight: Int,
    val calories: Int,
) {
    companion object
}