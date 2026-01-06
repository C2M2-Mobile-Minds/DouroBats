# Testing Guide for Schedule Feature

This document describes the testing strategy and setup for the schedule feature.

## Test Coverage

### ✅ Unit Tests (Complete - 31 tests passing)

Unit tests verify business logic, data transformations, and utility functions without requiring UI rendering.

**Location:** `src/commonTest/kotlin/`

**Test Files:**
- `MockSessionDataTest.kt` - Mock data generation and session filtering (15 tests)
- `CalendarUtilsTest.kt` - Calendar date calculations and grid generation (16 tests)

**Running Unit Tests:**
```bash
./gradlew :features:schedule:test
```

### 🔧 UI Tests (Infrastructure Ready)

Compose UI test dependencies are configured, but actual UI tests require platform-specific test runners.

**Dependencies Added:**
- `compose-ui-test` - Core testing framework
- `compose-ui-test-junit4` - JUnit4 integration for Android
- `compose-ui-test-manifest` - Debug manifest for instrumented tests

**Why UI Tests Aren't in `commonTest`:**

Compose UI tests need:
1. **A UI runtime** - Composables must actually render
2. **Platform-specific test runner** - Android requires instrumentation, Desktop requires JVM runtime
3. **Test manifest** - Android needs proper configuration for UI testing

## Running Compose UI Tests

### Option 1: Android Instrumented Tests (Recommended)

Create tests in `src/androidInstrumentedTest/kotlin/`:

```kotlin
@RunWith(AndroidJUnit4::class)
class SessionCardInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sessionCard_displaysSessionInfo() {
        composeTestRule.setContent {
            SessionCard(sessionData = testData)
        }

        composeTestRule
            .onNodeWithText("Baseball")
            .assertIsDisplayed()
    }
}
```

**Run with:**
```bash
./gradlew :features:schedule:connectedAndroidTest
```

**Requirements:**
- Connected Android device or emulator
- USB debugging enabled

### Option 2: Desktop Tests

For JVM/Desktop testing, create tests in a desktop-specific source set with the Compose Desktop test runner.

### Option 3: Screenshot Tests

Consider using screenshot testing libraries like:
- **Paparazzi** (JVM-based, no device needed)
- **Shot** (Android instrumented)
- **Compose Preview Screenshot Testing**

## Test Coverage Summary

| Component | Unit Tests | UI Tests | Notes |
|-----------|------------|----------|-------|
| `CalendarUtils` | ✅ 16 tests | N/A | Date calculations fully tested |
| `MockSessionData` | ✅ 15 tests | N/A | Data generation verified |
| `WeekCalendar` | ❌ | 🔧 Ready | Needs instrumented tests |
| `MonthCalendar` | ❌ | 🔧 Ready | Needs instrumented tests |
| `SessionCard` | ❌ | 🔧 Ready | Needs instrumented tests |
| `SessionListSection` | ❌ | 🔧 Ready | Needs instrumented tests |

## What's Tested

### ✅ Comprehensive Unit Test Coverage

**Calendar Logic:**
- Week start calculations (Monday-based weeks)
- Week date generation (7 consecutive days)
- Month grid calculations (complete weeks, 35-42 cells)
- Leap year handling
- Date validation and filtering

**Session Data:**
- Mock data generation (multiple sports, dates, venues)
- Session display data mapping (icons, names, venues)
- Booking status identification
- Sport type recognition (Baseball, Softball)
- Capacity calculations

### 🔧 Ready for UI Testing

**Components Ready for UI Tests:**
- Date selection interactions
- Calendar navigation (week/month switching)
- Session card rendering (time, location, capacity)
- Booking badge display
- Empty states
- Localization verification

## Next Steps for UI Testing

1. **Choose a platform:** Android instrumented or Desktop tests
2. **Create test source set:** `androidInstrumentedTest` or `desktopTest`
3. **Write UI tests** using examples above
4. **Set up CI/CD** to run tests automatically

## Best Practices

### Unit Tests
- ✅ Fast - run in milliseconds
- ✅ No dependencies - no device/emulator needed
- ✅ Deterministic - same input = same output
- ✅ Test business logic and calculations

### UI Tests
- ⚠️ Slower - require rendering
- ⚠️ Platform-specific - need device/emulator
- ✅ Verify user-facing behavior
- ✅ Catch visual regressions

## Continuous Integration

**Current Setup:**
```yaml
# Example CI configuration
test:
  - run: ./gradlew :features:schedule:test  # Unit tests ✅
  # - run: ./gradlew :features:schedule:connectedAndroidTest  # UI tests (needs device)
```

For UI tests in CI, use:
- **Android Emulator** in CI (GitHub Actions, GitLab CI)
- **Firebase Test Lab** for real devices
- **Paparazzi** for JVM-based screenshot tests (no emulator needed)

## References

- [Compose Testing Docs](https://developer.android.com/jetpack/compose/testing)
- [Compose Multiplatform Testing](https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/UI_Testing)
- [AndroidX Test](https://developer.android.com/training/testing)
