package pt.dourobats.app.core.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationManager {
    private val _events = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<NavigationEvent> = _events.asSharedFlow()

    fun navigate(event: NavigationEvent) {
        _events.tryEmit(event)
    }
}
