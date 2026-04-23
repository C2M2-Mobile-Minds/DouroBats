package pt.dourobats.app.features.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dourobats.features.schedule.generated.resources.*
import dourobats.features.schedule.generated.resources.Res
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.ui.components.DetailRow
import pt.dourobats.app.core.ui.components.StatusBadge
import pt.dourobats.app.core.ui.components.AppHeader
import pt.dourobats.app.core.ui.components.calendar.MonthCalendar
import pt.dourobats.app.core.ui.components.calendar.WeekCalendar
import pt.dourobats.app.core.ui.components.calendar.YearMonth
import pt.dourobats.app.core.ui.model.SessionDisplayData
import pt.dourobats.app.core.ui.theme.LocalSpacing

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
    )
}

@Composable
internal fun ScheduleScreen(
    uiState: ScheduleUiState,
    snackBarHostState: SnackbarHostState,
    onAction: (ScheduleAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    val cal = rememberCalendarLocalization()
    val today = kotlin.time.Clock.System.todayIn(TimeZone.currentSystemDefault())

    var currentYearMonth by remember {
        mutableStateOf(YearMonth(uiState.selectedDate.year, uiState.selectedDate.month))
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLow) // Section layer for base background
        ) {
            // Universal Header matching design
            AppHeader(
                title = stringResource(Res.string.training_title),
                content = {
                    // Month Selector (Centered below title) — only in MONTH view
                    if (uiState.viewMode == CalendarViewMode.MONTH) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { currentYearMonth = currentYearMonth.minusMonths(1) }) {
                                Icon(Icons.Default.ChevronLeft, null, tint = MaterialTheme.colorScheme.onPrimary)
                            }
                            Text(
                                text = "${cal.monthNames[currentYearMonth.month]} ${currentYearMonth.year}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = spacing.medium)
                            )
                            IconButton(onClick = { currentYearMonth = currentYearMonth.plusMonths(1) }) {
                                Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                }
            )

            // View mode toggle — outside the header, right-aligned
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.standard, vertical = spacing.small),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
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
                                yearMonth = currentYearMonth,
                                selectedDate = uiState.selectedDate,
                                today = today,
                                onDateSelected = { date ->
                                    if (date.year != currentYearMonth.year || date.month != currentYearMonth.month) {
                                        currentYearMonth = YearMonth(date.year, date.month)
                                    }
                                    onAction(ScheduleAction.SelectDate(date))                                },
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
                            SessionScheduleCard(
                                sessionData = sessionData,
                                isLoading = uiState.sessionLoadingStates[sessionData.session.id] ?: false,
                                onBook = { onAction(ScheduleAction.BookSession(it)) },
                                onCancel = { onAction(ScheduleAction.CancelBooking(it)) }
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
                                onCancel = { onAction(ScheduleAction.CancelBooking(it)) }
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

@Composable
private fun SessionScheduleCard(
    sessionData: SessionDisplayData,
    isLoading: Boolean,
    onBook: (String) -> Unit,
    onCancel: (String) -> Unit
) {
    val spacing = LocalSpacing.current
    val session = sessionData.session
    val isFull = session.currentAttendees >= session.capacity

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.screenHorizontal),
        shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest), // Component layer - "Active Card"
        elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
    ) {
        Column(modifier = Modifier.padding(spacing.standard)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(sessionData.sportIcon, fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(spacing.standard))
                    Column {
                        Text(
                            text = sessionData.sportName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Coach Pedro", // Adjusted to match design
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                if (sessionData.isUserBooked) {
                    StatusBadge(
                        text = stringResource(Res.string.session_booked_badge),
                        chipType = pt.dourobats.app.core.ui.components.ChipType.ATTENDING
                    )
                } else if (isFull) {
                    StatusBadge(
                        text = stringResource(Res.string.session_full_badge),
                        chipType = pt.dourobats.app.core.ui.components.ChipType.FULL
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.standard))

            DetailRow(icon = Icons.Default.AccessTime, text = formatTimeRange(session.dateTime, session.duration.inWholeMinutes.toInt()))
            DetailRow(icon = Icons.Default.LocationOn, text = sessionData.venueName)
            DetailRow(icon = Icons.Default.Groups, text = stringResource(Res.string.session_participants_label, session.currentAttendees, session.capacity))

            Spacer(modifier = Modifier.height(spacing.standard))

            when {
                isLoading -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator(Modifier.size(24.dp)) }
                sessionData.isUserBooked -> {
                    OutlinedButton(
                        onClick = { onCancel(session.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp) // rounded-md for serious athletic tone
                    ) {
                        Text(stringResource(Res.string.session_button_cancel))
                    }
                }
                isFull -> {
                    Button(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
                        colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    ) {
                        Text(stringResource(Res.string.session_button_full), color = Color.White)
                    }
                }
                else -> {
                    Button(
                        onClick = { onBook(session.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer) // Clay Orange for high-energy action
                    ) {
                        Text(
                            text = stringResource(Res.string.session_button_book),
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UpcomingBookedCard(
    sessionData: SessionDisplayData,
    onCancel: (String) -> Unit
) {
    val spacing = LocalSpacing.current
    val session = sessionData.session
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.screenHorizontal),
        shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), // Pitch Green for booked sessions
        elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
    ) {
        Row(
            modifier = Modifier.padding(spacing.standard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(sessionData.sportIcon, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(spacing.standard))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sessionData.sportName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "${session.dateTime.day} ${session.dateTime.month.name.take(3)} • ${formatLocalTime(session.dateTime.time)} • ${sessionData.venueName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(spacing.small))
                Surface(
                    shape = RoundedCornerShape(6.dp), // rounded-md consistency
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    modifier = Modifier.clickable { onCancel(session.id) }
                ) {
                    Text(
                        text = stringResource(Res.string.session_button_cancel_short),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

private fun formatTimeRange(start: LocalDateTime, durationMin: Int): String {
    val end = start.toInstant(TimeZone.currentSystemDefault()).plus(durationMin.toLong() * 60, kotlinx.datetime.DateTimeUnit.SECOND).toLocalDateTime(TimeZone.currentSystemDefault())
    return "${formatLocalTime(start.time)} - ${formatLocalTime(end.time)}"
}

private fun formatLocalTime(time: LocalTime): String {
    return "${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}"
}
