package pt.dourobats.app.core.ui.components.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Year/Month data class for calendar navigation.
 *
 * Represents a specific year and month combination, providing
 * arithmetic operations for navigating between months.
 *
 * @param year The calendar year
 * @param month The month of the year
 */
data class YearMonth(val year: Int, val month: Month) {
    /**
     * Returns a new YearMonth by subtracting the specified number of months.
     */
    fun minusMonths(months: Int): YearMonth {
        var newYear = year
        var newMonth = month.ordinal - months

        while (newMonth < 0) {
            newMonth += 12
            newYear -= 1
        }

        return YearMonth(newYear, Month.entries[newMonth])
    }

    /**
     * Returns a new YearMonth by adding the specified number of months.
     */
    fun plusMonths(months: Int): YearMonth {
        var newYear = year
        var newMonth = month.ordinal + months

        while (newMonth >= 12) {
            newMonth -= 12
            newYear += 1
        }

        return YearMonth(newYear, Month.entries[newMonth])
    }
}

/**
 * Modern month calendar component with grid layout and navigation.
 *
 * Displays a full month grid (7 columns × ~6 rows) with dates from
 * adjacent months shown in a grayed-out state. Users can navigate
 * between months using prev/next buttons.
 *
 * ## Features
 * - Full month grid (42 cells for consistent layout)
 * - Month navigation (previous/next buttons)
 * - Dates from adjacent months (grayed out, non-clickable)
 * - Date selection with animation
 * - Current day indicator
 * - Leap year handling
 * - Material Design 3 theming
 *
 * @param yearMonth The year and month to display
 * @param selectedDate Currently selected date
 * @param today Today's date (for highlighting)
 * @param onDateSelected Callback when a date is tapped
 * @param onMonthChange Callback when month navigation is triggered
 * @param monthNames Map of Month to localized month names (defaults to English)
 * @param dayNames Map of DayOfWeek to localized short day names (defaults to English)
 * @param modifier Optional modifier for the calendar
 */
@Composable
fun MonthCalendar(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    monthNames: Map<Month, String> = defaultMonthNames(),
    dayNames: Map<DayOfWeek, String> = defaultDayNames(),
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Column(modifier = modifier.fillMaxWidth()) {
        // Month header with navigation
        MonthHeader(
            yearMonth = yearMonth,
            today = today,
            monthNames = monthNames,
            onPreviousMonth = { onMonthChange(yearMonth.minusMonths(1)) },
            onNextMonth = { onMonthChange(yearMonth.plusMonths(1)) }
        )

        Spacer(modifier = Modifier.height(spacing.standard))

        // Day of week headers
        DayOfWeekHeader(dayNames = dayNames)

        Spacer(modifier = Modifier.height(spacing.small))

        // Month grid
        MonthGrid(
            yearMonth = yearMonth,
            selectedDate = selectedDate,
            today = today,
            onDateSelected = onDateSelected
        )
    }
}

/**
 * Month header with title and navigation buttons.
 */
@Composable
private fun MonthHeader(
    yearMonth: YearMonth,
    today: LocalDate,
    monthNames: Map<Month, String>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val spacing = LocalSpacing.current

    // Determine if previous month navigation should be disabled
    val currentYearMonth = YearMonth(today.year, today.month)
    val isPreviousMonthDisabled = yearMonth.year < currentYearMonth.year ||
            (yearMonth.year == currentYearMonth.year && yearMonth.month.ordinal <= currentYearMonth.month.ordinal)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.screenHorizontal),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous month button (disabled if would navigate to past)
        IconButton(
            onClick = onPreviousMonth,
            enabled = !isPreviousMonthDisabled
        ) {
            Text(
                text = "<",
                style = MaterialTheme.typography.headlineSmall,
                color = if (isPreviousMonthDisabled)
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }

        // Month and year label
        Text(
            text = "${monthNames[yearMonth.month]} ${yearMonth.year}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Next month button
        IconButton(onClick = onNextMonth) {
            Text(">", style = MaterialTheme.typography.headlineSmall)
        }
    }
}


/**
 * Day of week header row (Mon, Tue, Wed, ...).
 */
@Composable
private fun DayOfWeekHeader(dayNames: Map<DayOfWeek, String>) {
    val spacing = LocalSpacing.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.screenHorizontal),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        ).forEach { dayOfWeek ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayNames[dayOfWeek] ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


/**
 * Month grid layout (7×6 = 42 cells).
 */
@Composable
private fun MonthGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val spacing = LocalSpacing.current

    // Divide 42 dates into 6 lists of 7
    val weeks = remember(yearMonth) {
        generateMonthGridDates(yearMonth).chunked(7)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.screenHorizontal),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        weeks.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                week.forEach { date ->
                    Box(modifier = Modifier.weight(1f)) {
                        MonthDateItem(
                            date = date,
                            isSelected = date == selectedDate,
                            isToday = date == today,
                            isCurrentMonth = date.month == yearMonth.month,
                            onDateClick = onDateSelected
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual date cell in the month grid.
 */
@Composable
private fun MonthDateItem(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    isCurrentMonth: Boolean,
    onDateClick: (LocalDate) -> Unit
) {
    // Animated background color
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = if (isCurrentMonth) 1f else 0.3f
            )
        }
    )

    // Animated text color
    val textColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.onPrimary
            !isCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            isToday -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurface
        }
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f
    )

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = isCurrentMonth) { onDateClick(date) }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = date.day.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )

            if (isToday && !isSelected && isCurrentMonth) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

/**
 * Generates dates for month grid including padding from previous/next months.
 * Always returns 42 dates (6 rows × 7 columns) for consistent grid height.
 */
private fun generateMonthGridDates(yearMonth: YearMonth): List<LocalDate> {
    val firstDayOfMonth = LocalDate(yearMonth.year, yearMonth.month, 1)

    // Calculate padding from Monday
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek
    val daysFromMonday = firstDayOfWeek.ordinal

    // Start date (potentially from previous month)
    val startDate = firstDayOfMonth.minusDays(daysFromMonday)

    // Generate 42 dates (6 weeks)
    return (0 until 42).map { offset ->
        startDate.plusDays(offset)
    }
}

/**
 * Checks if a year is a leap year.
 */
private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

/**
 * Helper to subtract days from LocalDate.
 */
private fun LocalDate.minusDays(days: Int): LocalDate {
    return LocalDate.fromEpochDays(this.toEpochDays() - days)
}

/**
 * Helper to add days to LocalDate.
 */
private fun LocalDate.plusDays(days: Int): LocalDate {
    return LocalDate.fromEpochDays(this.toEpochDays() + days)
}

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
