package pt.dourobats.app.core.ui.components.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Modern week calendar component with horizontal scrolling.
 *
 * Displays a horizontally scrollable week view with smooth animations
 * and Material Design 3 styling. Users can swipe left/right to navigate
 * between weeks and tap dates to select them.
 *
 * ## Features
 * - Horizontal scrolling between weeks
 * - Date selection with animation
 * - Current day indicator
 * - Touch-friendly 48dp minimum targets
 * - Material Design 3 theming
 *
 * ## Usage
 *
 * ```kotlin
 * WeekCalendar(
 *     selectedDate = selectedDate,
 *     onDateSelected = { date ->
 *         selectedDate = date
 *     }
 * )
 * ```
 *
 * @param selectedDate Currently selected date
 * @param today Today's date (for highlighting)
 * @param onDateSelected Callback when a date is tapped
 * @param dayNames Map of DayOfWeek to localized short day names (defaults to English)
 * @param modifier Optional modifier for the calendar
 */
@Composable
fun WeekCalendar(
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    dayNames: Map<DayOfWeek, String> = defaultDayNames(),
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    // Generate only the 7 days of the current week
    val dates = remember(today) { generateCurrentWeekDates(today) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(4.dp), // Tiny gap between circles
        verticalAlignment = Alignment.CenterVertically
    ) {
        dates.forEach { date ->
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                DateItem(
                    date = date,
                    isSelected = date == selectedDate,
                    isToday = date == today,
                    dayNames = dayNames,
                    onDateClick = onDateSelected
                )
            }
        }
    }
}

/**
 * Individual date item in the week calendar.
 *
 * @param date The date to display
 * @param isSelected Whether this date is currently selected
 * @param isToday Whether this date is today
 * @param dayNames Map of DayOfWeek to localized short day names
 * @param onDateClick Callback when this date is clicked
 */
@Composable
private fun DateItem(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    dayNames: Map<DayOfWeek, String>,
    onDateClick: (LocalDate) -> Unit
) {
    val spacing = LocalSpacing.current

    // Animated background color
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surfaceVariant
        }
    )

    // Animated text color
    val textColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.onPrimary
            isToday -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurface
        }
    )

    // Animated scale for selection feedback
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f
    )

    Column(
        modifier = Modifier
            .size(56.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onDateClick(date) }
            .padding(spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Day of week (Mon, Tue, etc.)
        Text(
            text = dayNames[date.dayOfWeek] ?: "",
            style = MaterialTheme.typography.labelSmall,
            color = textColor.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        // Day number
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
            color = textColor,
            textAlign = TextAlign.Center
        )

        // Current day indicator dot
        if (isToday && !isSelected) {
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

/**
 * Generates a list of dates for the week view.
 *
 * Creates dates for the current week plus a specified number of weeks
 * before and after for smooth scrolling.
 *
 * @param centerDate The date to center the range around
 * @param weeksBeforeAfter Number of weeks to include before and after
 * @return List of LocalDate objects
 */
private fun generateWeekDates(
    centerDate: LocalDate,
    weeksBeforeAfter: Int = 4
): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()

    // Calculate start date (go back to start of week, then back N weeks)
    val daysFromMonday = (centerDate.dayOfWeek.ordinal) % 7
    val startOfCenterWeek = centerDate.minusDays(daysFromMonday)
    val startDate = startOfCenterWeek.minusDays(weeksBeforeAfter * 7)

    // Generate dates for (weeksBeforeAfter * 2 + 1) weeks
    val totalDays = (weeksBeforeAfter * 2 + 1) * 7

    for (i in 0 until totalDays) {
        dates.add(startDate.plusDays(i))
    }

    return dates
}

/**
 * Helper to subtract days from LocalDate.
 */
private fun LocalDate.minusDays(days: Int): LocalDate {
    return kotlinx.datetime.LocalDate.fromEpochDays(this.toEpochDays() - days)
}

/**
 * Helper to add days to LocalDate.
 */
private fun LocalDate.plusDays(days: Int): LocalDate {
    return kotlinx.datetime.LocalDate.fromEpochDays(this.toEpochDays() + days)
}


/**
 * Generates dates for the current week (Monday to Sunday).
 */
internal fun generateCurrentWeekDates(today: LocalDate): List<LocalDate> {
    // DayOfWeek.ordinal: Monday is 0, Sunday is 6
    val daysFromMonday = today.dayOfWeek.ordinal
    val startOfCurrentWeek = today.minusDays(daysFromMonday)

    // Generate exactly 7 days starting from Monday
    return (0..6).map { dayOffset ->
        startOfCurrentWeek.plusDays(dayOffset)
    }
}
