package pt.dourobats.app.core.localization

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.number

/**
 * Formats a [LocalDate] as "Mon DD, YYYY" (e.g., "Jan 06, 2026").
 *
 * **Note:** Month abbreviations are currently English-only. Future improvement:
 * convert to an `expect`/`actual` function that delegates to platform-native
 * date formatting APIs (Android: `DateTimeFormatter`, iOS: `NSDateFormatter`),
 * which are locale-aware and pick up the device language automatically.
 */
fun LocalDate.toDisplayDate(): String {
    val monthName = when (month.number) {
        1 -> "Jan"; 2 -> "Feb"; 3 -> "Mar"; 4 -> "Apr"
        5 -> "May"; 6 -> "Jun"; 7 -> "Jul"; 8 -> "Aug"
        9 -> "Sep"; 10 -> "Oct"; 11 -> "Nov"; 12 -> "Dec"
        else -> ""
    }
    return "$monthName ${dayOfMonth}, $year"
}

/**
 * Formats a [LocalTime] + duration as "HH:MM - HH:MM" (e.g., "18:00 - 20:00").
 *
 * @param durationMinutes session length in minutes
 */
fun LocalTime.toDisplayTimeRange(durationMinutes: Int): String {
    val startH = hour.toString().padStart(2, '0')
    val startM = minute.toString().padStart(2, '0')

    val totalMinutes = hour * 60 + minute + durationMinutes
    val endH = ((totalMinutes / 60) % 24).toString().padStart(2, '0')
    val endM = (totalMinutes % 60).toString().padStart(2, '0')

    return "$startH:$startM - $endH:$endM"
}
