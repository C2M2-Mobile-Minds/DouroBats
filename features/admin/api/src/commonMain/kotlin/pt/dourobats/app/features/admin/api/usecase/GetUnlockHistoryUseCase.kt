package pt.dourobats.app.features.admin.api.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.admin.api.model.CalendarUnlock

/**
 * Use case for retrieving the history of calendar unlocks for a sport.
 */
interface GetUnlockHistoryUseCase {
    /**
     * Gets the unlock history.
     * 
     * @param sportId The ID of the sport
     * @return Flow of list of unlock records
     */
    operator fun invoke(sportId: String): Flow<List<CalendarUnlock>>
}
