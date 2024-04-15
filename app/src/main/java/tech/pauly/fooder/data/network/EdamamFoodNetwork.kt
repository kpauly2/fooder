package tech.pauly.fooder.data.network

import arrow.core.Either
import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import tech.pauly.fooder.BuildConfig
import tech.pauly.fooder.data.domain.model.FoodError
import tech.pauly.fooder.data.network.model.EdamamFoodDetails
import tech.pauly.fooder.data.network.model.EdamamFoodId
import tech.pauly.fooder.data.network.model.EdamamFoodNetworkResponse
import tech.pauly.fooder.data.network.model.FoodDetailsRequest
import tech.pauly.fooder.data.network.model.IngredientsRequest
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private const val BASE_URL = BuildConfig.EDAMAM_FOOD_API_URL

// Edamam's Food API
// https://developer.edamam.com/food-database-api-docs

private interface EdamamFoodNetworkApi {
    @GET(value = "parser")
    suspend fun getFoodList(
        @Query("ingr") ingredient: String
    ): EdamamFoodNetworkResponse

    @POST(value = "nutrients")
    suspend fun getFoodDetails(@Body request: FoodDetailsRequest): EdamamFoodDetails
}

class EdamamFoodNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : FoodDataSource {

    private val edamamFoodApi = Retrofit.Builder()
        .baseUrl("$BASE_URL/api/food-database/v2/")
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addCallAdapterFactory(EitherCallAdapterFactory.create())
        .addConverterFactory(
            networkJson.asConverterFactory("application/json; charset=UTF-8".toMediaType())
        )
        .build()
        .create(EdamamFoodNetworkApi::class.java)

    override suspend fun getFoodList(query: String): Either<FoodError, EdamamFoodNetworkResponse> {
        return Either.catch {
            edamamFoodApi.getFoodList(query)
        }.mapLeft {
            if (it is Exception) FoodError.Exception(it)
            else FoodError.Unknown()
        }
    }

    override suspend fun getFoodDetails(foodId: EdamamFoodId): Either<FoodError, EdamamFoodDetails> {
        val foodDetailsRequest = FoodDetailsRequest(
            listOf(
                IngredientsRequest(
                    100, "http://www.edamam.com/ontologies/edamam.owl#Measure_gram", foodId
                )
            )
        )
        return Either.catch {
            edamamFoodApi.getFoodDetails(foodDetailsRequest)
        }.mapLeft {
            if (it is Exception) FoodError.Exception(it)
            else FoodError.Unknown()
        }
    }
}