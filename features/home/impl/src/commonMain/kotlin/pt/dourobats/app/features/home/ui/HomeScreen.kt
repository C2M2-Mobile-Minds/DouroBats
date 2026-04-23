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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dourobats.features.home.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.ui.components.AppHeader
import pt.dourobats.app.core.ui.components.DetailRow
import pt.dourobats.app.core.ui.components.SectionTitle
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.schedule.api.ui.UpcomingSessionItem

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
            .background(MaterialTheme.colorScheme.surfaceContainerLow) // Section layer for base background
    ) {
        // Shared Header Component
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

            // Next Session Section
            SectionTitle(title = stringResource(Res.string.home_next_session))
            NextSessionCard()

            Spacer(modifier = Modifier.height(spacing.large))

            // Management Portal Section (committee members only)
            if (state.isCommitteeUser) {
                SectionTitle(title = stringResource(Res.string.home_management_portal))
                ManagementPortalGrid(onAction = onAction)

                Spacer(modifier = Modifier.height(spacing.large))
            }

            // Latest News Section
            SectionTitle(title = stringResource(Res.string.home_latest_news))
            LatestNewsSection(onAction = onAction)

            Spacer(modifier = Modifier.height(spacing.large))

            // Upcoming Sessions Section
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

            // Announcements Section
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
private fun ManagementPortalGrid(onAction: (HomeAction) -> Unit) {
    val spacing = LocalSpacing.current
    Column(verticalArrangement = Arrangement.spacedBy(spacing.standard)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.standard)
        ) {
            PortalItem(
                title = stringResource(Res.string.home_portal_new_session),
                icon = Icons.Default.Add,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                iconColor = MaterialTheme.colorScheme.primary,
                onClick = { onAction(HomeAction.OnCreateSessionClick) },
                modifier = Modifier.weight(1f)
            )
            PortalItem(
                title = stringResource(Res.string.home_portal_reports),
                icon = Icons.Default.PieChart,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                iconColor = MaterialTheme.colorScheme.secondary,
                onClick = { onAction(HomeAction.OnViewReportsClick) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.standard)
        ) {
            PortalItem(
                title = stringResource(Res.string.home_portal_members),
                icon = Icons.Default.Groups,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconColor = MaterialTheme.colorScheme.tertiary,
                onClick = { onAction(HomeAction.OnManageMembersClick) },
                modifier = Modifier.weight(1f)
            )
            PortalItem(
                title = stringResource(Res.string.home_portal_post_news),
                icon = Icons.Default.Campaign,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                onClick = { },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PortalItem(
    title: String,
    icon: ImageVector,
    containerColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Card(
        onClick = onClick,
        modifier = modifier.aspectRatio(1.2f),
        shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
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
private fun NewsCard(
    tag: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest), // Component layer - "Active Card"
        elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow) // Section layer for hierarchy
            ) {
                Surface(
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    shape = RoundedCornerShape(6.dp) // rounded-md consistency
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(Res.string.home_read_more),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
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
        shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
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
                    color = MaterialTheme.colorScheme.secondaryContainer, // Pitch Green for "Attending" status
                    shape = RoundedCornerShape(6.dp) // rounded-md consistency
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

            DetailRow(
                icon = Icons.Default.CalendarToday, 
                text = "Friday, 27 February",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(spacing.extraSmall))
            DetailRow(
                icon = Icons.Default.LocationOn, 
                text = "Main Hall • 10:00 - 11:30",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(spacing.extraSmall))
            DetailRow(
                icon = Icons.Default.Groups,
                text = stringResource(Res.string.home_participants, 5, 12),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun AnnouncementItem(
    title: String,
    description: String,
    date: String
) {
    val spacing = LocalSpacing.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest // Component layer - "Active Card"
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
    ) {
        Row(
            modifier = Modifier.padding(spacing.standard),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(spacing.standard))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}
