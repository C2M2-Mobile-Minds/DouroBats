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
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateSessionUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = Channel<CreateSessionEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        loadVenues()
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
                it.copy(sessionType = action.type)
            }
            is CreateSessionAction.SelectDate -> _uiState.update {
                it.copy(selectedDate = action.date, dateError = false)
            }
            is CreateSessionAction.SelectTime -> _uiState.update {
                it.copy(selectedTime = action.time, timeError = false)
            }
            CreateSessionAction.Submit -> validateAndSave()
            CreateSessionAction.NavigateBack -> viewModelScope.launch {
                _effects.send(CreateSessionEffect.NavigateBack)
            }
        }
    }

    private fun validateAndSave() {
        val state = _uiState.value
        val nameError = state.sessionName.isBlank()
        val venueError = state.selectedVenue == null
        val dateError = state.selectedDate.isBlank()
        val timeError = state.selectedTime.isBlank()

        if (nameError || venueError || dateError || timeError) {
            _uiState.update {
                it.copy(
                    showErrors = true,
                    nameError = nameError,
                    venueError = venueError,
                    dateError = dateError,
                    timeError = timeError,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
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
