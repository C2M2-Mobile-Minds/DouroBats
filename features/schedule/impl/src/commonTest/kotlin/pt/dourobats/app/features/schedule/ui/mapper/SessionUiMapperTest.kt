package pt.dourobats.app.features.schedule.ui.mapper

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.testing.logging.FakeLogger
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.model.SessionStatus
import pt.dourobats.app.features.schedule.api.model.SkillLevel
import pt.dourobats.app.features.venues.api.model.Venue
import pt.dourobats.app.features.venues.testing.FakeGetVenueByIdUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class SessionUiMapperTest {

    private lateinit var mapper: SessionUiMapper

    private val fakeGetVenueById = FakeGetVenueByIdUseCase().apply {
        resultProvider = { id -> Result.Success(Venue(id = id, name = id, address = "", capacity = 0, sportIds = emptyList())) }
    }

    private val fakeLogger = FakeLogger()

    @BeforeTest
    fun setup() {
        mapper = SessionUiMapper(fakeGetVenueById, fakeLogger)
    }

    @Test
    fun `map should correctly resolve sport name for volleyball`() = runTest {
        val session = createBaseSession(sportId = "volleyball")

        val result = mapper.map(session, isBooked = false)

        assertEquals("Volleyball", result.sportName)
    }

    @Test
    fun `map should correctly resolve sport name for padel`() = runTest {
        val session = createBaseSession(sportId = "padel")

        val result = mapper.map(session, isBooked = false)

        assertEquals("Padel", result.sportName)
    }

    @Test
    fun `map should format time range correctly`() = runTest {
        val session = createBaseSession(
            dateTime = LocalDateTime(2026, 1, 12, 18, 0),
            duration = 1.5.hours
        )

        val result = mapper.map(session, isBooked = false)

        assertEquals("18:00 - 19:30", result.formattedTimeRange)
        assertEquals("18:00", result.formattedStartTime)
    }

    @Test
    fun `map should format short date correctly`() = runTest {
        val session = createBaseSession(
            dateTime = LocalDateTime(2026, 2, 15, 10, 0)
        )

        val result = mapper.map(session, isBooked = false)

        assertEquals("15 FEB", result.formattedShortDate.uppercase())
    }

    @Test
    fun `map should set isUserBooked correctly`() = runTest {
        val session = createBaseSession()

        val bookedResult = mapper.map(session, isBooked = true)
        val notBookedResult = mapper.map(session, isBooked = false)

        assertEquals(true, bookedResult.isUserBooked)
        assertEquals(false, notBookedResult.isUserBooked)
    }

    @Test
    fun `map should use 'Unknown venue' when venue lookup fails`() = runTest {
        val errorFake = FakeGetVenueByIdUseCase().apply {
            result = Result.Error(RuntimeException("Not found"))
        }
        val errorMapper = SessionUiMapper(getVenueByIdUseCase = errorFake, logger = fakeLogger)
        val result = errorMapper.map(createBaseSession(), isBooked = false)
        assertEquals("Unknown venue", result.venueName)
    }

    @Test
    fun `map should use 'Unknown venue' when venue lookup returns Loading`() = runTest {
        val loadingFake = FakeGetVenueByIdUseCase().apply {
            result = Result.Loading()
        }
        val loadingMapper = SessionUiMapper(getVenueByIdUseCase = loadingFake, logger = fakeLogger)
        val result = loadingMapper.map(createBaseSession(), isBooked = false)
        assertEquals("Unknown venue", result.venueName)
    }

    @Test
    fun `map should resolve venue name on successful lookup`() = runTest {
        val result = mapper.map(createBaseSession(), isBooked = false)
        // fakeGetVenueById returns Venue(name = id), venueId = "Pavilhão Municipal"
        assertEquals("Pavilhão Municipal", result.venueName)
    }

    @Test
    fun `map should correctly resolve sport name for futsal`() = runTest {
        val result = mapper.map(createBaseSession(sportId = "futsal"), isBooked = false)
        assertEquals("Futsal", result.sportName)
    }

    @Test
    fun `map should correctly resolve sport name for swimming`() = runTest {
        val result = mapper.map(createBaseSession(sportId = "swimming"), isBooked = false)
        assertEquals("Swimming", result.sportName)
    }

    @Test
    fun `map should correctly resolve sport name for basketball`() = runTest {
        val result = mapper.map(createBaseSession(sportId = "basketball"), isBooked = false)
        assertEquals("Basketball", result.sportName)
    }

    @Test
    fun `map should correctly resolve sport name for running`() = runTest {
        val result = mapper.map(createBaseSession(sportId = "running"), isBooked = false)
        assertEquals("Running", result.sportName)
    }

    @Test
    fun `map should capitalize sport name for unknown sport id`() = runTest {
        val result = mapper.map(createBaseSession(sportId = "tennis"), isBooked = false)
        assertEquals("Tennis", result.sportName)
    }

    @Test
    fun `map should zero-pad hours and minutes in time range`() = runTest {
        val session = createBaseSession(
            dateTime = LocalDateTime(2026, 1, 12, 9, 5),
            duration = 55.minutes,
        )
        val result = mapper.map(session, isBooked = false)
        assertEquals("09:05 - 10:00", result.formattedTimeRange)
        assertEquals("09:05", result.formattedStartTime)
    }

    @Test
    fun `map should format time range correctly when crossing midnight`() = runTest {
        val session = createBaseSession(
            dateTime = LocalDateTime(2026, 1, 23, 23, 0),
            duration = 2.hours,
        )
        val result = mapper.map(session, isBooked = false)
        assertEquals("23:00 - 01:00", result.formattedTimeRange)
        assertEquals("23:00", result.formattedStartTime)
    }

    @Test
    fun `map should format short date correctly for December`() = runTest {
        val session = createBaseSession(dateTime = LocalDateTime(2026, 12, 31, 10, 0))
        val result = mapper.map(session, isBooked = false)
        assertEquals("31 DEC", result.formattedShortDate.uppercase())
    }

    private fun createBaseSession(
        sportId: String = "volleyball",
        dateTime: LocalDateTime = LocalDateTime(2026, 1, 12, 18, 0),
        duration: kotlin.time.Duration = 2.hours
    ) = Session(
        id = "test-id",
        sportId = sportId,
        dateTime = dateTime,
        duration = duration,
        venueId = "Pavilhão Municipal",
        targetLevel = SkillLevel.INTERMEDIATE,
        capacity = 20,
        currentAttendees = 10,
        status = SessionStatus.SCHEDULED
    )
}
