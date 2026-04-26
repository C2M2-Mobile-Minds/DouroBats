package pt.dourobats.app.features.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp
import dourobats.features.home.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.components.layout.AppHeader
import pt.dourobats.app.core.ui.components.layout.SectionTitle
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.home.ui.components.AnnouncementItem
import pt.dourobats.app.features.home.ui.components.LatestNewsSection
import pt.dourobats.app.features.home.ui.components.NextSessionCard
import pt.dourobats.app.features.home.ui.components.StatCard
import pt.dourobats.app.features.home.ui.components.UpcomingSessionItem

@Composable
internal fun HomeRoute() {
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppHeader(
                title = if (state.displayName.isNotBlank()) {
                    stringResource(Res.string.home_welcome, state.displayName)
                } else {
                    stringResource(Res.string.home_welcome_fallback)
                },
                subtitle = stringResource(Res.string.home_subtitle),
                // Content column comes AFTER in code — natural draw order puts it on top.
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .zIndex(1f)
                    .verticalScroll(rememberScrollState()) // scroll inside the shifted viewport
                    .padding(horizontal = spacing.standard)
            ) {
                // Small top breathing room between header text and the floating hero card
                Spacer(modifier = Modifier.height(spacing.medium))
                // NextSessionCard floats directly into the header — no spacer/section title needed
                NextSessionCard()

                Spacer(modifier = Modifier.height(spacing.large))

                SectionTitle(title = stringResource(Res.string.home_stats_this_season))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.standard),
                ) {
                    StatCard(
                        label = stringResource(Res.string.home_stat_sessions_label),
                        value = stringResource(Res.string.home_stat_sessions_value),
                        icon = Icons.AutoMirrored.Filled.DirectionsRun,
                        modifier = Modifier.weight(1f),
                    )
                    StatCard(
                        label = stringResource(Res.string.home_stat_ranking_label),
                        value = stringResource(Res.string.home_stat_ranking_value),
                        icon = Icons.Default.EmojiEvents,
                        accentColor = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(modifier = Modifier.height(spacing.large))

                SectionTitle(title = stringResource(Res.string.home_latest_news))
                LatestNewsSection(onAction = onAction)

                Spacer(modifier = Modifier.height(spacing.large))

                SectionTitle(title = stringResource(Res.string.home_upcoming_sessions))
                UpcomingSessionItem(
                    sport = stringResource(Res.string.home_sport_swimming),
                    location = stringResource(Res.string.home_venue_indoor_pool),
                    time = "16:00",
                    participants = "12/20",
                    icon = Icons.Default.Pool
                )
                Spacer(modifier = Modifier.height(spacing.small))
                UpcomingSessionItem(
                    sport = stringResource(Res.string.home_sport_volleyball),
                    location = stringResource(Res.string.home_venue_secondary_hall),
                    time = "18:00",
                    participants = "10/14",
                    icon = Icons.Default.SportsVolleyball
                )
                Spacer(modifier = Modifier.height(spacing.small))
                UpcomingSessionItem(
                    sport = stringResource(Res.string.home_sport_basketball),
                    location = stringResource(Res.string.home_venue_main_hall),
                    time = "10:00",
                    participants = "5/12",
                    icon = Icons.Default.SportsBasketball
                )

                Spacer(modifier = Modifier.height(spacing.large))

                SectionTitle(title = stringResource(Res.string.home_announcements))
                AnnouncementItem(
                    title = stringResource(Res.string.home_announcement_hall_title),
                    description = stringResource(Res.string.home_announcement_hall_desc),
                    date = stringResource(Res.string.home_announcement_hall_date)
                )
                Spacer(modifier = Modifier.height(spacing.small))
                AnnouncementItem(
                    title = stringResource(Res.string.home_announcement_tournament_title),
                    description = stringResource(Res.string.home_announcement_tournament_desc),
                    date = stringResource(Res.string.home_announcement_tournament_date)
                )
            }
        }

        // Committee-only FAB: instant access to "Create Session" — most common admin task
        AnimatedVisibility(
            visible = state.isCommitteeUser,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(spacing.standard)
                .navigationBarsPadding(),
        ) {
            FloatingActionButton(
                onClick = { onAction(HomeAction.OnCreateSessionClick) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(Res.string.home_portal_new_session))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenAthletePreview() {
    AppTheme {
        HomeScreen(
            state = HomeUiState(
                isCommitteeUser = false,
                displayName = "Carlos",
            ),
            onAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenCommitteePreview() {
    AppTheme {
        HomeScreen(
            state = HomeUiState(
                isCommitteeUser = true,
                displayName = "Carlos",
            ),
            onAction = {},
        )
    }
}
