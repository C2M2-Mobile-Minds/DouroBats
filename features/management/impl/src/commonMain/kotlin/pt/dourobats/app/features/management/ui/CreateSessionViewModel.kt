package pt.dourobats.app.features.management.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.dourobats.app.features.venues.api.usecase.GetVenuesUseCase

internal class CreateSessionViewModel(
    private val getVenuesUseCase: GetVenuesUseCase,
    private val templateStore: TemplateStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateSessionUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = Channel<CreateSessionEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        loadVenues()
        _uiState.update { it.copy(templates = templateStore.templates) }
    }

    private fun loadVenues() {
        viewModelScope.launch {
            _uiState.update { it.copy(isVenuesLoading = true) }
            getVenuesUseCase().collect { venues ->
                _uiState.update { it.copy(availableVenues = venues, isVenuesLoading = false) }
            }
        }
    }

    fun onAction(action: CreateSessionAction) {
        when (action) {
            is CreateSessionAction.UpdateName -> _uiState.update {
                it.copy(sessionName = action.name, nameError = action.name.isBlank())
            }
            is CreateSessionAction.UpdateVenue -> _uiState.update {
                it.copy(selectedVenue = action.venue, venueError = false)
            }
            is CreateSessionAction.UpdateMaxAthletes -> _uiState.update {
                it.copy(maxAthletes = action.value)
            }
            is CreateSessionAction.UpdateSessionType -> _uiState.update {
                it.copy(sessionType = action.type, levelError = false, durationError = false)
            }
            is CreateSessionAction.UpdateLevel -> _uiState.update {
                it.copy(selectedLevel = action.level, levelError = false)
            }
            is CreateSessionAction.UpdateCoach -> _uiState.update {
                it.copy(coachName = action.coach)
            }
            is CreateSessionAction.UpdateDuration -> _uiState.update {
                it.copy(duration = action.minutes, durationError = false)
            }
            is CreateSessionAction.SelectDate -> _uiState.update {
                it.copy(selectedDate = action.date, dateError = false)
            }
            is CreateSessionAction.SelectTime -> _uiState.update {
                it.copy(selectedTime = action.time, timeError = false)
            }
            CreateSessionAction.OpenDatePicker -> _uiState.update {
                it.copy(showDatePicker = true)
            }
            CreateSessionAction.OpenTimePicker -> _uiState.update {
                it.copy(showTimePicker = true)
            }
            CreateSessionAction.ClosePickers -> _uiState.update {
                it.copy(showDatePicker = false, showTimePicker = false)
            }
            is CreateSessionAction.UpdateObservations -> _uiState.update { it.copy(observations = action.value) }
            is CreateSessionAction.SelectTemplate -> applyTemplate(action.template)
            is CreateSessionAction.ToggleSaveTemplate -> _uiState.update {
                it.copy(shouldSaveAsTemplate = action.enabled, templateNameError = false)
            }
            is CreateSessionAction.UpdateTemplateName -> _uiState.update {
                it.copy(templateName = action.name, templateNameError = action.name.isBlank())
            }
            CreateSessionAction.Submit -> validateAndSave()
            CreateSessionAction.NavigateBack -> viewModelScope.launch {
                _effects.send(CreateSessionEffect.NavigateBack)
            }
        }
    }

    private fun applyTemplate(template: SessionTemplate) {
        _uiState.update {
            it.copy(
                sessionType = template.type,
                sessionName = template.name,
                selectedVenue = template.venue ?: it.selectedVenue,
                selectedLevel = template.level,
                duration = template.duration,
                maxAthletes = template.maxAthletes,
                coachName = template.coachName,
                showErrors = false,
                nameError = false,
                venueError = false,
                levelError = false,
                durationError = false,
            )
        }
    }

    private fun validateAndSave() {
        val state = _uiState.value
        val type = state.sessionType
        val nameError = state.sessionName.isBlank()
        val venueError = state.selectedVenue == null
        val levelError = type.requiresLevel && state.selectedLevel == null
        val durationError = type.hasDuration && state.duration == null
        val dateError = state.selectedDate.isBlank()
        val timeError = state.selectedTime.isBlank()

        val templateNameError = state.shouldSaveAsTemplate && state.templateName.isBlank()

        if (nameError || venueError || levelError || durationError || dateError || timeError || templateNameError) {
            _uiState.update {
                it.copy(
                    showErrors = true,
                    nameError = nameError,
                    venueError = venueError,
                    levelError = levelError,
                    durationError = durationError,
                    dateError = dateError,
                    timeError = timeError,
                    templateNameError = templateNameError,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (state.shouldSaveAsTemplate) {
                templateStore.add(
                    SessionTemplate(
                        id = "template-${kotlin.random.Random.nextInt(100000)}",
                        name = state.templateName,
                        type = state.sessionType,
                        venue = state.selectedVenue,
                        level = state.selectedLevel,
                        duration = state.duration,
                        maxAthletes = state.maxAthletes,
                        coachName = state.coachName,
                    )
                )
                _uiState.update { it.copy(templates = templateStore.templates) }
            }
            val fakeSessionId = "session-${kotlin.random.Random.nextInt(100000)}"
            _uiState.update { it.copy(isLoading = false) }
            _effects.send(
                CreateSessionEffect.SessionCreated(
                    sessionId = fakeSessionId,
                    sessionName = state.sessionName,
                )
            )
        }
    }
}
