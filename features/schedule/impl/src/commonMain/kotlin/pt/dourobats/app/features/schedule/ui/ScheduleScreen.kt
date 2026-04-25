package pt.dourobats.app.features.schedule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dourobats.features.schedule.generated.resources.*
import dourobats.features.schedule.generated.resources.Res
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.ui.components.layout.AppHeader
import pt.dourobats.app.features.schedule.calendar.MonthCalendar
import pt.dourobats.app.features.schedule.calendar.WeekCalendar
import pt.dourobats.app.features.schedule.calendar.YearMonth
import pt.dourobats.app.features.schedule.calendar.rememberCalendarLocalization
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.schedule.ui.components.SessionCard
import pt.dourobats.app.features.schedule.ui.components.UpcomingBookedCard

@Composable
fun ScheduleRoute() {
    val viewModel: ScheduleViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        uiState.successMessage?.let { message ->
            scope.launch {
                snackBarHostState.showSnackbar(message)
                viewModel.clearMessages()
            }
        }
        uiState.errorMessage?.let { message ->
            scope.launch {
                snackBarHostState.showSnackbar(message)
                viewModel.clearMessages()
            }
        }
    }

    ScheduleScreen(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onAction = { action ->
            when (action) {
                is ScheduleAction.ToggleViewMode -> viewModel.toggleViewMode()
                is ScheduleAction.SelectDate -> viewModel.selectDate(action.date)
                is ScheduleAction.BookSession -> viewModel.bookSession(action.sessionId)
                is ScheduleAction.CancelBooking -> viewModel.cancelBooking(action.sessionId)
            }
        },
        onYearMonthChange = { viewModel.updateYearMonth(it) }
    )
}

@Composable
internal fun ScheduleScreen(
    uiState: ScheduleUiState,
    snackBarHostState: SnackbarHostState,
    onAction: (ScheduleAction) -> Unit,
    onYearMonthChange: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    val cal = rememberCalendarLocalization()
    val today = kotlin.time.Clock.System.todayIn(TimeZone.currentSystemDefault())

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            AppHeader(
                title = stringResource(Res.string.training_title),
                content = {
                    if (uiState.viewMode == CalendarViewMode.MONTH) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { onYearMonthChange(uiState.currentYearMonth.minusMonths(1)) }) {
                                Icon(Icons.Default.ChevronLeft, null, tint = MaterialTheme.colorScheme.onPrimary)
                            }
                            Text(
                                text = "${cal.monthNames[uiState.currentYearMonth.month]} ${uiState.currentYearMonth.year}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = spacing.medium)
                            )
                            IconButton(onClick = { onYearMonthChange(uiState.currentYearMonth.plusMonths(1)) }) {
                                Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.standard, vertical = spacing.small),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.clickable { onAction(ScheduleAction.ToggleViewMode) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (uiState.viewMode == CalendarViewMode.WEEK) Icons.Default.CalendarMonth else Icons.Default.ViewWeek,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.viewMode == CalendarViewMode.WEEK)
                                stringResource(Res.string.view_mode_month)
                            else
                                stringResource(Res.string.view_mode_week),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        when (uiState.viewMode) {
                            CalendarViewMode.WEEK -> WeekCalendar(
                                selectedDate = uiState.selectedDate,
                                today = today,
                                onDateSelected = { onAction(ScheduleAction.SelectDate(it)) },
                                dayNames = cal.dayNames,
                                sessionDates = uiState.allSessionDates,
                                todayLabel = stringResource(Res.string.schedule_today)
                            )
                            CalendarViewMode.MONTH -> MonthCalendar(
                                yearMonth = uiState.currentYearMonth,
                                selectedDate = uiState.selectedDate,
                                today = today,
                                onDateSelected = { date ->
                                    onAction(ScheduleAction.SelectDate(date))
                                },
                                dayNames = cal.dayNames,
                                sessionDates = uiState.allSessionDates,
                                todayLabel = stringResource(Res.string.schedule_today),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    item {
                        val dateStr = "${uiState.selectedDate.day} ${cal.monthNames[uiState.selectedDate.month]?.uppercase()}"
                        Text(
                            text = dateStr,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = spacing.screenHorizontal, vertical = spacing.standard)
                        )
                    }

                    if (uiState.sessionsForSelectedDate.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(Res.string.sessions_available_empty),
                                modifier = Modifier.padding(spacing.screenHorizontal),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        items(uiState.sessionsForSelectedDate) { sessionData ->
                            SessionCard(
                                sessionData = sessionData,
                                isLoading = uiState.sessionLoadingStates[sessionData.session.id] ?: false,
                                onBookSession = { onAction(ScheduleAction.BookSession(it)) },
                                onCancelBooking = { onAction(ScheduleAction.CancelBooking(it)) },
                                bookButtonText = stringResource(Res.string.session_button_book),
                                cancelButtonText = stringResource(Res.string.session_button_cancel),
                                fullButtonText = stringResource(Res.string.session_button_full),
                                bookedBadgeText = stringResource(Res.string.session_booked_badge),
                                attendingText = "" // Using stringResource in IconLabelRow inside SessionCard instead
                            )
                            Spacer(modifier = Modifier.height(spacing.small))
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(spacing.large))
                        Text(
                            text = stringResource(Res.string.sessions_my_schedule_title),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = spacing.screenHorizontal)
                        )
                        Spacer(modifier = Modifier.height(spacing.standard))
                    }

                    if (uiState.upcomingBookedSessions.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(Res.string.sessions_my_schedule_empty),
                                modifier = Modifier.padding(spacing.screenHorizontal),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        items(uiState.upcomingBookedSessions) { sessionData ->
                            UpcomingBookedCard(
                                sessionData = sessionData,
                                onCancel = { onAction(ScheduleAction.CancelBooking(it)) },
                                cancelShortText = stringResource(Res.string.session_button_cancel_short)
                            )
                            Spacer(modifier = Modifier.height(spacing.small))
                        }
                    }

                    item { Spacer(modifier = Modifier.height(spacing.huge)) }
                }
            }
        }
    }
}
