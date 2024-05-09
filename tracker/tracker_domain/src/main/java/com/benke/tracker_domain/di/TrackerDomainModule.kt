package di

import com.benke.core.domain.preferences.Preferences
import com.benke.tracker_domain.repository.TrackerRepository
import com.benke.tracker_domain.usecase.CalculateMealNutrientsUseCase
import com.benke.tracker_domain.usecase.DeleteTrackedFoodUseCase
import com.benke.tracker_domain.usecase.GetFoodsForDateUseCase
import com.benke.tracker_domain.usecase.SearchFoodUseCase
import com.benke.tracker_domain.usecase.TrackFoodUseCase
import com.benke.tracker_domain.usecase.TrackerUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {

    @Provides
    @ViewModelScoped
    fun provideTrackerUseCases(
        repository: TrackerRepository,
        preferences: Preferences
    ): TrackerUseCases {
        return TrackerUseCases(
            trackFoodUseCase = TrackFoodUseCase(repository),
            searchFoodUseCase = SearchFoodUseCase(repository),
            getFoodsForDateUseCase = GetFoodsForDateUseCase(repository),
            deleteTrackedFoodUseCase = DeleteTrackedFoodUseCase(repository),
            calculateMealNutrientsUseCase = CalculateMealNutrientsUseCase(preferences)
        )
    }
}