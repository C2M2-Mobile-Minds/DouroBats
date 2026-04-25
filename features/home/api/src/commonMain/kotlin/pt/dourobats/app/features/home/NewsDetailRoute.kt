package pt.dourobats.app.features.home

import kotlinx.serialization.Serializable
import pt.dourobats.app.core.common.navigation.AppRoute

@Serializable
data class NewsDetailRoute(val newsId: String) : AppRoute
