package pt.dourobats.app.features.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import dourobats.features.schedule.generated.resources.Res
import dourobats.features.schedule.generated.resources.training_coming_soon
import dourobats.features.schedule.generated.resources.training_title
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.core.ui.theme.LocalSpacing

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier.fillMaxSize().padding(spacing.screenHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.training_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(spacing.small))
        Text(
            text = stringResource(Res.string.training_coming_soon),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
