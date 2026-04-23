package pt.dourobats.app.features.login.api.model

/**
 * Represents user profile information.
 * Immutable data class following domain-driven design.
 *
 * @property displayName User's display name
 * @property email User's email address (read-only, managed by auth system)
 * @property phoneNumber User's phone number
 * @property profileImageUrl Optional URL to user's profile image
 * @property roles Club roles assigned to this user (e.g. Athlete, Committee)
 */
data class UserProfile(
    val displayName: String,
    val email: String,
    val phoneNumber: String,
    val profileImageUrl: String? = null,
    val roles: List<UserRole> = emptyList()
) {
    companion object {
        /**
         * Creates an empty UserProfile instance.
         * Useful for initialization and default states.
         */
        fun empty() = UserProfile(
            displayName = "",
            email = "",
            phoneNumber = "",
            profileImageUrl = null,
            roles = emptyList()
        )
    }
}
