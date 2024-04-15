package tech.pauly.fooder.data.network.model

import arrow.optics.optics
import kotlinx.serialization.Serializable


@Serializable
@optics
@JvmInline
value class Kcal(val kcal: Float) {
    companion object
}

@Serializable
@optics
@JvmInline
value class Gram(val gram: Float) {
    companion object
}

@Serializable
@optics
@JvmInline
value class Milligram(val milligram: Float) {
    companion object
}

@Serializable
@optics
@JvmInline
value class Microgram(val microgram: Float) {
    companion object
}