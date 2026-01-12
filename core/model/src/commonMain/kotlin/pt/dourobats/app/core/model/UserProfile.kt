package pt.dourobats.app.core.domain.model

/**
 * Represents user profile information.
 * Immutable data class following domain-driven design.
 *
 * @property displayName User's display name
 * @property email User's email address (read-only, managed by auth system)
 * @property phoneNumber User's phone number
 * @property profileImageUrl Optional URL to user's profile image
 */
data class UserProfile(
    val displayName: String,
    val email: String,
    val phoneNumber: String,
    val profileImageUrl: String? = null
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
            profileImageUrl = null
        )
    }
}
