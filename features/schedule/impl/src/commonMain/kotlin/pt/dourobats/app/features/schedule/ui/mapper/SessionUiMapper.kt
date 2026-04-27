package pt.dourobats.app.features.schedule.ui.mapper

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.logging.Logger
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.ui.SessionUiModel
import pt.dourobats.app.features.venues.api.usecase.GetVenueByIdUseCase
import kotlin.time.Duration.Companion.minutes

/**
 * Maps domain [Session] objects to [SessionUiModel] for UI rendering.
 *
 * Centralising the sport-ID → display-name/icon logic here keeps the ViewModel
 * focused on state management. As new sports are added, only this class changes.
 */
internal class SessionUiMapper(
    private val getVenueByIdUseCase: GetVenueByIdUseCase,
    private val logger: Logger,
) {

    suspend fun map(session: Session, isBooked: Boolean): SessionUiModel {
        val venueName = when (val result = getVenueByIdUseCase(session.venueId)) {
            is Result.Success -> result.data.name
            is Result.Error -> {
                logger.w("Venue not found for id='${session.venueId}': ${result.message}", tag = TAG)
                "Unknown venue"
            }
            is Result.Loading -> "Unknown venue"
        }

        return SessionUiModel(
            session = session,
            sportName = resolveSportName(session.sportId),
            venueName = venueName,
            isUserBooked = isBooked,
            formattedTimeRange = formatTimeRange(session.dateTime, session.duration.inWholeMinutes.toInt()),
            formattedStartTime = formatLocalTime(session.dateTime.time),
            formattedShortDate = "${session.dateTime.day} ${session.dateTime.month.name.take(3)}"
        )
    }

    private fun resolveSportName(sportId: String): String = when (sportId) {
        "volleyball" -> "Volleyball"
        "futsal"     -> "Futsal"
        "swimming"   -> "Swimming"
        "basketball" -> "Basketball"
        "padel"      -> "Padel"
        "running"    -> "Running"
        else         -> sportId.replaceFirstChar { it.uppercaseChar() }
    }

    private fun formatTimeRange(start: LocalDateTime, durationMin: Int): String {
        val end = start.toInstant(TimeZone.currentSystemDefault())
            .plus(durationMin.minutes)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        return "${formatLocalTime(start.time)} - ${formatLocalTime(end.time)}"
    }

    private fun formatLocalTime(time: LocalTime): String {
        return "${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}"
    }

    companion object {
        private const val TAG = "SessionUiMapper"
    }
}
