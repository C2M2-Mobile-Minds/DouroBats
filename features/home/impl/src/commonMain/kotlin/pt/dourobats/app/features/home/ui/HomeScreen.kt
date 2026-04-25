package pt.dourobats.app.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dourobats.features.home.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.ui.components.layout.AppHeader
import pt.dourobats.app.core.ui.components.primitives.IconLabelRow
import pt.dourobats.app.core.ui.components.layout.SectionTitle
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.home.ui.components.AnnouncementItem
import pt.dourobats.app.features.home.ui.components.ManagementPortalGrid
import pt.dourobats.app.features.home.ui.components.NewsCard
import pt.dourobats.app.features.home.ui.components.UpcomingSessionItem

@Composable
fun HomeRoute(
    onCreateSession: () -> Unit = {},
    onViewReports: () -> Unit = {},
    onManageMembers: () -> Unit = {},
    onNewsDetail: (String) -> Unit = {},
) {
    val viewModel: HomeViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        state = uiState,
        onAction = { action ->
            when (action) {
                is HomeAction.OnCreateSessionClick -> onCreateSession()
                is HomeAction.OnViewReportsClick -> onViewReports()
                is HomeAction.OnManageMembersClick -> onManageMembers()
                is HomeAction.OnNewsClick -> onNewsDetail(action.newsId)
            }
        }
    )
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

@Composable
private fun LatestNewsSection(onAction: (HomeAction) -> Unit) {
    val spacing = LocalSpacing.current
    val newsItems = listOf(
        Triple(stringResource(Res.string.home_tag_tournament), "Summer Championship 2024", "Registration is now open for the annual summer championship. Secure your spot before it fills up!"),
        Triple(stringResource(Res.string.home_tag_update), "New Venue Wing", "We have expanded our facilities with 4 new courts available for booking starting next week."),
        Triple(stringResource(Res.string.home_tag_event), "Community Night", "Join us for our monthly community gathering with friendly matches and refreshments for all members.")
    )
    val newsIds = listOf("news_1", "news_2", "news_3")
    val pagerState = rememberPagerState(pageCount = { newsItems.size })

    Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(end = 32.dp),
            pageSpacing = spacing.standard,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val (tag, title, description) = newsItems[page]
            NewsCard(
                tag = tag,
                title = title,
                description = description,
                onClick = { onAction(HomeAction.OnNewsClick(newsIds[page])) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(newsItems.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (isSelected) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                )
            }
        }
    }
}

@Composable
private fun NextSessionCard() {
    val spacing = LocalSpacing.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
    ) {
        Column(modifier = Modifier.padding(spacing.standard)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.SportsBasketball,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(spacing.standard))
                    Column {
                        Text(
                            text = "Basketball",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Coach Pedro",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.home_booked),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.large))

            IconLabelRow(
                icon = Icons.Default.CalendarToday,
                text = "Friday, 27 February",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(spacing.extraSmall))
            IconLabelRow(
                icon = Icons.Default.LocationOn,
                text = "Main Hall • 10:00 - 11:30",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(spacing.extraSmall))
            IconLabelRow(
                icon = Icons.Default.Groups,
                text = stringResource(Res.string.home_participants, 5, 12),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
