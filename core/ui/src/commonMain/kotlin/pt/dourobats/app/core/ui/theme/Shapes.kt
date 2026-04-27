package pt.dourobats.app.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Kinetic Precision Shape System
 *
 * Sports apps feel faster with controlled, sharp-athletic corners. Overly pill-shaped
 * elements (24dp+) read as casual/consumer — not professional athletics.
 *
 * ## Corner Radius Scale
 *
 * | Token       | Radius | Usage                                     |
 * |-------------|--------|-------------------------------------------|
 * | extraSmall  | 4dp    | Micro chips, skill-level badges           |
 * | small       | 6dp    | Training cards, buttons (rounded-md)      |
 * | medium      | 12dp   | Dialogs, peeking bottom sheets            |
 * | large       | 16dp   | Full bottom sheets, modals                |
 * | extraLarge  | 28dp   | Full-screen overlays, hero sheets         |
 *
 * Wired into [MaterialTheme.shapes] via [AppTheme].
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(6.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
)
