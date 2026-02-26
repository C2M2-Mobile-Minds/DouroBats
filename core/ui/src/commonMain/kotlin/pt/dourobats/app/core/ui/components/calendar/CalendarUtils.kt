package pt.dourobats.app.core.ui.components.calendar

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

/**
 * Default English short day names.
 */
fun defaultDayNames(): Map<DayOfWeek, String> = mapOf(
    DayOfWeek.MONDAY to "Mon",
    DayOfWeek.TUESDAY to "Tue",
    DayOfWeek.WEDNESDAY to "Wed",
    DayOfWeek.THURSDAY to "Thu",
    DayOfWeek.FRIDAY to "Fri",
    DayOfWeek.SATURDAY to "Sat",
    DayOfWeek.SUNDAY to "Sun"
)

/**
 * Default English month names.
 */
fun defaultMonthNames(): Map<Month, String> = mapOf(
    Month.JANUARY to "January",
    Month.FEBRUARY to "February",
    Month.MARCH to "March",
    Month.APRIL to "April",
    Month.MAY to "May",
    Month.JUNE to "June",
    Month.JULY to "July",
    Month.AUGUST to "August",
    Month.SEPTEMBER to "September",
    Month.OCTOBER to "October",
    Month.NOVEMBER to "November",
    Month.DECEMBER to "December"
)

/**
 * Helper to subtract days from LocalDate.
 */
fun LocalDate.minusDays(days: Int): LocalDate {
    return LocalDate.fromEpochDays(this.toEpochDays() - days)
}

/**
 * Helper to add days to LocalDate.
 */
fun LocalDate.plusDays(days: Int): LocalDate {
    return LocalDate.fromEpochDays(this.toEpochDays() + days)
}