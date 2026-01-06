package pt.dourobats.app.features.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dourobats.features.schedule.generated.resources.Res
import dourobats.features.schedule.generated.resources.view_mode_month
import dourobats.features.schedule.generated.resources.view_mode_week
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.schedule.components.MonthCalendar
import pt.dourobats.app.features.schedule.components.SessionListSection
import pt.dourobats.app.features.schedule.components.WeekCalendar
import pt.dourobats.app.features.schedule.components.YearMonth
import pt.dourobats.app.features.schedule.data.MockSessionData
import pt.dourobats.app.features.schedule.data.toDisplayData

/**
 * Calendar view mode enum.
 */
enum class CalendarViewMode {
    WEEK,
    MONTH
}

/**
 * Training schedule screen with week and month calendar views.
 *
 * Displays a switchable calendar view (week or month) for browsing dates
 * and selecting training sessions. Users can toggle between views using
 * a button in the top-right corner.
 */
@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    var selectedDate by remember {
        mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    }
    var viewMode by remember { mutableStateOf(CalendarViewMode.WEEK) }
    var currentYearMonth by remember {
        mutableStateOf(YearMonth(selectedDate.year, selectedDate.month))
    }

    // Load mock data
    val allSessions = remember { MockSessionData.generateMockSessions() }
    val bookedIds = remember { MockSessionData.getUserBookedSessionIds() }
    val sessionsWithData = remember(allSessions, bookedIds) {
        allSessions.toDisplayData(bookedIds)
    }

    // Filter sessions for selected date
    val selectedDateSessions = remember(sessionsWithData, selectedDate) {
        sessionsWithData.filter { it.session.dateTime.date == selectedDate }
    }

    // Filter user's upcoming booked sessions
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val upcomingSessions = remember(sessionsWithData, today) {
        sessionsWithData.filter {
            it.isUserBooked && it.session.dateTime.date >= today
        }.sortedBy { it.session.dateTime }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = spacing.standard),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // View Mode Toggle
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.screenHorizontal),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    viewMode = if (viewMode == CalendarViewMode.WEEK)
                        CalendarViewMode.MONTH
                    else
                        CalendarViewMode.WEEK
                }) {
                    Text(
                        text = if (viewMode == CalendarViewMode.WEEK)
                            stringResource(Res.string.view_mode_month)
                        else
                            stringResource(Res.string.view_mode_week),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(spacing.small))
        }

        // Conditional calendar display
        item {
            when (viewMode) {
                CalendarViewMode.WEEK -> WeekCalendar(
                    selectedDate = selectedDate,
                    onDateSelected = { date -> selectedDate = date }
                )
                CalendarViewMode.MONTH -> MonthCalendar(
                    yearMonth = currentYearMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { date -> selectedDate = date },
                    onMonthChange = { yearMonth -> currentYearMonth = yearMonth },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(spacing.large))
        }

        // Section 1: Available sessions on selected date
        item {
            SessionListSection(
                title = "Available Sessions",
                sessions = selectedDateSessions,
                emptyMessage = "No sessions available on this date"
            )
        }

        item {
            Spacer(modifier = Modifier.height(spacing.large))
        }

        // Section 2: User's upcoming booked sessions
        item {
            SessionListSection(
                title = "My Schedule",
                sessions = upcomingSessions,
                emptyMessage = "You have no upcoming sessions booked"
            )
        }

        item {
            Spacer(modifier = Modifier.height(spacing.large))
        }
    }
}
