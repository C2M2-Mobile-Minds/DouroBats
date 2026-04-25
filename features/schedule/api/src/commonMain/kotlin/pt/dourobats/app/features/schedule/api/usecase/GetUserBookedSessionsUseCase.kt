package pt.dourobats.app.features.schedule.api.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import pt.dourobats.app.features.schedule.api.model.Session

/**
 * Use case for retrieving user's booked sessions.
 * Returns sessions sorted by date/time.
 * Filtering for upcoming sessions can be done by the presentation layer.
 */
interface GetUserBookedSessionsUseCase {
    /**
     * Get user's booked sessions filtered by date.
     * Returns a Flow that emits whenever sessions or bookings change.
     * Sessions are sorted by date/time and filtered to be on or after the given date.
     *
     * @param fromDate Filter sessions from this date onwards
     * @return Flow of booked sessions (from the specified date onwards)
     */
    operator fun invoke(fromDate: LocalDate): Flow<List<Session>>
}
