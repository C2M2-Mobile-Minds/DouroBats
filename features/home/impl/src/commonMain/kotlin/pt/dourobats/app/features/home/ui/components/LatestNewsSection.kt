package pt.dourobats.app.features.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dourobats.features.home.generated.resources.Res
import dourobats.features.home.generated.resources.home_tag_event
import dourobats.features.home.generated.resources.home_tag_tournament
import dourobats.features.home.generated.resources.home_tag_update
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.home.ui.HomeAction

@Composable
internal fun LatestNewsSection(onAction: (HomeAction) -> Unit) {
    val spacing = LocalSpacing.current
    val newsItems = listOf(
        Triple(stringResource(Res.string.home_tag_tournament), "Summer Championship 2024", "Registration is now open for the annual summer championship. Secure your spot before it fills up!"),
        Triple(stringResource(Res.string.home_tag_update), "New Venue Wing", "We have expanded our facilities with 4 new courts available for booking starting next week."),
        Triple(stringResource(Res.string.home_tag_event), "Community Night", "Join us for our monthly community gathering with friendly matches and refreshments for all members.")
    )
    val newsIds = listOf("news_1", "news_2", "news_3")
    val pagerState = rememberPagerState(pageCount = { newsItems.size })

    Column(verticalArrangement = Arrangement.spacedBy(spacing.medium)) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(end = spacing.huge),
            pageSpacing = spacing.standard,
            beyondViewportPageCount = 1,
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

        PagerDotIndicator(
            pageCount = newsItems.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
private fun PagerDotIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val isSelected = currentPage == index

            val width by animateDpAsState(
                targetValue = if (isSelected) 16.dp else 6.dp,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "dotWidth",
            )
            val color by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                              else MaterialTheme.colorScheme.outlineVariant,
                label = "dotColor",
            )

            Box(
                modifier = Modifier
                    .height(6.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LatestNewsSectionPreview() {
    AppTheme {
        LatestNewsSection(onAction = {})
    }
}

