package pt.dourobats.app.core.common.logging

/**
 * Platform adapter factory — Hexagonal Architecture "adapter" entry point.
 *
 * The [Logger] interface lives as a pure domain port. This separate file holds the
 * `expect` declaration so the interface file stays free of platform coupling.
 *
 * Implementations live in `:androidMain` and `:iosMain` source sets.
 */
expect fun createPlatformLogger(): Logger
