package pt.dourobats.app.features.schedule

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import dourobats.features.schedule.generated.resources.Res
import dourobats.features.schedule.generated.resources.training_coming_soon
import dourobats.features.schedule.generated.resources.training_title
import dourobats.features.schedule.generated.resources.view_mode_month
import dourobats.features.schedule.generated.resources.view_mode_week
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.schedule.components.MonthCalendar
import pt.dourobats.app.features.schedule.components.WeekCalendar
import pt.dourobats.app.features.schedule.components.YearMonth

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = spacing.standard),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen title
        Text(
            text = stringResource(Res.string.training_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = spacing.screenHorizontal)
        )

        Spacer(modifier = Modifier.height(spacing.small))

        // View Mode Toggle
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

        Spacer(modifier = Modifier.height(spacing.small))

        // Conditional calendar display
        when (viewMode) {
            CalendarViewMode.WEEK -> WeekCalendar(
                selectedDate = selectedDate,
                onDateSelected = { date -> selectedDate = date }
            )
            CalendarViewMode.MONTH -> MonthCalendar(
                yearMonth = currentYearMonth,
                selectedDate = selectedDate,
                onDateSelected = { date -> selectedDate = date },
                onMonthChange = { yearMonth -> currentYearMonth = yearMonth }
            )
        }

        Spacer(modifier = Modifier.height(spacing.large))

        // Placeholder for session list (will be implemented in DB010)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.screenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Selected: ${selectedDate}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(spacing.small))
            Text(
                text = stringResource(Res.string.training_coming_soon),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
