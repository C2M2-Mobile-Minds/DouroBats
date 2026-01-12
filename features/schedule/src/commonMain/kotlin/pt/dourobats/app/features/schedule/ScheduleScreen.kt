package pt.dourobats.app.features.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dourobats.features.schedule.generated.resources.*
import dourobats.features.schedule.generated.resources.Res
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.core.ui.components.calendar.MonthCalendar
import pt.dourobats.app.core.ui.components.calendar.WeekCalendar
import pt.dourobats.app.core.ui.components.calendar.YearMonth
import pt.dourobats.app.core.ui.components.SessionListSection

/**
 * Training schedule screen with week and month calendar views.
 *
 * Displays a switchable calendar view (week or month) for browsing dates
 * and selecting training sessions. Users can toggle between views using
 * a button in the top-right corner.
 *
 * Now uses ViewModel following clean architecture:
 * - UI observes state from ViewModel
 * - Business logic handled by use cases
 * - Ready for API integration
 */
@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = koinViewModel()
) {
    val spacing = LocalSpacing.current
    val uiState by viewModel.uiState.collectAsState()

    var currentYearMonth by remember {
        mutableStateOf(YearMonth(uiState.selectedDate.year, uiState.selectedDate.month))
    }

    // Localized calendar strings
    val dayNames = mapOf(
        DayOfWeek.MONDAY to stringResource(Res.string.day_monday_short),
        DayOfWeek.TUESDAY to stringResource(Res.string.day_tuesday_short),
        DayOfWeek.WEDNESDAY to stringResource(Res.string.day_wednesday_short),
        DayOfWeek.THURSDAY to stringResource(Res.string.day_thursday_short),
        DayOfWeek.FRIDAY to stringResource(Res.string.day_friday_short),
        DayOfWeek.SATURDAY to stringResource(Res.string.day_saturday_short),
        DayOfWeek.SUNDAY to stringResource(Res.string.day_sunday_short)
    )

    val monthNames = mapOf(
        Month.JANUARY to stringResource(Res.string.month_january),
        Month.FEBRUARY to stringResource(Res.string.month_february),
        Month.MARCH to stringResource(Res.string.month_march),
        Month.APRIL to stringResource(Res.string.month_april),
        Month.MAY to stringResource(Res.string.month_may),
        Month.JUNE to stringResource(Res.string.month_june),
        Month.JULY to stringResource(Res.string.month_july),
        Month.AUGUST to stringResource(Res.string.month_august),
        Month.SEPTEMBER to stringResource(Res.string.month_september),
        Month.OCTOBER to stringResource(Res.string.month_october),
        Month.NOVEMBER to stringResource(Res.string.month_november),
        Month.DECEMBER to stringResource(Res.string.month_december)
    )

    val today = kotlin.time.Clock.System.todayIn(TimeZone.currentSystemDefault())

    // Show loading indicator
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
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
                TextButton(onClick = { viewModel.toggleViewMode() }) {
                    Text(
                        text = if (uiState.viewMode == CalendarViewMode.WEEK)
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
            when (uiState.viewMode) {
                CalendarViewMode.WEEK -> WeekCalendar(
                    selectedDate = uiState.selectedDate,
                    today = today,
                    onDateSelected = { date -> viewModel.selectDate(date) },
                    dayNames = dayNames
                )
                CalendarViewMode.MONTH -> MonthCalendar(
                    yearMonth = currentYearMonth,
                    selectedDate = uiState.selectedDate,
                    today = today,
                    onDateSelected = { date -> viewModel.selectDate(date) },
                    onMonthChange = { yearMonth -> currentYearMonth = yearMonth },
                    monthNames = monthNames,
                    dayNames = dayNames,
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
                title = stringResource(Res.string.sessions_available_title),
                sessions = uiState.sessionsForSelectedDate,
                emptyMessage = stringResource(Res.string.sessions_available_empty),
                showDate = false,  // Don't show date - all sessions are on selected date
                bookedBadgeText = stringResource(Res.string.session_booked_badge)
            )
        }

        item {
            Spacer(modifier = Modifier.height(spacing.large))
        }

        // Section 2: User's upcoming booked sessions
        item {
            SessionListSection(
                title = stringResource(Res.string.sessions_my_schedule_title),
                sessions = uiState.upcomingBookedSessions,
                emptyMessage = stringResource(Res.string.sessions_my_schedule_empty),
                showDate = true,  // Show date - sessions can be on different dates
                bookedBadgeText = stringResource(Res.string.session_booked_badge)
            )
        }

        item {
            Spacer(modifier = Modifier.height(spacing.large))
        }
    }
}
