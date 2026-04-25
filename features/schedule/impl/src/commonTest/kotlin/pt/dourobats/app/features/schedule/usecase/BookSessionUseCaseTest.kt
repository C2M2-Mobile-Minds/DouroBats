package pt.dourobats.app.features.schedule.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.NetworkException
import pt.dourobats.app.core.common.exception.ValidationException
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.repository.TrainingRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BookSessionUseCaseTest {

    @Test
    fun `bookSession with valid sessionId returns success`() = runTest {
        // Given
        val repository = FakeTrainingRepository(shouldSucceed = true)
        val useCase = BookSessionUseCaseImpl(repository)

        // When
        val result = useCase("session-123")

        // Then
        assertTrue(result is Result.Success)
        assertEquals("session-123", repository.lastBookedSessionId)
    }

    @Test
    fun `bookSession with blank sessionId returns validation error`() = runTest {
        // Given
        val repository = FakeTrainingRepository(shouldSucceed = true)
        val useCase = BookSessionUseCaseImpl(repository)

        // When
        val result = useCase("   ")

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is ValidationException.RequiredField)
    }

    @Test
    fun `bookSession with empty sessionId returns validation error`() = runTest {
        // Given
        val repository = FakeTrainingRepository(shouldSucceed = true)
        val useCase = BookSessionUseCaseImpl(repository)

        // When
        val result = useCase("")

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is ValidationException.RequiredField)
    }

    @Test
    fun `bookSession propagates repository errors`() = runTest {
        // Given
        val repository = FakeTrainingRepository(shouldSucceed = false)
        val useCase = BookSessionUseCaseImpl(repository)

        // When
        val result = useCase("session-123")

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is NetworkException)
    }

    /**
     * Fake implementation of TrainingRepository for testing.
     */
    private class FakeTrainingRepository(
        private val shouldSucceed: Boolean
    ) : TrainingRepository {
        var lastBookedSessionId: String? = null
        var lastCancelledSessionId: String? = null

        override fun getSessionsByDate(date: LocalDate): Flow<List<Session>> {
            return flowOf(emptyList())
        }

        override fun getAllSessions(): Flow<List<Session>> {
            return flowOf(emptyList())
        }

        override fun getUserBookedSessionIds(): Flow<Set<String>> {
            return flowOf(emptySet())
        }

        override suspend fun bookSession(sessionId: String): Result<Unit> {
            lastBookedSessionId = sessionId
            return if (shouldSucceed) {
                Result.Success(Unit)
            } else {
                Result.Error(NetworkException("Booking failed"))
            }
        }

        override suspend fun cancelBooking(sessionId: String): Result<Unit> {
            lastCancelledSessionId = sessionId
            return if (shouldSucceed) {
                Result.Success(Unit)
            } else {
                Result.Error(NetworkException("Cancellation failed"))
            }
        }

        override suspend fun getSessionById(id: String): Result<Session> {
            return Result.Error(NetworkException("Not implemented"))
        }
    }
}
