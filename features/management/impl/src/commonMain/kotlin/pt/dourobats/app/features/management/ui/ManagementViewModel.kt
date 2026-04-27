package pt.dourobats.app.features.management.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pt.dourobats.app.features.management.ManagementAction
import pt.dourobats.app.features.management.ManagementEffect

internal class ManagementViewModel : ViewModel() {

    private val _effects = Channel<ManagementEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onAction(action: ManagementAction) {
        viewModelScope.launch {
            when (action) {
                ManagementAction.CreateSessionRequested ->
                    _effects.send(ManagementEffect.NavigateToCreateSession)
                ManagementAction.PostAnnouncementRequested ->
                    _effects.send(ManagementEffect.NavigateToAnnouncement)
                ManagementAction.CheckInRequested ->
                    _effects.send(ManagementEffect.NavigateToCheckIn)
                ManagementAction.Dismiss ->
                    _effects.send(ManagementEffect.Back)
            }
        }
    }
}
