package pt.dourobats.app.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pt.dourobats.app.features.login.api.UserRole
import pt.dourobats.app.core.domain.usecase.ObserveUserProfileUseCase

internal class HomeViewModel(
    private val observeUserProfile: ObserveUserProfileUseCase
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = observeUserProfile()
        .map { profile ->
            HomeUiState(
                isCommitteeUser = profile.roles.contains(UserRole.COMMITTEE),
                displayName = profile.displayName
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )
}
