package pt.dourobats.app.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pt.dourobats.app.core.model.Language
import pt.dourobats.app.core.model.Theme
import pt.dourobats.app.core.model.UserProfile
import pt.dourobats.app.core.model.UserRole
import pt.dourobats.app.core.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private val languageKey = stringPreferencesKey("language")
    private val themeKey = stringPreferencesKey("theme")
    private val displayNameKey = stringPreferencesKey("display_name")
    private val emailKey = stringPreferencesKey("email")
    private val phoneNumberKey = stringPreferencesKey("phone_number")
    private val profileImageUrlKey = stringPreferencesKey("profile_image_url")
    private val rolesKey = stringPreferencesKey("user_roles")

    override val languageFlow: Flow<Language> = dataStore.data.map { preferences ->
        val languageCode = preferences[languageKey]
        if (languageCode != null) {
            Language.fromCode(languageCode)
        } else {
            Language.getSystemDefault()
        }
    }

    override suspend fun setLanguage(language: Language) {
        dataStore.edit { preferences ->
            preferences[languageKey] = language.code
        }
    }

    override suspend fun getLanguage(): Language {
        return languageFlow.first()
    }

    override val themeFlow: Flow<Theme> = dataStore.data.map { preferences ->
        val themeValue = preferences[themeKey]
        if (themeValue != null) {
            Theme.fromValue(themeValue)
        } else {
            Theme.LIGHT
        }
    }

    override suspend fun setTheme(theme: Theme) {
        dataStore.edit { preferences ->
            preferences[themeKey] = theme.name
        }
    }

    override suspend fun getTheme(): Theme {
        return themeFlow.first()
    }

    override val userProfileFlow: Flow<UserProfile> = dataStore.data.map { preferences ->
        val rolesString = preferences[rolesKey]
        val roles = if (!rolesString.isNullOrEmpty()) {
            rolesString.split(",").mapNotNull { name ->
                UserRole.entries.find { it.name == name }
            }
        } else emptyList()
        UserProfile(
            displayName = preferences[displayNameKey] ?: "",
            email = preferences[emailKey] ?: "",
            phoneNumber = preferences[phoneNumberKey] ?: "",
            profileImageUrl = preferences[profileImageUrlKey],
            roles = roles
        )
    }

    override suspend fun updateUserProfile(profile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[displayNameKey] = profile.displayName
            preferences[emailKey] = profile.email
            preferences[phoneNumberKey] = profile.phoneNumber
            if (profile.profileImageUrl != null) {
                preferences[profileImageUrlKey] = profile.profileImageUrl
            } else {
                preferences.remove(profileImageUrlKey)
            }
            preferences[rolesKey] = profile.roles.joinToString(",") { it.name }
        }
    }

    override suspend fun getUserProfile(): UserProfile {
        return userProfileFlow.first()
    }
}
