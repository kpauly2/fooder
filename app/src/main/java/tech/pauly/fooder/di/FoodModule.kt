package tech.pauly.fooder.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tech.pauly.fooder.data.domain.DefaultFoodRepository
import tech.pauly.fooder.data.domain.FoodRepository
import tech.pauly.fooder.data.network.EdamamFoodNetwork
import tech.pauly.fooder.data.network.FoodDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class FoodModule {

    @Binds
    internal abstract fun bindsFoodRepository(
        foodRepository: DefaultFoodRepository
    ): FoodRepository

    @Binds
    internal abstract fun bindsFoodNetwork(
        foodDataSource: EdamamFoodNetwork
    ): FoodDataSource
}