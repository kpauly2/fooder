package tech.pauly.fooder.data.network.model

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.right
import arrow.core.toNonEmptyListOrNull
import arrow.optics.optics
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.pauly.fooder.data.domain.model.FoodDetails
import tech.pauly.fooder.data.domain.model.Food
import tech.pauly.fooder.data.domain.model.FoodError
import tech.pauly.fooder.data.domain.model.FoodList
import tech.pauly.fooder.data.domain.model.Macros

@Serializable
@optics
data class EdamamFoodNetworkResponse(
    val text: String, // input query string
    // the top result of a food item for the query string
    val parsed: List<EdamamFoodListItem> = listOf(),
    // all search results for the query string sorted by relevance
    val hints: List<EdamamFoodListItem> = listOf(),
    @SerialName("_links") val links: EdamamLinks? = null
) {
    companion object

    // Turn food list response into Either `FoodError.NoResults` or `FoodList`
    // `FoodError.NoResults`: No results for query
    // `FoodList`: Domain model for food items, other validation occurs
    fun asFoodList(): Either<FoodError.NoResults, FoodList> {
        // although it's the intention of API, we don't use `parsed` as the food within is repeated in `hints[0]`
        // hints[] is supposed to be used as a predictive list for searching, but due to API limits we use it as the search results instead
        val newList = hints.toNonEmptyListOrNull()?.let { list ->
            list.map { it.food.toDomainFood() }
        }
        return either {
            ensureNotNull(newList) { FoodError.NoResults() }
            FoodList(newList.distinctBy { it.id }) // Edamam data has duplicates (different data but identical ids)
        }
    }
}

@Serializable
@optics
data class FoodDetailsRequest(
    val ingredients: List<IngredientsRequest>,
) {
    companion object
}

@Serializable
@optics
data class IngredientsRequest(
    val quantity: Int,
    @SerialName("measureURI") val measureUri: String,
    val foodId: String // todo custom serialize this and measure URIs
) {
    companion object
}

@Serializable
@optics
data class EdamamLinks(
    val next: EdamamNextLink? = null
) {
    companion object
}

@Serializable
@optics
data class EdamamNextLink(
    val title: String? = null, val href: String? = null
) {
    companion object
}


@Serializable
@optics
data class EdamamFoodListItem(
    val food: EdamamFood,
    // measures
) {
    companion object
}

@Serializable
@optics
data class EdamamFood(
    val foodId: EdamamFoodId,
    val label: String,
    val knownAs: String? = null,
    val nutrients: EdamamNutrients? = null,
    val category: String? = null, // enum
    val categoryLabel: String? = null, // enum or value
    val image: String? = null
) {
    companion object

    fun toDomainFood(): Food {
        val macros = if (nutrients?.energy != null &&
            nutrients.totalLipid != null &&
            nutrients.carbohydrate != null &&
            nutrients.protein != null
        ) {
            Macros(
                calories = nutrients.energy,
                fat = nutrients.totalLipid,
                carbs = nutrients.carbohydrate,
                protein = nutrients.protein
            )
        } else null

        return Food(name = label, image = image, id = foodId, macros = macros)
    }
}

@Serializable
@optics
data class EdamamFoodDetails(
    val uri: String,
    val calories: Kcal,
    val totalWeight: Gram,
    val dietLabels: List<String>,
    val healthLabels: List<String>,
    val cautions: List<String>,  // todo total nutrients, total daily, ingredients
) {
    companion object

    fun toDomainFoodDetails(): Either<FoodError, FoodDetails> {
        return FoodDetails(
            weight = totalWeight.gram.toInt(),
            calories = calories.kcal.toInt(),
        ).right() // todo more details, error types
    }
}

typealias EdamamFoodId = String

@Serializable
data class EdamamNutrients(
    @SerialName("SUGAR.added") val addedSugar: Gram? = null,
    @SerialName("CA") val calcium: Milligram? = null,
    @SerialName("CHOCDF.net") val carbohydrateNet: Gram? = null,
    @SerialName("CHOCDF") val carbohydrate: Gram? = null, // by difference
    @SerialName("CHOLE") val cholesterol: Milligram? = null,
    @SerialName("ENERC_KCAL") val energy: Kcal? = null,
    @SerialName("FAMS") val fattyAcidsMono: Gram? = null, // total monounsaturated fats
    @SerialName("FAPU") val fattyAcidsPoly: Gram? = null, // total polyunsaturated fats
    @SerialName("FASAT") val fattyAcidsSat: Gram? = null, // total saturated fats
    @SerialName("FATRN") val fattyAcidsTrans: Gram? = null, // total trans fats
    @SerialName("FIBTG") val fiber: Gram? = null, // total dietary
    @SerialName("FOLDFE") val folateDFE: Microgram? = null, // dietary folate equivalents
    @SerialName("FOLFD") val folateFood: Microgram? = null,
    @SerialName("FOLAC") val folicAcid: Microgram? = null,
    @SerialName("FE") val iron: Milligram? = null, // Fe
    @SerialName("MG") val magnesium: Milligram? = null,
    @SerialName("NIA") val niacin: Milligram? = null,
    @SerialName("P") val phosphorus: Milligram? = null, // P
    @SerialName("K") val potassium: Milligram? = null, // K
    @SerialName("PROCNT") val protein: Gram? = null,
    @SerialName("PIBF") val riboflavin: Milligram? = null,
    @SerialName("NA") val sodium: Milligram? = null, // NA
    @SerialName("Sugar.alcohol") val sugarAlcohols: Gram? = null,
    @SerialName("SUGAR") val totalSugars: Gram? = null,
    @SerialName("THIA") val thiamin: Milligram? = null,
    @SerialName("FAT") val totalLipid: Gram? = null, // (fat)
    @SerialName("VITA_RAE") val vitaminA: Microgram? = null, // Vitamin A
    @SerialName("VITB12") val vitaminB12: Microgram? = null, // Vitamin B-12
    @SerialName("VITB6A") val vitaminB6: Microgram? = null, // Vitamin B-6
    @SerialName("VITC") val vitaminC: Microgram? = null, // Vitamin C, total ascorbic acid
    @SerialName("VITD") val vitaminD: Microgram? = null, // Vitamin D (D2 + D3)
    @SerialName("TOCPHA") val vitaminE: Microgram? = null, // Vitamin E (alpha-tocopherol)
    @SerialName("VITK1") val vitaminK: Microgram? = null, // Vitamin K (phylloquinone)
    @SerialName("WATER") val water: Gram? = null,
    @SerialName("ZN") val zinc: Milligram? = null // Zn
) {
    companion object
}
