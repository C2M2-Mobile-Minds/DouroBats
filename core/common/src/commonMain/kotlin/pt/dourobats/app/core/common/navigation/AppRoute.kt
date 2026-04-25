package pt.dourobats.app.core.common.navigation

/**
 * Marker interface for all type-safe navigation routes in the app.
 *
 * Every `@Serializable` route object/class must implement this interface so that
 * [NavigationEvent.Navigate] can enforce a compile-time constraint — only valid,
 * known route types can be emitted through the navigation system.
 *
 * Example:
 * ```kotlin
 * @Serializable
 * object HomeRoute : AppRoute
 *
 * @Serializable
 * data class NewsDetailRoute(val newsId: String) : AppRoute
 * ```
 */
interface AppRoute
