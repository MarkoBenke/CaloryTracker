package com.benke.tracker_domain.usecase

data class TrackerUseCases(
    val trackFoodUseCase: TrackFoodUseCase,
    val searchFoodUseCase: SearchFoodUseCase,
    val deleteTrackedFoodUseCase: DeleteTrackedFoodUseCase,
    val getFoodsForDateUseCase: GetFoodsForDateUseCase,
    val calculateMealNutrientsUseCase: CalculateMealNutrientsUseCase
)