package pt.dourobats.app.features.schedule.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import pt.dourobats.app.features.schedule.ui.components.SessionCardShimmer
import pt.dourobats.app.features.schedule.ui.components.ViewModeToggle

@Composable
internal fun ScheduleRoute() {
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
    val dateStr = remember(uiState.selectedDate, cal) {
        "${uiState.selectedDate.day} ${cal.monthNames[uiState.selectedDate.month]?.uppercase()}"
    }
    val dayOfWeekStr = remember(uiState.selectedDate, cal) {
        cal.dayNames[uiState.selectedDate.dayOfWeek]?.uppercase() ?: ""
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = uiState.selectedDate != today,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                SmallFloatingActionButton(
                    onClick = { onAction(ScheduleAction.SelectDate(today)) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                ) {
                    Icon(Icons.Default.Today, contentDescription = "Back to today")
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                ViewModeToggle(
                    viewMode = uiState.viewMode,
                    onToggle = { onAction(ScheduleAction.ToggleViewMode) }
                )
            }

            if (uiState.isLoading) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = spacing.screenHorizontal,
                        vertical = spacing.standard,
                    ),
                    verticalArrangement = Arrangement.spacedBy(spacing.small),
                ) {
                    items(5) { SessionCardShimmer() }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                ) {
                    item {
                        AnimatedContent(
                            targetState = uiState.viewMode,
                            transitionSpec = {
                                (fadeIn(tween(300)) + expandVertically(animationSpec = tween(300)))
                                    .togetherWith(fadeOut(tween(250)) + shrinkVertically(animationSpec = tween(250)))
                            },
                        ) { mode ->
                            when (mode) {
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
                                    onDateSelected = { onAction(ScheduleAction.SelectDate(it)) },
                                    dayNames = cal.dayNames,
                                    sessionDates = uiState.allSessionDates,
                                    todayLabel = stringResource(Res.string.schedule_today),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    stickyHeader {
                        Surface(
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = spacing.screenHorizontal,
                                        vertical = spacing.standard,
                                    ),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(width = 4.dp, height = 16.dp)
                                            .background(
                                                MaterialTheme.colorScheme.primary,
                                                CircleShape,
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(spacing.small))
                                    Text(
                                        text = dayOfWeekStr,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.primary,
                                        letterSpacing = 1.sp,
                                    )
                                    Spacer(modifier = Modifier.width(spacing.small))
                                    Text(
                                        text = dateStr,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                )
                            }
                        }
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
                                isCompact = false,
                                isLoading = uiState.sessionLoadingStates[sessionData.session.id] ?: false,
                                onBook = { onAction(ScheduleAction.BookSession(it)) },
                                onCancel = { onAction(ScheduleAction.CancelBooking(it)) },
                                modifier = Modifier.padding(horizontal = spacing.screenHorizontal),
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
                            SessionCard(
                                sessionData = sessionData,
                                isCompact = true,
                                isLoading = uiState.sessionLoadingStates[sessionData.session.id] ?: false,
                                onBook = {},
                                onCancel = { onAction(ScheduleAction.CancelBooking(it)) },
                                modifier = Modifier.padding(horizontal = spacing.screenHorizontal),
                            )
                            Spacer(modifier = Modifier.height(spacing.small))
                        }
                    }

                    item { Spacer(modifier = Modifier.height(spacing.large)) }
                }
            }
        }
    }
}
