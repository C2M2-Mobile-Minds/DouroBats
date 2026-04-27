package pt.dourobats.app.features.management.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dourobats.features.management.generated.resources.Res
import dourobats.features.management.generated.resources.management_create_session_date_label
import dourobats.features.management.generated.resources.management_create_session_date_placeholder
import dourobats.features.management.generated.resources.management_create_session_location_label
import dourobats.features.management.generated.resources.management_create_session_max_athletes_label
import dourobats.features.management.generated.resources.management_create_session_name_label
import dourobats.features.management.generated.resources.management_create_session_save
import dourobats.features.management.generated.resources.management_create_session_section_details
import dourobats.features.management.generated.resources.management_create_session_section_schedule
import dourobats.features.management.generated.resources.management_create_session_time_label
import dourobats.features.management.generated.resources.management_create_session_time_placeholder
import dourobats.features.management.generated.resources.management_create_session_title
import dourobats.features.management.generated.resources.management_create_session_type_category
import dourobats.features.management.generated.resources.management_create_session_type_match
import dourobats.features.management.generated.resources.management_create_session_type_meeting
import dourobats.features.management.generated.resources.management_create_session_type_practice
import dourobats.features.management.generated.resources.management_create_session_type_social
import dourobats.features.management.generated.resources.management_create_session_type_tournament
import dourobats.features.management.generated.resources.management_create_session_type_tryout
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.components.layout.AppHeader
import pt.dourobats.app.core.ui.components.layout.SectionHeader
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.core.ui.theme.proIndigo

internal enum class SessionType { PRACTICE, MATCH, MEETING, TOURNAMENT, SOCIAL, TRYOUT }

private fun SessionType.icon(): ImageVector = when (this) {
    SessionType.PRACTICE   -> Icons.Default.FitnessCenter
    SessionType.MATCH      -> Icons.Default.EmojiEvents
    SessionType.MEETING    -> Icons.Default.Groups
    SessionType.TOURNAMENT -> Icons.Default.WorkspacePremium
    SessionType.SOCIAL     -> Icons.Default.Celebration
    SessionType.TRYOUT     -> Icons.Default.PersonSearch
}

@Composable
internal fun CreateSessionScreen(onBack: () -> Unit) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()

    var sessionName    by remember { mutableStateOf("") }
    var location       by remember { mutableStateOf("") }
    var maxAthletes    by remember { mutableStateOf("") }
    var selectedType   by remember { mutableStateOf(SessionType.PRACTICE) }
    var selectedDate   by remember { mutableStateOf("") }
    var selectedTime   by remember { mutableStateOf("") }
    var showTypePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        AppHeader(
            title = stringResource(Res.string.management_create_session_title),
            leading = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.management_create_session_title),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            },
            trailing = {
                TextButton(onClick = onBack) { // TODO: wire to real save
                    Text(
                        text = stringResource(Res.string.management_create_session_save),
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

            // ── Essential Details ──────────────────────────────────────────────
            SectionHeader(stringResource(Res.string.management_create_session_section_details))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(modifier = Modifier.padding(spacing.standard)) {

                    // Session Category trigger
                    SessionTypeSelectionCard(
                        selectedType = selectedType,
                        onClick = { showTypePicker = true },
                    )

                    Spacer(modifier = Modifier.height(spacing.standard))

                    // Session Name
                    ProTextField(
                        value = sessionName,
                        onValueChange = { sessionName = it },
                        label = stringResource(Res.string.management_create_session_name_label),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(spacing.standard))

                    // Location + Max Athletes side by side
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.standard),
                    ) {
                        ProTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = stringResource(Res.string.management_create_session_location_label),
                            modifier = Modifier.weight(1f),
                        )
                        ProTextField(
                            value = maxAthletes,
                            onValueChange = { maxAthletes = it },
                            label = stringResource(Res.string.management_create_session_max_athletes_label),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(0.6f),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacing.large))

            // ── Schedule ──────────────────────────────────────────────────────
            SectionHeader(stringResource(Res.string.management_create_session_section_schedule))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column {
                    SchedulePickerRow(
                        label = stringResource(Res.string.management_create_session_date_label),
                        value = selectedDate.ifEmpty {
                            stringResource(Res.string.management_create_session_date_placeholder)
                        },
                        icon = Icons.Default.CalendarToday,
                        onClick = { /* TODO: date picker */ },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    )
                    SchedulePickerRow(
                        label = stringResource(Res.string.management_create_session_time_label),
                        value = selectedTime.ifEmpty {
                            stringResource(Res.string.management_create_session_time_placeholder)
                        },
                        icon = Icons.Default.Schedule,
                        onClick = { /* TODO: time picker */ },
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.huge))
        }
    }

    if (showTypePicker) {
        SessionTypeBottomSheet(
            selectedType = selectedType,
            onTypeSelected = { selectedType = it; showTypePicker = false },
            onDismiss = { showTypePicker = false },
        )
    }
}

// ── ProTextField — proIndigo focus border for admin context ───────────────────

@Composable
private fun ProTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = proIndigo,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedLabelColor = proIndigo,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
    )
}

// ── SessionTypeSelectionCard — tappable trigger showing current type ──────────

@Composable
private fun SessionTypeSelectionCard(
    selectedType: SessionType,
    onClick: () -> Unit,
) {
    val spacing = LocalSpacing.current
    val label = selectedType.label()

    Column {
        Text(
            text = stringResource(Res.string.management_create_session_type_category).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp),
        )
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.standard),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.standard),
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(proIndigo.copy(alpha = 0.10f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = selectedType.icon(),
                        contentDescription = null,
                        tint = proIndigo,
                        modifier = Modifier.size(22.dp),
                    )
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                )
            }
        }
    }
}

// ── SessionTypeBottomSheet — full list of types ───────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionTypeBottomSheet(
    selectedType: SessionType,
    onTypeSelected: (SessionType) -> Unit,
    onDismiss: () -> Unit,
) {
    val spacing = LocalSpacing.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 0.dp,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = spacing.standard)
                .padding(bottom = spacing.standard),
        ) {
            Text(
                text = stringResource(Res.string.management_create_session_type_category),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(vertical = spacing.standard),
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column {
                    SessionType.entries.forEachIndexed { index, type ->
                        SessionTypeItem(
                            type = type,
                            isSelected = type == selectedType,
                            onClick = { onTypeSelected(type) },
                        )
                        if (index < SessionType.entries.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = spacing.standard),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionTypeItem(
    type: SessionType,
    isSelected: Boolean,
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
                .size(44.dp)
                .background(
                    if (isSelected) proIndigo.copy(alpha = 0.12f)
                    else MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(12.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = type.icon(),
                contentDescription = null,
                tint = if (isSelected) proIndigo else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp),
            )
        }
        Text(
            text = type.label(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) proIndigo else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = proIndigo,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

// ── SchedulePickerRow — tappable row for date / time ─────────────────────────

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

// ── Helpers ───────────────────────────────────────────────────────────────────

@Composable
private fun SessionType.label(): String = when (this) {
    SessionType.PRACTICE   -> stringResource(Res.string.management_create_session_type_practice)
    SessionType.MATCH      -> stringResource(Res.string.management_create_session_type_match)
    SessionType.MEETING    -> stringResource(Res.string.management_create_session_type_meeting)
    SessionType.TOURNAMENT -> stringResource(Res.string.management_create_session_type_tournament)
    SessionType.SOCIAL     -> stringResource(Res.string.management_create_session_type_social)
    SessionType.TRYOUT     -> stringResource(Res.string.management_create_session_type_tryout)
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun CreateSessionScreenPreview() {
    AppTheme {
        CreateSessionScreen(onBack = {})
    }
}
