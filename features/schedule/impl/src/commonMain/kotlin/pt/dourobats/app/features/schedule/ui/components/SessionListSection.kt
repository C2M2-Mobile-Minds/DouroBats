package pt.dourobats.app.features.schedule.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.schedule.api.ui.SessionUiModel

/**
 * Section component for displaying a list of sessions.
 */
@Composable
internal fun SessionListSection(
    title: String,
    sessions: List<SessionUiModel>,
    emptyMessage: String,
    showDate: Boolean = false,
    bookedBadgeText: String = "Booked",
    sessionLoadingStates: Map<String, Boolean> = emptyMap(),
    onBookSession: ((String) -> Unit)? = null,
    onCancelBooking: ((String) -> Unit)? = null,
    bookButtonText: String = "Book",
    cancelButtonText: String = "Cancel",
    attendingText: String = "attending",
    fullButtonText: String = "Full",
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = spacing.screenHorizontal)
        )

        Spacer(modifier = Modifier.height(spacing.standard))

        if (sessions.isEmpty()) {
            Text(
                text = emptyMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = spacing.screenHorizontal)
            )
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.small),
                modifier = Modifier.padding(horizontal = spacing.screenHorizontal)
            ) {
                sessions.forEach { sessionData ->
                    SessionCard(
                        sessionData = sessionData,
                        showDate = showDate,
                        bookedBadgeText = bookedBadgeText,
                        isLoading = sessionLoadingStates[sessionData.session.id] ?: false,
                        onBookSession = onBookSession,
                        onCancelBooking = onCancelBooking,
                        bookButtonText = bookButtonText,
                        cancelButtonText = cancelButtonText,
                        attendingText = attendingText,
                        fullButtonText = fullButtonText
                    )
                }
            }
        }
    }
}
