package pt.dourobats.app.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dourobats.features.home.generated.resources.Res
import dourobats.features.home.generated.resources.home_create_session_date_label
import dourobats.features.home.generated.resources.home_create_session_date_placeholder
import dourobats.features.home.generated.resources.home_create_session_location_label
import dourobats.features.home.generated.resources.home_create_session_max_athletes_label
import dourobats.features.home.generated.resources.home_create_session_name_label
import dourobats.features.home.generated.resources.home_create_session_save
import dourobats.features.home.generated.resources.home_create_session_section_details
import dourobats.features.home.generated.resources.home_create_session_section_schedule
import dourobats.features.home.generated.resources.home_create_session_time_label
import dourobats.features.home.generated.resources.home_create_session_time_placeholder
import dourobats.features.home.generated.resources.home_create_session_title
import dourobats.features.home.generated.resources.home_create_session_type_event
import dourobats.features.home.generated.resources.home_create_session_type_label
import dourobats.features.home.generated.resources.home_create_session_type_match
import dourobats.features.home.generated.resources.home_create_session_type_practice
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.components.inputs.DouroTextField
import pt.dourobats.app.core.ui.components.layout.AppHeader
import pt.dourobats.app.core.ui.components.layout.SectionHeader
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing

internal enum class SessionType { PRACTICE, MATCH, EVENT }

@Composable
internal fun CreateSessionScreen(onBack: () -> Unit) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()

    var sessionName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var maxAthletes by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(SessionType.PRACTICE) }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppHeader(
            title = stringResource(Res.string.home_create_session_title),
            leading = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.home_create_session_title),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            },
            trailing = {
                TextButton(onClick = onBack) { // TODO: implement save
                    Text(
                        text = stringResource(Res.string.home_create_session_save),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            },
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = spacing.screenHorizontal),
        ) {
            Spacer(modifier = Modifier.height(spacing.medium))

            SessionTypePicker(
                selectedType = selectedType,
                onTypeSelected = { selectedType = it },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(spacing.large))

            SectionHeader(stringResource(Res.string.home_create_session_section_details))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(modifier = Modifier.padding(spacing.standard)) {
                    DouroTextField(
                        value = sessionName,
                        onValueChange = { sessionName = it },
                        label = stringResource(Res.string.home_create_session_name_label),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(spacing.standard))
                    DouroTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = stringResource(Res.string.home_create_session_location_label),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(spacing.standard))
                    DouroTextField(
                        value = maxAthletes,
                        onValueChange = { maxAthletes = it },
                        label = stringResource(Res.string.home_create_session_max_athletes_label),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.large))

            SectionHeader(stringResource(Res.string.home_create_session_section_schedule))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column {
                    SchedulePickerRow(
                        label = stringResource(Res.string.home_create_session_date_label),
                        value = selectedDate.ifEmpty { stringResource(Res.string.home_create_session_date_placeholder) },
                        icon = Icons.Default.CalendarToday,
                        onClick = { /* TODO: open date picker */ },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    )
                    SchedulePickerRow(
                        label = stringResource(Res.string.home_create_session_time_label),
                        value = selectedTime.ifEmpty { stringResource(Res.string.home_create_session_time_placeholder) },
                        icon = Icons.Default.Schedule,
                        onClick = { /* TODO: open time picker */ },
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.huge))
        }
    }
}

@Composable
private fun SessionTypePicker(
    selectedType: SessionType,
    onTypeSelected: (SessionType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.home_create_session_type_label).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp),
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SessionType.entries.forEachIndexed { index, type ->
                SegmentedButton(
                    selected = selectedType == type,
                    onClick = { onTypeSelected(type) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = SessionType.entries.size),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = androidx.compose.ui.graphics.Color.White,
                        inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Text(
                        text = when (type) {
                            SessionType.PRACTICE -> stringResource(Res.string.home_create_session_type_practice)
                            SessionType.MATCH -> stringResource(Res.string.home_create_session_type_match)
                            SessionType.EVENT -> stringResource(Res.string.home_create_session_type_event)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SchedulePickerRow(
    label: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = spacing.standard, vertical = spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.standard),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                    RoundedCornerShape(10.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateSessionScreenPreview() {
    AppTheme {
        CreateSessionScreen(onBack = {})
    }
}
