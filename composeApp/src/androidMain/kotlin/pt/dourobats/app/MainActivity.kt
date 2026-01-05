package pt.dourobats.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.SystemBarsController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            // Set the activity view for system bars controller
            val view = LocalView.current
            SystemBarsController.setActivityView(view)

            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}