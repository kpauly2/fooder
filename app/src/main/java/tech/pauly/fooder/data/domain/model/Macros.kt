package tech.pauly.fooder.data.domain.model

import arrow.optics.optics
import kotlinx.serialization.Serializable
import tech.pauly.fooder.data.network.model.Gram
import tech.pauly.fooder.data.network.model.Kcal
import tech.pauly.fooder.roundTo2Decimal


const val CALORIES_PER_GRAM_FAT = 9
const val CALORIES_PER_GRAM_CARB = 4
const val CALORIES_PER_GRAM_PROTEIN = 4

@Serializable
@optics
data class Macros(
    val calories: Kcal,
    val fat: Gram,
    val carbs: Gram,
    val protein: Gram,
) {
    companion object

    val fatRounded = Gram(fat.gram.roundTo2Decimal())
    val carbsRounded = Gram(carbs.gram.roundTo2Decimal())
    val proteinRounded = Gram(protein.gram.roundTo2Decimal())

    private val caloriesFromFat: Kcal = Kcal(fat.gram * CALORIES_PER_GRAM_FAT)
    private val caloriesFromCarbs: Kcal = Kcal(carbs.gram * CALORIES_PER_GRAM_CARB)
    private val caloriesFromProtein: Kcal = Kcal(protein.gram * CALORIES_PER_GRAM_PROTEIN)
    // we calc this because these often don't add up to stated calories
    private val caloriesFromMacros = Kcal(caloriesFromFat.kcal + caloriesFromCarbs.kcal + caloriesFromProtein.kcal)

    val percentageFat: Float = ((caloriesFromFat.kcal / caloriesFromMacros.kcal) * 100).roundTo2Decimal()
    val percentageCarbs: Float = ((caloriesFromCarbs.kcal / caloriesFromMacros.kcal) * 100).roundTo2Decimal()
    val percentageProtein: Float = ((caloriesFromProtein.kcal / caloriesFromMacros.kcal) * 100).roundTo2Decimal()
}
