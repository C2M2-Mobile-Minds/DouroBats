package pt.dourobats.app.features.schedule.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalendarUtilsTest {

    @Test
    fun `getWeekStart returns Monday for a date on Monday`() {
        // Arrange - Monday, January 6, 2026
        val monday = LocalDate(2026, 1, 5)  // This is a Monday

        // Act
        val weekStart = CalendarUtils.getWeekStart(monday)

        // Assert
        assertEquals(monday, weekStart, "Monday should be its own week start")
        assertEquals(DayOfWeek.MONDAY, weekStart.dayOfWeek)
    }

    @Test
    fun `getWeekStart returns Monday for a date on Tuesday`() {
        // Arrange - Tuesday, January 6, 2026
        val tuesday = LocalDate(2026, 1, 6)

        // Act
        val weekStart = CalendarUtils.getWeekStart(tuesday)

        // Assert
        assertEquals(LocalDate(2026, 1, 5), weekStart, "Should return previous Monday")
        assertEquals(DayOfWeek.MONDAY, weekStart.dayOfWeek)
    }

    @Test
    fun `getWeekStart returns Monday for a date on Sunday`() {
        // Arrange - Sunday, January 11, 2026
        val sunday = LocalDate(2026, 1, 11)

        // Act
        val weekStart = CalendarUtils.getWeekStart(sunday)

        // Assert
        assertEquals(LocalDate(2026, 1, 5), weekStart, "Should return Monday of that week")
        assertEquals(DayOfWeek.MONDAY, weekStart.dayOfWeek)
    }

    @Test
    fun `getWeekDates returns 7 days starting from Monday`() {
        // Arrange - Any date in January 2026
        val date = LocalDate(2026, 1, 8)  // Wednesday

        // Act
        val weekDates = CalendarUtils.getWeekDates(date)

        // Assert
        assertEquals(7, weekDates.size, "Should return 7 days")
        assertEquals(DayOfWeek.MONDAY, weekDates.first().dayOfWeek, "Should start with Monday")
        assertEquals(DayOfWeek.SUNDAY, weekDates.last().dayOfWeek, "Should end with Sunday")
    }

    @Test
    fun `getWeekDates returns consecutive days`() {
        // Arrange
        val date = LocalDate(2026, 1, 8)

        // Act
        val weekDates = CalendarUtils.getWeekDates(date)

        // Assert
        for (i in 0 until weekDates.size - 1) {
            val diff = weekDates[i + 1].toEpochDays() - weekDates[i].toEpochDays()
            assertEquals(1, diff, "Days should be consecutive")
        }
    }

    @Test
    fun `getWeekDates for Monday returns same Monday as start`() {
        // Arrange
        val monday = LocalDate(2026, 1, 5)

        // Act
        val weekDates = CalendarUtils.getWeekDates(monday)

        // Assert
        assertEquals(monday, weekDates.first(), "Monday should be first day of its week")
    }

    @Test
    fun `getMonthGridStart returns Monday for month starting on Monday`() {
        // Arrange - January 2024 starts on Monday
        val year = 2024
        val month = 1

        // Act
        val gridStart = CalendarUtils.getMonthGridStart(year, month)

        // Assert
        assertEquals(DayOfWeek.MONDAY, gridStart.dayOfWeek)
        assertEquals(LocalDate(2024, 1, 1), gridStart)
    }

    @Test
    fun `getMonthGridStart returns Monday for month starting on Wednesday`() {
        // Arrange - January 2026 starts on Thursday
        val year = 2026
        val month = 1

        // Act
        val gridStart = CalendarUtils.getMonthGridStart(year, month)

        // Assert
        assertEquals(DayOfWeek.MONDAY, gridStart.dayOfWeek, "Grid should start on Monday")
        assertTrue(gridStart < LocalDate(2026, 1, 1), "Grid start should be before month start")
    }

    @Test
    fun `getMonthGridDates returns at least 28 dates`() {
        // Arrange - February 2026
        val year = 2026
        val month = 2

        // Act
        val gridDates = CalendarUtils.getMonthGridDates(year, month)

        // Assert
        assertTrue(gridDates.size >= 28, "Should have at least 28 dates for February")
    }

    @Test
    fun `getMonthGridDates returns multiples of 7 dates`() {
        // Arrange - Any month
        val year = 2026
        val month = 6

        // Act
        val gridDates = CalendarUtils.getMonthGridDates(year, month)

        // Assert
        assertEquals(0, gridDates.size % 7, "Grid dates should be multiples of 7 (complete weeks)")
    }

    @Test
    fun `getMonthGridDates starts on Monday`() {
        // Arrange
        val year = 2026
        val month = 3

        // Act
        val gridDates = CalendarUtils.getMonthGridDates(year, month)

        // Assert
        assertEquals(DayOfWeek.MONDAY, gridDates.first().dayOfWeek, "Grid should start on Monday")
    }

    @Test
    fun `getMonthGridDates ends on Sunday`() {
        // Arrange
        val year = 2026
        val month = 5

        // Act
        val gridDates = CalendarUtils.getMonthGridDates(year, month)

        // Assert
        assertEquals(DayOfWeek.SUNDAY, gridDates.last().dayOfWeek, "Grid should end on Sunday")
    }

    @Test
    fun `getMonthGridDates includes all days of the month`() {
        // Arrange - January 2026 has 31 days
        val year = 2026
        val month = 1

        // Act
        val gridDates = CalendarUtils.getMonthGridDates(year, month)

        // Assert
        val januaryDates = gridDates.filter { it.monthNumber == 1 && it.year == 2026 }
        assertEquals(31, januaryDates.size, "Should include all 31 days of January")
    }

    @Test
    fun `getMonthGridDates handles February in leap year`() {
        // Arrange - February 2024 (leap year)
        val year = 2024
        val month = 2

        // Act
        val gridDates = CalendarUtils.getMonthGridDates(year, month)

        // Assert
        val februaryDates = gridDates.filter { it.monthNumber == 2 && it.year == 2024 }
        assertEquals(29, februaryDates.size, "Should include 29 days for leap year February")
    }

    @Test
    fun `getMonthGridDates handles February in non-leap year`() {
        // Arrange - February 2026 (non-leap year)
        val year = 2026
        val month = 2

        // Act
        val gridDates = CalendarUtils.getMonthGridDates(year, month)

        // Assert
        val februaryDates = gridDates.filter { it.monthNumber == 2 && it.year == 2026 }
        assertEquals(28, februaryDates.size, "Should include 28 days for non-leap year February")
    }

    @Test
    fun `isDateInMonth returns true for date in month`() {
        // Arrange
        val date = LocalDate(2026, 1, 15)

        // Act & Assert
        assertTrue(CalendarUtils.isDateInMonth(date, 2026, 1), "Date should be in January 2026")
    }

    @Test
    fun `isDateInMonth returns false for date in different month`() {
        // Arrange
        val date = LocalDate(2026, 1, 15)

        // Act & Assert
        assertFalse(CalendarUtils.isDateInMonth(date, 2026, 2), "Date should not be in February")
    }

    @Test
    fun `isDateInMonth returns false for date in different year`() {
        // Arrange
        val date = LocalDate(2026, 1, 15)

        // Act & Assert
        assertFalse(CalendarUtils.isDateInMonth(date, 2025, 1), "Date should not be in January 2025")
    }

    @Test
    fun `isDateInMonth returns true for first day of month`() {
        // Arrange
        val date = LocalDate(2026, 6, 1)

        // Act & Assert
        assertTrue(CalendarUtils.isDateInMonth(date, 2026, 6), "First day should be in the month")
    }

    @Test
    fun `isDateInMonth returns true for last day of month`() {
        // Arrange
        val date = LocalDate(2026, 6, 30)

        // Act & Assert
        assertTrue(CalendarUtils.isDateInMonth(date, 2026, 6), "Last day should be in the month")
    }
}
