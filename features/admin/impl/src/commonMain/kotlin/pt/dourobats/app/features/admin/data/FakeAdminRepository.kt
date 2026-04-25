package pt.dourobats.app.features.admin.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.admin.api.model.CalendarUnlock
import pt.dourobats.app.features.admin.api.repository.AdminRepository
import pt.dourobats.app.features.schedule.api.model.Session

internal class FakeAdminRepository : AdminRepository {

    private val _unlocks = MutableStateFlow<List<CalendarUnlock>>(
        listOf(
            CalendarUnlock(
                id = "unlock-1",
                sportId = "volleyball",
                startDate = LocalDate(2026, 1, 1),
                endDate = LocalDate(2026, 1, 31),
                unlockedBy = "admin-1",
                unlockedAt = Clock.System.now()
            ),
            CalendarUnlock(
                id = "unlock-2",
                sportId = "volleyball",
                startDate = LocalDate(2026, 2, 1),
                endDate = LocalDate(2026, 2, 28),
                unlockedBy = "admin-1",
                unlockedAt = Clock.System.now()
            )
        )
    )

    private val _sessions = MutableStateFlow<List<Session>>(emptyList())

    override suspend fun unlockCalendar(
        sportId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<CalendarUnlock> {
        val newUnlock = CalendarUnlock(
            id = "unlock-${_unlocks.value.size + 1}",
            sportId = sportId,
            startDate = startDate,
            endDate = endDate,
            unlockedBy = "current-admin",
            unlockedAt = Clock.System.now()
        )
        _unlocks.value = _unlocks.value + newUnlock
        return Result.Success(newUnlock)
    }

    override fun getUnlockHistory(sportId: String): Flow<List<CalendarUnlock>> {
        return _unlocks.asStateFlow().map { list ->
            list.filter { it.sportId == sportId }
        }
    }

    override suspend fun updateAthleteLevel(
        athleteId: String,
        sportId: String,
        newLevel: String,
        reason: String
    ): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun createSession(session: Session): Result<Unit> {
        _sessions.value = _sessions.value + session
        return Result.Success(Unit)
    }

    override suspend fun updateSession(session: Session): Result<Unit> {
        _sessions.value = _sessions.value.map { existingSession ->
            if (existingSession.id == session.id) session else existingSession
        }
        return Result.Success(Unit)
    }
}
