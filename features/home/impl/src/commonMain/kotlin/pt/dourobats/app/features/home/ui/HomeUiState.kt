package pt.dourobats.app.features.home.ui

/**
 * UI state for the Home screen.
 *
 * @property isCommitteeUser True when the signed-in user has the COMMITTEE role,
 *   which gates visibility of the management portal section.
 */
internal data class HomeUiState(
    val isCommitteeUser: Boolean = false,
    val displayName: String = ""
)
