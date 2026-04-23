package pt.dourobats.app.features.login.api

/**
 * Represents the role a user holds within the club.
 *
 * - [ATHLETE] – a club member who participates in training sessions.
 * - [SUPPORTER] – a non-playing member who supports the club.
 * - [COMMITTEE] – a club administrator with management privileges.
 */
enum class UserRole {
    ATHLETE,
    SUPPORTER,
    COMMITTEE
}
