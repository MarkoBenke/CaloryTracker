package com.benke.tracker_data.repostory

import com.benke.tracker_data.local.dao.TrackerDao
import com.benke.tracker_data.mapper.toTrackableFood
import com.benke.tracker_data.mapper.toTrackedFood
import com.benke.tracker_data.mapper.toTrackedFoodEntity
import com.benke.tracker_data.remote.OpenFoodApi
import com.benke.tracker_domain.model.TrackableFood
import com.benke.tracker_domain.model.TrackedFood
import com.benke.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerDataRepositoryImpl(
    private val dao: TrackerDao,
    private val api: OpenFoodApi
) : TrackerRepository {
    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return try {
            val searchDto = api.searchFood(
                query, page, pageSize
            )
            return Result.success(
                searchDto.products
                    .filter {
                        val calculatedCalories = it.nutriments.carbohydrates100g * 4f +
                                it.nutriments.proteins100g * 4f +
                                it.nutriments.fat100g * 9f
                        val lowerBound = calculatedCalories * 0.99f
                        val upperBound = calculatedCalories * 1.01f
                        it.nutriments.energyKcal100g in (lowerBound..upperBound)

                    }
                    .mapNotNull { it.toTrackableFood() }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    override fun getFoodForDate(date: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodsForDate(date.dayOfMonth, date.monthValue, date.year).map { entities ->
            entities.map { it.toTrackedFood() }
        }
    }
}