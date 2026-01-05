package pt.dourobats.app.core.ui.theme

/**
 * iOS-specific implementation for system bars appearance.
 * iOS automatically adjusts status bar appearance based on the view controller's style,
 * so this is a no-op implementation.
 */
actual fun updateSystemBarsAppearance(isDark: Boolean) {
    // No-op on iOS - the system handles this automatically
}
