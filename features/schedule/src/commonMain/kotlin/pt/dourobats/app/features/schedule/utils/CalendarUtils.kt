package pt.dourobats.app.features.schedule.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus

/**
 * Calendar utility functions for date calculations.
 */
object CalendarUtils {

    /**
     * Gets the start date of the week containing the given date.
     * Week starts on Monday.
     *
     * @param date The date to find the week start for
     * @return The Monday of the week containing the given date
     */
    fun getWeekStart(date: LocalDate): LocalDate {
        val dayOfWeek = date.dayOfWeek
        val daysFromMonday = dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber
        return date.minus(daysFromMonday, DateTimeUnit.DAY)
    }

    /**
     * Gets all dates in the week containing the given date.
     * Week starts on Monday and ends on Sunday.
     *
     * @param date Any date in the week
     * @return List of 7 dates from Monday to Sunday
     */
    fun getWeekDates(date: LocalDate): List<LocalDate> {
        val monday = getWeekStart(date)
        return (0..6).map { monday.plus(it, DateTimeUnit.DAY) }
    }

    /**
     * Gets the first day to display in a month calendar grid.
     * This is the Monday of the week containing the first day of the month.
     *
     * @param year The year
     * @param month The month (1-12)
     * @return The first date to display in the calendar grid
     */
    fun getMonthGridStart(year: Int, month: Int): LocalDate {
        val firstDayOfMonth = LocalDate(year, month, 1)
        return getWeekStart(firstDayOfMonth)
    }

    /**
     * Gets all dates to display in a month calendar grid.
     * Includes days from previous/next months to fill complete weeks.
     *
     * @param year The year
     * @param month The month (1-12)
     * @return List of 35 or 42 dates covering complete weeks
     */
    fun getMonthGridDates(year: Int, month: Int): List<LocalDate> {
        val firstDayOfMonth = LocalDate(year, month, 1)
        // Get the last day by going to next month's first day and subtracting one day
        val nextMonth = if (month == 12) LocalDate(year + 1, 1, 1) else LocalDate(year, month + 1, 1)
        val lastDayOfMonth = nextMonth.minus(1, DateTimeUnit.DAY)

        val gridStart = getWeekStart(firstDayOfMonth)
        val gridEnd = getWeekStart(lastDayOfMonth).plus(6, DateTimeUnit.DAY)

        val totalDays = gridEnd.toEpochDays() - gridStart.toEpochDays() + 1

        return List(totalDays.toInt()) { day ->
            gridStart.plus(day, DateTimeUnit.DAY)
        }
    }

    /**
     * Checks if a year is a leap year.
     */
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    /**
     * Checks if a date belongs to the given month.
     *
     * @param date The date to check
     * @param year The target year
     * @param month The target month
     * @return true if the date is in the given month, false otherwise
     */
    fun isDateInMonth(date: LocalDate, year: Int, month: Int): Boolean {
        return date.year == year && date.month.number == month
    }
}
