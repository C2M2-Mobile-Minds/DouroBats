package pt.dourobats.app.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dourobats.features.home.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.ui.components.layout.AppHeader
import pt.dourobats.app.core.ui.components.layout.SectionTitle
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.home.ui.components.AnnouncementItem
import pt.dourobats.app.features.home.ui.components.LatestNewsSection
import pt.dourobats.app.features.home.ui.components.ManagementPortalGrid
import pt.dourobats.app.features.home.ui.components.NextSessionCard
import pt.dourobats.app.features.home.ui.components.UpcomingSessionItem

@Composable
fun HomeRoute() {
    val viewModel: HomeViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(state = uiState, onAction = viewModel::onAction)
}

@Composable
internal fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        AppHeader(
            title = if (state.displayName.isNotBlank()) {
                stringResource(Res.string.home_welcome, state.displayName)
            } else {
                stringResource(Res.string.home_welcome_fallback)
            },
            subtitle = stringResource(Res.string.home_subtitle)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.standard)
        ) {
            Spacer(modifier = Modifier.height(spacing.large))

            SectionTitle(title = stringResource(Res.string.home_next_session))
            NextSessionCard()

            Spacer(modifier = Modifier.height(spacing.large))

            if (state.isCommitteeUser) {
                SectionTitle(title = stringResource(Res.string.home_management_portal))
                ManagementPortalGrid(onAction = onAction)

                Spacer(modifier = Modifier.height(spacing.large))
            }

            SectionTitle(title = stringResource(Res.string.home_latest_news))
            LatestNewsSection(onAction = onAction)

            Spacer(modifier = Modifier.height(spacing.large))

            SectionTitle(title = stringResource(Res.string.home_upcoming_sessions))
            UpcomingSessionItem(
                sport = "Swimming",
                location = "Indoor Pool",
                time = "16:00",
                participants = "12/20",
                icon = Icons.Default.Pool
            )
            Spacer(modifier = Modifier.height(spacing.small))
            UpcomingSessionItem(
                sport = "Volleyball",
                location = "Secondary Hall",
                time = "18:00",
                participants = "10/14",
                icon = Icons.Default.SportsVolleyball
            )
            Spacer(modifier = Modifier.height(spacing.small))
            UpcomingSessionItem(
                sport = "Basketball",
                location = "Main Hall",
                time = "10:00",
                participants = "5/12",
                icon = Icons.Default.SportsBasketball
            )

            Spacer(modifier = Modifier.height(spacing.large))

            SectionTitle(title = stringResource(Res.string.home_announcements))
            AnnouncementItem(
                title = "New Sports Hall Opened",
                description = "The new sports hall is now available for bookings!",
                date = "25 February 2026"
            )
            Spacer(modifier = Modifier.height(spacing.small))
            AnnouncementItem(
                title = "Football Tournament",
                description = "Registration open for the summer football tournament.",
                date = "24 February 2026"
            )

            Spacer(modifier = Modifier.height(spacing.huge))
        }
    }
}
