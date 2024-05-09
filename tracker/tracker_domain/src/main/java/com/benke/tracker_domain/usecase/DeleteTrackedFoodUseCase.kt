package com.benke.tracker_domain.usecase

import com.benke.tracker_domain.model.TrackedFood
import com.benke.tracker_domain.repository.TrackerRepository

class DeleteTrackedFoodUseCase(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(food: TrackedFood) {
        repository.deleteTrackedFood(food)
    }
}