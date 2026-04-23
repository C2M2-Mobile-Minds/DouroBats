package pt.dourobats.app.features.settings.api.model

enum class Theme(val displayName: String) {
    LIGHT("Light"),
    DARK("Dark");

    companion object {
        fun fromValue(value: String): Theme =
            entries.find { it.name == value } ?: LIGHT
    }
}
