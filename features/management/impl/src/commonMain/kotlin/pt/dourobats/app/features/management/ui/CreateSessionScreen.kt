package pt.dourobats.app.features.management.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import dourobats.features.management.generated.resources.management_create_session_coach_label
import dourobats.features.management.generated.resources.management_create_session_date_label
import dourobats.features.management.generated.resources.management_create_session_date_placeholder
import dourobats.features.management.generated.resources.management_create_session_date_required
import dourobats.features.management.generated.resources.management_create_session_duration_required
import dourobats.features.management.generated.resources.management_create_session_duration_section
import dourobats.features.management.generated.resources.management_create_session_level_advanced
import dourobats.features.management.generated.resources.management_create_session_level_all
import dourobats.features.management.generated.resources.management_create_session_level_beginner
import dourobats.features.management.generated.resources.management_create_session_level_intermediate
import dourobats.features.management.generated.resources.management_create_session_level_required
import dourobats.features.management.generated.resources.management_create_session_level_section
import dourobats.features.management.generated.resources.management_create_session_max_athletes_label
import dourobats.features.management.generated.resources.management_create_session_name_label
import dourobats.features.management.generated.resources.management_create_session_name_required
import dourobats.features.management.generated.resources.management_create_session_obs_label
import dourobats.features.management.generated.resources.management_create_session_obs_placeholder
import dourobats.features.management.generated.resources.management_create_session_obs_section
import dourobats.features.management.generated.resources.management_create_session_save
import dourobats.features.management.generated.resources.management_create_session_section_details
import dourobats.features.management.generated.resources.management_create_session_section_schedule
import dourobats.features.management.generated.resources.management_create_session_time_label
import dourobats.features.management.generated.resources.management_create_session_time_placeholder
import dourobats.features.management.generated.resources.management_create_session_time_required
import dourobats.features.management.generated.resources.management_create_session_title
import dourobats.features.management.generated.resources.management_create_session_type_category
import dourobats.features.management.generated.resources.management_create_session_type_match
import dourobats.features.management.generated.resources.management_create_session_type_meeting
import dourobats.features.management.generated.resources.management_create_session_type_practice
import dourobats.features.management.generated.resources.management_create_session_type_social
import dourobats.features.management.generated.resources.management_create_session_type_tournament
import dourobats.features.management.generated.resources.management_create_session_venue_empty
import dourobats.features.management.generated.resources.management_create_session_venue_label
import dourobats.features.management.generated.resources.management_create_session_venue_placeholder
import dourobats.features.management.generated.resources.management_create_session_venue_required
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow
import dourobats.features.management.generated.resources.management_create_session_save_as_template
import dourobats.features.management.generated.resources.management_create_session_save_as_template_desc
import dourobats.features.management.generated.resources.management_create_session_template_name_label
import dourobats.features.management.generated.resources.management_create_session_template_name_required
import dourobats.features.management.generated.resources.management_create_session_templates_section
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import pt.dourobats.app.core.ui.components.layout.AppHeader
import pt.dourobats.app.core.ui.components.layout.SectionHeader
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.core.ui.theme.proIndigo
import pt.dourobats.app.features.venues.api.model.Venue

internal enum class SessionType { PRACTICE, MATCH, MEETING, TOURNAMENT, SOCIAL }

internal enum class SessionLevel { BEGINNER, INTERMEDIATE, ADVANCED, ALL }

internal val SessionType.requiresLevel: Boolean
    get() = this != SessionType.MEETING

internal val SessionType.hasMaxAthletes: Boolean
    get() = when (this) {
        SessionType.PRACTICE, SessionType.MATCH, SessionType.TOURNAMENT -> true
        else -> false
    }

internal val SessionType.hasCoach: Boolean
    get() = this == SessionType.PRACTICE

internal val SessionType.hasDuration: Boolean
    get() = when (this) {
        SessionType.PRACTICE, SessionType.MATCH, SessionType.MEETING -> true
        else -> false
    }

private fun SessionType.icon(): ImageVector = when (this) {
    SessionType.PRACTICE   -> Icons.Default.Loop
    SessionType.MATCH      -> Icons.Default.EmojiEvents
    SessionType.MEETING    -> Icons.Default.Groups
    SessionType.TOURNAMENT -> Icons.Default.WorkspacePremium
    SessionType.SOCIAL     -> Icons.Default.Celebration
}

@Composable
internal fun CreateSessionScreen(
    uiState: CreateSessionUiState,
    onAction: (CreateSessionAction) -> Unit,
) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    var showTypePicker by remember { mutableStateOf(false) }

    val endTimeLabel = remember(uiState.selectedTime, uiState.duration) {
        val time = uiState.selectedTime
        val dur = uiState.duration ?: return@remember null
        val parts = time.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: return@remember null
        val m = parts.getOrNull(1)?.toIntOrNull() ?: return@remember null
        val total = h * 60 + m + dur
        "Ends at ${((total / 60) % 24).toString().padStart(2, '0')}:${(total % 60).toString().padStart(2, '0')}"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        AppHeader(
            title = stringResource(Res.string.management_create_session_title),
            leading = {
                IconButton(onClick = { onAction(CreateSessionAction.NavigateBack) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.management_create_session_title),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            },
            trailing = {
                TextButton(onClick = { onAction(CreateSessionAction.Submit) }) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(
                            text = stringResource(Res.string.management_create_session_save),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            },
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .navigationBarsPadding()
                .padding(horizontal = spacing.screenHorizontal),
        ) {
            Spacer(modifier = Modifier.height(spacing.medium))

            // ── Quick-Start Templates ──────────────────────────────────────────
            if (uiState.templates.isNotEmpty()) {
                CategorizedTemplateSelector(
                    templates = uiState.templates,
                    onTemplateSelected = { onAction(CreateSessionAction.SelectTemplate(it)) },
                )
                Spacer(modifier = Modifier.height(spacing.small))
            }

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
                        selectedType = uiState.sessionType,
                        onClick = { showTypePicker = true },
                    )

                    Spacer(modifier = Modifier.height(spacing.standard))

                    // Session Name
                    ProTextField(
                        value = uiState.sessionName,
                        onValueChange = { onAction(CreateSessionAction.UpdateName(it)) },
                        label = stringResource(Res.string.management_create_session_name_label),
                        isError = uiState.nameError && uiState.showErrors,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    if (uiState.nameError && uiState.showErrors) {
                        Text(
                            text = stringResource(Res.string.management_create_session_name_required),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(spacing.standard))

                    // Venue — full width always
                    VenueSelector(
                        selectedVenue = uiState.selectedVenue,
                        venues = uiState.availableVenues,
                        onVenueSelected = { onAction(CreateSessionAction.UpdateVenue(it)) },
                        isError = uiState.venueError && uiState.showErrors,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    // Level — shown when session type requires it
                    AnimatedVisibility(visible = uiState.sessionType.requiresLevel) {
                        Column {
                            Spacer(modifier = Modifier.height(spacing.standard))
                            LevelSelectionRow(
                                selectedLevel = uiState.selectedLevel,
                                onLevelSelected = { onAction(CreateSessionAction.UpdateLevel(it)) },
                                isError = uiState.levelError && uiState.showErrors,
                            )
                        }
                    }

                    // Max Athletes — practice, match, tournament
                    AnimatedVisibility(visible = uiState.sessionType.hasMaxAthletes) {
                        Column {
                            Spacer(modifier = Modifier.height(spacing.standard))
                            ProTextField(
                                value = uiState.maxAthletes,
                                onValueChange = { onAction(CreateSessionAction.UpdateMaxAthletes(it)) },
                                label = stringResource(Res.string.management_create_session_max_athletes_label),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    // Coach — practice only
                    AnimatedVisibility(visible = uiState.sessionType.hasCoach) {
                        Column {
                            Spacer(modifier = Modifier.height(spacing.standard))
                            ProTextField(
                                value = uiState.coachName,
                                onValueChange = { onAction(CreateSessionAction.UpdateCoach(it)) },
                                label = stringResource(Res.string.management_create_session_coach_label),
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacing.large))

            // ── Observations ──────────────────────────────────────────────────
            SectionHeader(stringResource(Res.string.management_create_session_obs_section))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                OutlinedTextField(
                    value = uiState.observations,
                    onValueChange = { onAction(CreateSessionAction.UpdateObservations(it)) },
                    label = { Text(stringResource(Res.string.management_create_session_obs_label)) },
                    placeholder = { Text(stringResource(Res.string.management_create_session_obs_placeholder)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.standard),
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = proIndigo,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedLabelColor = proIndigo,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                )
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
                border = if ((uiState.dateError || uiState.timeError) && uiState.showErrors)
                    BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                else null,
            ) {
                Column {
                    SchedulePickerRow(
                        label = stringResource(Res.string.management_create_session_date_label),
                        value = uiState.selectedDate.ifEmpty {
                            stringResource(Res.string.management_create_session_date_placeholder)
                        },
                        icon = Icons.Default.CalendarToday,
                        onClick = { onAction(CreateSessionAction.OpenDatePicker) },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    )
                    SchedulePickerRow(
                        label = stringResource(Res.string.management_create_session_time_label),
                        value = uiState.selectedTime.ifEmpty {
                            stringResource(Res.string.management_create_session_time_placeholder)
                        },
                        icon = Icons.Default.Schedule,
                        onClick = { onAction(CreateSessionAction.OpenTimePicker) },
                    )
                    // Duration presets — practice, match, meeting
                    AnimatedVisibility(visible = uiState.sessionType.hasDuration) {
                        Column {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = spacing.standard),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                            )
                            DurationSelectionRow(
                                selectedDuration = uiState.duration,
                                onDurationSelected = { onAction(CreateSessionAction.UpdateDuration(it)) },
                                isError = uiState.durationError && uiState.showErrors,
                            )
                        }
                    }
                    if (endTimeLabel != null) {
                        Text(
                            text = endTimeLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = proIndigo,
                            modifier = Modifier.padding(start = 72.dp, bottom = spacing.standard),
                        )
                    }
                }
            }
            if (uiState.dateError && uiState.showErrors) {
                Text(
                    text = stringResource(Res.string.management_create_session_date_required),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                )
            } else if (uiState.timeError && uiState.showErrors) {
                Text(
                    text = stringResource(Res.string.management_create_session_time_required),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                )
            } else if (uiState.durationError && uiState.showErrors) {
                Text(
                    text = stringResource(Res.string.management_create_session_duration_required),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                )
            }

            Spacer(modifier = Modifier.height(spacing.large))

            // ── Save as Template ───────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(modifier = Modifier.padding(spacing.standard)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(Res.string.management_create_session_save_as_template),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = stringResource(Res.string.management_create_session_save_as_template_desc),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Switch(
                            checked = uiState.shouldSaveAsTemplate,
                            onCheckedChange = { onAction(CreateSessionAction.ToggleSaveTemplate(it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = proIndigo, checkedTrackColor = proIndigo.copy(alpha = 0.4f)),
                        )
                    }
                    AnimatedVisibility(visible = uiState.shouldSaveAsTemplate) {
                        Column {
                            Spacer(modifier = Modifier.height(spacing.small))
                            ProTextField(
                                value = uiState.templateName,
                                onValueChange = { onAction(CreateSessionAction.UpdateTemplateName(it)) },
                                label = stringResource(Res.string.management_create_session_template_name_label),
                                isError = uiState.templateNameError && uiState.showErrors,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            if (uiState.templateNameError && uiState.showErrors) {
                                Text(
                                    text = stringResource(Res.string.management_create_session_template_name_required),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacing.standard))
        }
    }

    if (showTypePicker) {
        SessionTypeBottomSheet(
            selectedType = uiState.sessionType,
            onTypeSelected = { type ->
                onAction(CreateSessionAction.UpdateSessionType(type))
                showTypePicker = false
            },
            onDismiss = { showTypePicker = false },
        )
    }

    if (uiState.showDatePicker) {
        SessionDatePickerDialog(
            onDateSelected = { onAction(CreateSessionAction.SelectDate(it)) },
            onDismiss = { onAction(CreateSessionAction.ClosePickers) },
        )
    }

    if (uiState.showTimePicker) {
        SessionTimePickerDialog(
            onTimeSelected = { onAction(CreateSessionAction.SelectTime(it)) },
            onDismiss = { onAction(CreateSessionAction.ClosePickers) },
        )
    }
}

// ── CategorizedTemplateSelector ───────────────────────────────────────────────

@Composable
private fun CategorizedTemplateSelector(
    templates: List<SessionTemplate>,
    onTemplateSelected: (SessionTemplate) -> Unit,
) {
    val spacing = LocalSpacing.current
    val grouped = remember(templates) { templates.groupBy { it.type } }
    val availableTypes = remember(grouped) { grouped.keys.toList() }
    var selectedType by remember(availableTypes) { mutableStateOf(availableTypes.firstOrNull() ?: SessionType.PRACTICE) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.management_create_session_templates_section),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = spacing.screenHorizontal, vertical = spacing.small),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = spacing.screenHorizontal),
            horizontalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            items(availableTypes) { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { selectedType = type },
                    label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = proIndigo,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }
        Spacer(modifier = Modifier.height(spacing.small))
        LazyRow(
            contentPadding = PaddingValues(horizontal = spacing.screenHorizontal),
            horizontalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            val cards = grouped[selectedType] ?: emptyList()
            items(cards) { template ->
                TemplateCard(template = template, onClick = { onTemplateSelected(template) })
            }
        }
    }
}

@Composable
private fun TemplateCard(
    template: SessionTemplate,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = template.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = proIndigo,
                )
                Text(
                    text = template.venue?.name ?: "—",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            val meta = buildString {
                template.duration?.let { append("${it}min") }
                template.level?.let {
                    if (isNotEmpty()) append(" · ")
                    append(it.name.lowercase().replaceFirstChar { c -> c.uppercase() })
                }
            }
            if (meta.isNotEmpty()) {
                Text(
                    text = meta,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ── VenueSelector — dropdown for venue selection ──────────────────────────────

@Composable
internal fun VenueSelector(
    selectedVenue: Venue?,
    venues: List<Venue>,
    onVenueSelected: (Venue) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
        .also { source ->
            LaunchedEffect(source) {
                source.interactions.collect { interaction ->
                    if (interaction is PressInteraction.Release) expanded = !expanded
                }
            }
        }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedVenue?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(Res.string.management_create_session_venue_label)) },
            placeholder = { Text(stringResource(Res.string.management_create_session_venue_placeholder)) },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded },
                )
            },
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = proIndigo,
                focusedLabelColor = proIndigo,
                cursorColor = proIndigo,
            ),
            interactionSource = interactionSource,
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        ) {
            if (venues.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        Text(
                            stringResource(Res.string.management_create_session_venue_empty),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    onClick = { expanded = false },
                )
            } else {
                venues.forEach { venue ->
                    DropdownMenuItem(
                        text = { Text(venue.name) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp),
                            )
                        },
                        onClick = {
                            onVenueSelected(venue)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
    if (isError) {
        Text(
            text = stringResource(Res.string.management_create_session_venue_required),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
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
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
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

// ── LevelSelector — dropdown for target level ────────────────────────────────

@Composable
private fun LevelSelectionRow(
    selectedLevel: SessionLevel?,
    onLevelSelected: (SessionLevel) -> Unit,
    isError: Boolean,
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
        .also { source ->
            LaunchedEffect(source) {
                source.interactions.collect { interaction ->
                    if (interaction is PressInteraction.Release) expanded = !expanded
                }
            }
        }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedLevel?.displayName() ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(Res.string.management_create_session_level_section)) },
            placeholder = { Text(stringResource(Res.string.management_create_session_level_required)) },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded },
                )
            },
            leadingIcon = selectedLevel?.let {
                {
                    Icon(
                        imageVector = it.icon(),
                        contentDescription = null,
                        tint = proIndigo,
                        modifier = Modifier.size(20.dp),
                    )
                }
            },
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = proIndigo,
                focusedLabelColor = proIndigo,
                cursorColor = proIndigo,
            ),
            interactionSource = interactionSource,
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        ) {
            SessionLevel.entries.forEach { level ->
                DropdownMenuItem(
                    text = { Text(level.displayName()) },
                    leadingIcon = {
                        Icon(
                            imageVector = level.icon(),
                            contentDescription = null,
                            tint = if (selectedLevel == level) proIndigo
                                   else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                    },
                    onClick = {
                        onLevelSelected(level)
                        expanded = false
                    },
                )
            }
        }
    }
    if (isError) {
        Text(
            text = stringResource(Res.string.management_create_session_level_required),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
        )
    }
}

// ── DurationSelectionRow — preset chips for session duration ──────────────────

@Composable
private fun DurationSelectionRow(
    selectedDuration: Int?,
    onDurationSelected: (Int) -> Unit,
    isError: Boolean,
) {
    val spacing = LocalSpacing.current
    Column(modifier = Modifier.padding(spacing.standard)) {
        Text(
            text = stringResource(Res.string.management_create_session_duration_section).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            color = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(30, 60, 90, 120).forEach { minutes ->
                FilterChip(
                    selected = selectedDuration == minutes,
                    onClick = { onDurationSelected(minutes) },
                    label = { Text("${minutes}m") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = proIndigo,
                        selectedLabelColor = Color.White,
                    ),
                )
            }
        }
        if (isError) {
            Text(
                text = stringResource(Res.string.management_create_session_duration_required),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            )
        }
    }
}

// ── SessionDatePickerDialog — M3 date picker restricted to today+ ─────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val todayMillis = Clock.System.now().toEpochMilliseconds()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todayMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                utcTimeMillis >= todayMillis - 86_400_000L
        },
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date
                        val dayOfWeek = date.dayOfWeek.name
                            .lowercase().replaceFirstChar { it.uppercase() }
                        val month = date.month.name
                            .lowercase().replaceFirstChar { it.uppercase() }
                        onDateSelected("$dayOfWeek, ${date.dayOfMonth} $month")
                    }
                    onDismiss()
                },
            ) {
                Text("CONFIRM", fontWeight = FontWeight.Bold, color = proIndigo)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCEL") }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}

// ── SessionTimePickerDialog — M3 time picker defaulting to 18:00 ──────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionTimePickerDialog(
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = 18,
        initialMinute = 0,
        is24Hour = true,
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val h = timePickerState.hour.toString().padStart(2, '0')
                    val m = timePickerState.minute.toString().padStart(2, '0')
                    onTimeSelected("$h:$m")
                    onDismiss()
                },
            ) {
                Text("SET TIME", fontWeight = FontWeight.Bold, color = proIndigo)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCEL") }
        },
        text = { TimePicker(state = timePickerState) },
    )
}

// ── Helpers ───────────────────────────────────────────────────────────────────

@Composable
private fun SessionLevel.displayName(): String = when (this) {
    SessionLevel.BEGINNER     -> stringResource(Res.string.management_create_session_level_beginner)
    SessionLevel.INTERMEDIATE -> stringResource(Res.string.management_create_session_level_intermediate)
    SessionLevel.ADVANCED     -> stringResource(Res.string.management_create_session_level_advanced)
    SessionLevel.ALL          -> stringResource(Res.string.management_create_session_level_all)
}

private fun SessionLevel.icon(): ImageVector = when (this) {
    SessionLevel.BEGINNER     -> Icons.Default.KeyboardArrowUp
    SessionLevel.INTERMEDIATE -> Icons.Default.KeyboardDoubleArrowUp
    SessionLevel.ADVANCED     -> Icons.Default.Whatshot
    SessionLevel.ALL          -> Icons.Default.GridView
}

@Composable
private fun SessionType.label(): String = when (this) {
    SessionType.PRACTICE   -> stringResource(Res.string.management_create_session_type_practice)
    SessionType.MATCH      -> stringResource(Res.string.management_create_session_type_match)
    SessionType.MEETING    -> stringResource(Res.string.management_create_session_type_meeting)
    SessionType.TOURNAMENT -> stringResource(Res.string.management_create_session_type_tournament)
    SessionType.SOCIAL     -> stringResource(Res.string.management_create_session_type_social)
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun CreateSessionScreenPreview() {
    AppTheme {
        CreateSessionScreen(
            uiState = CreateSessionUiState(),
            onAction = {},
        )
    }
}
