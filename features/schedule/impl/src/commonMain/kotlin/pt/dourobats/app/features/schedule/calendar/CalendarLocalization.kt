package pt.dourobats.app.features.schedule.calendar

import androidx.compose.runtime.Composable
import dourobats.features.schedule.generated.resources.Res
import dourobats.features.schedule.generated.resources.*
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource

internal data class CalendarLocalization(
    val dayNames: Map<DayOfWeek, String>,
    val monthNames: Map<Month, String>
)

@Composable
internal fun rememberCalendarLocalization(): CalendarLocalization {
    return CalendarLocalization(
        dayNames = mapOf(
            DayOfWeek.MONDAY to stringResource(Res.string.day_monday_short),
            DayOfWeek.TUESDAY to stringResource(Res.string.day_tuesday_short),
            DayOfWeek.WEDNESDAY to stringResource(Res.string.day_wednesday_short),
            DayOfWeek.THURSDAY to stringResource(Res.string.day_thursday_short),
            DayOfWeek.FRIDAY to stringResource(Res.string.day_friday_short),
            DayOfWeek.SATURDAY to stringResource(Res.string.day_saturday_short),
            DayOfWeek.SUNDAY to stringResource(Res.string.day_sunday_short)
        ),
        monthNames = mapOf(
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
    )
}
