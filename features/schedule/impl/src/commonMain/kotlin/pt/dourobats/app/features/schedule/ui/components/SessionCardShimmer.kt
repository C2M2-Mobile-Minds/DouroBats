package pt.dourobats.app.features.schedule.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import pt.dourobats.app.core.ui.components.cards.SectionCard
import pt.dourobats.app.core.ui.components.feedback.shimmer
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Skeleton placeholder for [SessionCard] shown during data loading.
 * Mirrors the FullSessionLayout exactly — zero layout shift when real data arrives.
 */
@Composable
internal fun SessionCardShimmer(modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current

    SectionCard(
        modifier = modifier,
        contentPadding = PaddingValues(spacing.standard),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Mirrors the Time Block (64dp fixed width)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(64.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 48.dp, height = 20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmer()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(width = 32.dp, height = 12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmer()
                    )
                }

                // Mirrors the VerticalDivider
                Box(
                    modifier = Modifier
                        .padding(horizontal = spacing.standard)
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )

                // Mirrors the Content Block
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmer()
                    )
                    Spacer(modifier = Modifier.height(spacing.small))
                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmer()
                    )
                    Spacer(modifier = Modifier.height(spacing.extraSmall))
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmer()
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.medium))

            // Mirrors the DouroButton (48dp exact height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .shimmer()
            )
        }
    }
}

