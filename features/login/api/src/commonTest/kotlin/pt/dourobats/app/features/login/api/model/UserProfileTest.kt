package pt.dourobats.app.features.login.api.model

import pt.dourobats.app.features.login.api.model.UserProfile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(kotlin.experimental.ExperimentalNativeApi::class)
class UserProfileTest {

    @Test
    fun `empty returns profile with empty strings`() {
        // Arrange & Act
        val profile = UserProfile.empty()

        // Assert
        assertEquals("", profile.displayName)
        assertEquals("", profile.email)
        assertEquals("", profile.phoneNumber)
        assertNull(profile.profileImageUrl)
    }

    @Test
    fun `profile is created with all fields when provided`() {
        // Arrange & Act
        val profile = UserProfile(
            displayName = "John Doe",
            email = "john@example.com",
            phoneNumber = "+351912345678",
            profileImageUrl = "https://example.com/avatar.jpg"
        )

        // Assert
        assertEquals("John Doe", profile.displayName)
        assertEquals("john@example.com", profile.email)
        assertEquals("+351912345678", profile.phoneNumber)
        assertEquals("https://example.com/avatar.jpg", profile.profileImageUrl)
    }

    @Test
    fun `profile is created without image URL when not provided`() {
        // Arrange & Act
        val profile = UserProfile(
            displayName = "Jane Smith",
            email = "jane@example.com",
            phoneNumber = "+351987654321"
        )

        // Assert
        assertEquals("Jane Smith", profile.displayName)
        assertEquals("jane@example.com", profile.email)
        assertEquals("+351987654321", profile.phoneNumber)
        assertNull(profile.profileImageUrl)
    }

    @Test
    fun `copy updates display name when changed`() {
        // Arrange
        val original = UserProfile(
            displayName = "Original Name",
            email = "test@example.com",
            phoneNumber = "+351912345678"
        )

        // Act
        val updated = original.copy(displayName = "New Name")

        // Assert
        assertEquals("New Name", updated.displayName)
        assertEquals(original.email, updated.email)
        assertEquals(original.phoneNumber, updated.phoneNumber)
    }

    @Test
    fun `copy updates phone number when changed`() {
        // Arrange
        val original = UserProfile(
            displayName = "Test User",
            email = "test@example.com",
            phoneNumber = "+351111111111"
        )

        // Act
        val updated = original.copy(phoneNumber = "+351222222222")

        // Assert
        assertEquals(original.displayName, updated.displayName)
        assertEquals(original.email, updated.email)
        assertEquals("+351222222222", updated.phoneNumber)
    }

    @Test
    fun `copy updates image URL when changed`() {
        // Arrange
        val original = UserProfile(
            displayName = "Test User",
            email = "test@example.com",
            phoneNumber = "+351912345678",
            profileImageUrl = "https://old.com/image.jpg"
        )

        // Act
        val updated = original.copy(profileImageUrl = "https://new.com/image.jpg")

        // Assert
        assertEquals("https://new.com/image.jpg", updated.profileImageUrl)
    }
}
