package com.benke.tracker_domain.repository

import com.benke.tracker_domain.model.TrackableFood
import com.benke.tracker_domain.model.TrackedFood
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TrackerRepository {

    suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>>

    suspend fun insertTrackedFood(food: TrackedFood)
    suspend fun deleteTrackedFood(food: TrackedFood)
    fun getFoodForDate(date: LocalDate): Flow<List<TrackedFood>>
}