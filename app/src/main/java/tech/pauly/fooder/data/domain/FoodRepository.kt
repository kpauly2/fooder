package tech.pauly.fooder.data.domain

import arrow.core.getOrElse
import kotlinx.coroutines.flow.Flow
import tech.pauly.fooder.data.DataOperation
import tech.pauly.fooder.data.domain.model.FoodDetails
import tech.pauly.fooder.data.domain.model.FoodList
import tech.pauly.fooder.data.local.RecentSearchesDao
import tech.pauly.fooder.data.local.model.RecentSearch
import tech.pauly.fooder.data.network.EdamamFoodNetwork
import tech.pauly.fooder.data.network.model.EdamamFoodId
import javax.inject.Inject


interface FoodRepository {
    val recentSearches: Flow<List<RecentSearch>>
    suspend fun getFoodsForQuery(queryString: String): DataOperation<FoodList>
    suspend fun getFoodDetails(foodId: EdamamFoodId): DataOperation<FoodDetails>
}

class DefaultFoodRepository @Inject constructor(
    private val dataSource: EdamamFoodNetwork,
    private val recentSearchesDao: RecentSearchesDao,
) : FoodRepository {

    override val recentSearches = recentSearchesDao.getAll()

    // Get foods from Edamam API using `queryString`, returning either `FoodError` or `FoodList`
    // Saves `queryString` to local recent searches database
    override suspend fun getFoodsForQuery(queryString: String): DataOperation<FoodList> {
        val response = dataSource.getFoodList(queryString).getOrElse {
            return DataOperation.Failure(it)
        }
        val foodList = response.asFoodList().getOrElse {
            return DataOperation.Failure(it)
        }
        recentSearchesDao.insertRecentSearch(RecentSearch(searchQuery = queryString))
        return DataOperation.Success(foodList)
    }

    // get details for the `foodId` from Edamam API and return either `FoodDetails` or `FoodError`
    override suspend fun getFoodDetails(foodId: EdamamFoodId): DataOperation<FoodDetails> {
        val networkResponse = dataSource.getFoodDetails(foodId).getOrElse {
            return DataOperation.Failure(it)
        }
        val foodDetails = networkResponse.toDomainFoodDetails().getOrElse {
            return DataOperation.Failure(it)
        }
        return DataOperation.Success(foodDetails)
    }
}
