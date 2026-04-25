package pt.dourobats.app.features.schedule.ui.mapper

import kotlinx.datetime.LocalDateTime
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.model.SessionStatus
import pt.dourobats.app.features.schedule.api.model.SkillLevel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours

class SessionUiMapperTest {

    private lateinit var mapper: SessionUiMapper

    @BeforeTest
    fun setup() {
        mapper = SessionUiMapper()
    }

    @Test
    fun `map should correctly resolve sport name and icon for volleyball`() {
        val session = createBaseSession(sportId = "volleyball")
        
        val result = mapper.map(session, isBooked = false)
        
        assertEquals("Volleyball", result.sportName)
        assertEquals("🏐", result.sportIcon)
    }

    @Test
    fun `map should correctly resolve sport name and icon for padel`() {
        val session = createBaseSession(sportId = "padel")
        
        val result = mapper.map(session, isBooked = false)
        
        assertEquals("Padel", result.sportName)
        assertEquals("🎾", result.sportIcon)
    }

    @Test
    fun `map should format time range correctly`() {
        val session = createBaseSession(
            dateTime = LocalDateTime(2026, 1, 12, 18, 0),
            duration = 1.5.hours
        )
        
        val result = mapper.map(session, isBooked = false)
        
        assertEquals("18:00 - 19:30", result.formattedTimeRange)
    }

    @Test
    fun `map should format short date correctly`() {
        val session = createBaseSession(
            dateTime = LocalDateTime(2026, 2, 15, 10, 0)
        )
        
        val result = mapper.map(session, isBooked = false)
        
        assertEquals("15 FEB", result.formattedShortDate.uppercase())
    }

    @Test
    fun `map should set isUserBooked correctly`() {
        val session = createBaseSession()
        
        val bookedResult = mapper.map(session, isBooked = true)
        val notBookedResult = mapper.map(session, isBooked = false)
        
        assertEquals(true, bookedResult.isUserBooked)
        assertEquals(false, notBookedResult.isUserBooked)
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
