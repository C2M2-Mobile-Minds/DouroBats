package pt.dourobats.app.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(kotlin.experimental.ExperimentalNativeApi::class)
class UserProfileRolesTest {

    @Test
    fun `empty profile has no roles`() {
        val profile = UserProfile.empty()

        assertTrue(profile.roles.isEmpty())
    }

    @Test
    fun `profile is created with roles`() {
        val profile = UserProfile(
            displayName = "Alice",
            email = "alice@example.com",
            phoneNumber = "+351910000001",
            roles = listOf(UserRole.ATHLETE, UserRole.COMMITTEE)
        )

        assertEquals(2, profile.roles.size)
        assertTrue(profile.roles.contains(UserRole.ATHLETE))
        assertTrue(profile.roles.contains(UserRole.COMMITTEE))
    }

    @Test
    fun `profile defaults to empty roles when not provided`() {
        val profile = UserProfile(
            displayName = "Bob",
            email = "bob@example.com",
            phoneNumber = "+351910000002"
        )

        assertTrue(profile.roles.isEmpty())
    }

    @Test
    fun `copy updates roles`() {
        val original = UserProfile(
            displayName = "Carol",
            email = "carol@example.com",
            phoneNumber = "+351910000003",
            roles = listOf(UserRole.SUPPORTER)
        )

        val updated = original.copy(roles = listOf(UserRole.ATHLETE))

        assertEquals(listOf(UserRole.ATHLETE), updated.roles)
        assertEquals(original.displayName, updated.displayName)
    }

    @Test
    fun `existing UserProfileTest empty returns null imageUrl`() {
        val profile = UserProfile.empty()

        assertNull(profile.profileImageUrl)
    }
}
