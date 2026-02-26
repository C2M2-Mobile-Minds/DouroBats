package pt.dourobats.app.core.common

/**
 * True when the app is running a debug build.
 * Used to gate developer-only features from production users.
 */
expect val isDebug: Boolean
