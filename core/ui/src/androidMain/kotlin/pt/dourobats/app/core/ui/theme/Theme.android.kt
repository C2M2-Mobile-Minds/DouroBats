package pt.dourobats.app.core.ui.theme

import android.app.Activity
import android.view.View
import androidx.core.view.WindowCompat

/**
 * Android-specific implementation to update system bars appearance.
 * Sets status bar and navigation bar icons to be light (white) in dark mode
 * and dark (black) in light mode for better visibility.
 */
actual fun updateSystemBarsAppearance(isDark: Boolean) {
    // This will be called from a Composable context, but needs to access the Activity
    // We'll use a different approach - store a reference that can be set from MainActivity
    SystemBarsController.updateAppearance(isDark)
}

/**
 * Helper object to manage system bars appearance.
 * The activity reference is set from MainActivity.
 */
object SystemBarsController {
    private var activityView: View? = null

    fun setActivityView(view: View) {
        activityView = view
    }

    fun updateAppearance(isDark: Boolean) {
        val view = activityView ?: return
        val window = (view.context as? Activity)?.window ?: return

        WindowCompat.getInsetsController(window, view).apply {
            // Set light or dark icons based on theme
            // When isDark = true (dark mode), we want light icons (isAppearanceLightStatusBars = false)
            // When isDark = false (light mode), we want dark icons (isAppearanceLightStatusBars = true)
            isAppearanceLightStatusBars = !isDark
            isAppearanceLightNavigationBars = !isDark
        }
    }
}
