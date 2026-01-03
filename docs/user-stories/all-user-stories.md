# All User Stories - Complete List

> **Note:** All stories use DBXXX numbering. Organize them on your project board as needed.
>
> **Important:** Localization folders already exist for 5 languages (en, pt-PT, pt-BR, es-ES, en-GB). All UI stories should add strings to existing language files.

---

## Epic 1: Branding & Splash Screen

Establish the DouroBats brand identity and create a professional first impression through native splash screens. This epic covers asset preparation and platform-specific implementation for both Android and iOS, ensuring smooth transitions and proper dark mode support.

<!-- EPIC:DATA #1 #2 #3 #4 -->

### DB001: Prepare Splash Screen Assets
**Story Points:** 2

**As a** designer/developer
**I need** high-resolution splash screen assets
**So that** the app can display a professional branded loading screen

**Technical Details:**
- Create/obtain splash logo SVG or PNG (min 1024x1024)
- Define brand primary background color (hex code)
- Define brand dark mode background color (hex code)
- Document asset specifications for both platforms

**Acceptance Criteria:**
- [ ] High-resolution logo asset is available (1024x1024 minimum)
- [ ] Light mode brand color documented
- [ ] Dark mode brand color documented
- [ ] Assets meet Android safe zone requirements (maskable icon)
- [ ] Assets meet iOS Retina display requirements

**Notes:** Theme system already exists and handles dark/light modes.

---

### DB002: Android Splash Screen Implementation
**Story Points:** 5

**As a** user on Android
**I want** to see a branded splash screen when launching the app
**So that** I have a professional first impression

**Technical Details:**
- Implement Android 12+ Splash Screen API
- Update `windowSplashScreenAnimatedIcon` in `themes.xml`
- Add splash logo to `composeApp/src/androidMain/res/drawable`
- Ensure icon fits within safe zone (masking circle)
- Use existing theme colors for background

**Acceptance Criteria:**
- [ ] Splash screen displays on Android 12+ devices
- [ ] Icon is centered and not clipped on various screen sizes
- [ ] Background color uses existing theme colors
- [ ] Splash screen appears before Compose UI loads
- [ ] Works on Android 5.0+ (with compat library)
- [ ] Respects existing dark/light theme

**Notes:** AppTheme already exists with dark mode support.

---

### DB003: iOS Splash Screen Implementation
**Story Points:** 5

**As a** user on iOS
**I want** to see a branded splash screen when launching the app
**So that** I have a professional first impression

**Technical Details:**
- Update `LaunchScreen.storyboard` or `Assets.xcassets`
- Use PDF or SVG vector for logo (single scale, sharp on all displays)
- Location: `iosApp/iosApp/Assets.xcassets`
- Use existing theme colors for background

**Acceptance Criteria:**
- [ ] Splash screen displays on iOS devices
- [ ] Icon is sharp on all Retina displays (2x, 3x)
- [ ] Icon is centered and not clipped on various screen sizes
- [ ] Background color uses existing theme colors
- [ ] Works on iPhone and iPad
- [ ] Respects existing dark/light theme

**Notes:** AppTheme already exists with dark mode support.

---

### DB004: Splash to App Transition
**Story Points:** 3

**As a** user
**I want** a smooth transition from splash screen to the main app
**So that** I don't experience jarring visual changes

**Technical Details:**
- Match background color in App.kt to splash background
- Ensure no white flash between native splash and Compose UI
- Test transition smoothness on both platforms
- Handle different screen densities

**Acceptance Criteria:**
- [ ] No white flash occurs on Android
- [ ] No white flash occurs on iOS
- [ ] Transition feels smooth and professional
- [ ] Background colors match exactly between splash and first screen
- [ ] Works correctly in both light and dark modes

---

## Epic 2: Navigation Refactoring

Transform the navigation system to be truly multi-sport by replacing volleyball-specific elements with universal sports icons and terminology. This ensures the app welcomes athletes from all sports equally.

<!-- EPIC:DATA #5 #6 #7 -->

**Notes:** Bottom navigation already exists with emoji icons. These stories update it to be sport-agnostic.

### DB005: Update Navigation Icons to Sport-Agnostic
**Story Points:** 3

**As a** multi-sport athlete
**I want** navigation icons that represent all sports
**So that** the app doesn't feel volleyball-specific

**Technical Details:**
- Replace emoji icons in existing Screen enum with Material Icons
- Use Material Icons Extended or compose-icons library
- Update NavigationItem in App.kt
- Icons needed: Home/Sports, Calendar/Sessions, Library/Resources, Profile

**Acceptance Criteria:**
- [ ] All navigation icons are sport-agnostic Material Icons
- [ ] Icons display correctly on both Android and iOS
- [ ] Icons are vector-based (scale without pixelation)
- [ ] Remove emoji-based icons from Screen enum

**Existing Code:** `Screen` enum in App.kt currently uses emojis.

---

### DB006: Rename Navigation Labels
**Story Points:** 2

**As a** user
**I want** clear navigation labels
**So that** I understand what each section does

**Technical Details:**
- Update Screen enum labels:
  - "Training" → "Sessions"
  - "Home" → Keep or change to "Sports"
- Update stringResource references
- Update all screen titles to match navigation labels
- **Add new strings to all 5 existing localization files**

**Acceptance Criteria:**
- [ ] All navigation labels use new sport-agnostic terminology
- [ ] String keys are properly referenced (no hardcoded strings)
- [ ] Screen headers match their navigation labels
- [ ] Labels are clear and descriptive
- [ ] Strings added to all 5 language files (en, pt-PT, pt-BR, es-ES, en-GB)

**Existing Code:** Screen enum in App.kt, existing localization in core/ui/composeResources.

---

### DB007: Navigation Visual Polish
**Story Points:** 3

**As a** user
**I want** clear visual feedback on navigation
**So that** I know which section I'm viewing

**Technical Details:**
- Configure selected/unselected icon states in existing NavigationBar
- Apply correct tinting/colors based on existing theme
- Ensure colors meet accessibility contrast requirements
- Test in both light and dark modes

**Acceptance Criteria:**
- [ ] Selected icons are clearly highlighted
- [ ] Unselected icons are appropriately muted
- [ ] Icon colors work in light mode
- [ ] Icon colors work in dark mode
- [ ] Visual feedback is immediate when tapping
- [ ] Meets WCAG contrast requirements

**Existing Code:** NavigationBar in App.kt, AppTheme with color scheme.

---

## Epic 3: Sessions & Booking - UI

Build the core user experience for browsing and booking training sessions. This epic delivers intuitive calendar views, session details, and booking actions with optimistic updates for a responsive feel. This is the primary feature users will interact with daily.

<!-- EPIC:DATA #8 #9 #10 #11 #12 #13 #14 -->

### DB008: Calendar UI Component - Week View
**Story Points:** 8

**As a** user
**I want** to see a week calendar view
**So that** I can browse dates to find sessions

**Technical Details:**
- Implement horizontal scrollable week view using Compose LazyRow
- Display current week with ability to scroll to previous/next weeks
- Use kotlinx-datetime for date handling (already in project)
- Make dates tappable with visual selection state
- Show current day indicator
- Handle timezone correctly
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Week view displays 7 days (Sun-Sat or Mon-Sun based on locale)
- [ ] User can scroll horizontally to see other weeks
- [ ] Tapping a date selects it
- [ ] Selected date is visually highlighted
- [ ] Current day has special indicator
- [ ] Works on both Android and iOS
- [ ] Smooth scrolling performance
- [ ] All strings localized in 5 languages

**Existing Code:** kotlinx-datetime already imported, TrainingSession model exists.

---

### DB009: Calendar UI Component - Month View
**Story Points:** 8

**As a** user
**I want** to see a full month calendar
**So that** I can plan sessions further ahead

**Technical Details:**
- Implement month grid view (7 columns, ~6 rows)
- Use LazyVerticalGrid or custom layout
- Show current month with navigation to other months
- Use kotlinx-datetime (already in project)
- Handle months with different number of days
- Show dates from adjacent months (grayed out)
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Month grid displays correctly
- [ ] Can navigate to previous/next months
- [ ] Dates from other months shown but disabled/grayed
- [ ] Current month label displayed
- [ ] Tapping date selects it
- [ ] Works on both platforms
- [ ] Handles leap years correctly
- [ ] All strings localized in 5 languages

**Existing Code:** kotlinx-datetime already imported.

---

### DB010: Session List Display
**Story Points:** 5

**As a** user
**I want** to see available sessions for selected date
**So that** I can choose which session to book

**Technical Details:**
- Enhance existing ScheduleScreen with session list
- Display list of sessions for selected date
- Show session details: time, sport, venue, capacity
- Use LazyColumn for performance
- Show session status (Available, Full, Cancelled)
- Display current attendance (e.g., "15/20 confirmed")
- Handle empty state (no sessions for date)
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Sessions list updates when date changes
- [ ] Each session shows all relevant information
- [ ] Available capacity is clearly visible
- [ ] Empty state message when no sessions
- [ ] List scrolls smoothly
- [ ] Sessions sorted by time
- [ ] All strings localized in 5 languages

**Existing Code:** ScheduleScreen already exists, TrainingSession model exists.

---

### DB011: Session Details Screen
**Story Points:** 5

**As a** user
**I want** to see full session details
**So that** I can make an informed booking decision

**Technical Details:**
- Create session details screen
- Display: sport, date/time, venue (with address), target skill level, capacity, description
- Show list of confirmed attendees (if applicable)
- Show "Book" button if available
- Show "Cancel Booking" button if already booked
- Handle navigation from session list
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] All session information displayed clearly
- [ ] Venue address is shown
- [ ] Target skill level indicated (but noted as recommendation)
- [ ] Booking button state reflects availability
- [ ] Navigation back to list works
- [ ] Layout works on different screen sizes
- [ ] All strings localized in 5 languages

---

### DB012: Booking Action - UI and Optimistic Updates
**Story Points:** 5

**As a** user
**I want** to book a session quickly
**So that** I can secure my spot

**Technical Details:**
- "Book" button triggers booking action
- Show loading indicator during API call (or fake delay)
- Optimistic UI update (mark as booked immediately)
- Rollback if booking fails
- Update session capacity in list
- Show success confirmation (SnackBar)
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Book button is prominent and accessible
- [ ] Loading state shown during booking
- [ ] UI updates immediately (optimistic)
- [ ] Success message displayed
- [ ] Session marked as booked in list
- [ ] Capacity decrements immediately
- [ ] Rollback works if booking fails
- [ ] All strings localized in 5 languages

---

### DB013: Cancel Booking - UI
**Story Points:** 3

**As a** user
**I want** to cancel my booking
**So that** I can free up my spot if I can't attend

**Technical Details:**
- "Cancel Booking" button on session details
- Show confirmation dialog before cancelling
- Loading indicator during cancellation
- Optimistic UI update
- Update session capacity
- Show cancellation success message
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Cancel button visible when user has booked
- [ ] Confirmation dialog prevents accidental cancellation
- [ ] Loading state during cancellation
- [ ] Session unmarked as booked
- [ ] Capacity increments
- [ ] Success message shown
- [ ] All strings localized in 5 languages

---

### DB014: Empty State - No Sessions
**Story Points:** 2

**As a** user
**I want** clear feedback when no sessions exist
**So that** I understand why the list is empty

**Technical Details:**
- Design empty state UI (illustration + message)
- Show when no sessions found for selected date
- Provide helpful message (e.g., "No sessions scheduled for this date")
- Consider adding "Create Session" CTA for committee members
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Empty state shows when no sessions
- [ ] Message is clear and helpful
- [ ] Visual design is polished (not just text)
- [ ] Works in both light and dark modes
- [ ] All strings localized in 5 languages

---

## Epic 4: Domain Layer & Architecture

Establish the foundational business logic and data architecture. This epic defines domain models, repository interfaces, and patterns that all other features will build upon. A well-designed domain layer ensures consistency and maintainability across the entire application.

<!-- EPIC:DATA #15 #16 #52 #17 #18 -->

**Notes:** Some domain infrastructure already exists (TrainingSession, repositories, Koin DI).

### DB015: Expand Domain Models for Sessions
**Story Points:** 5

**As a** developer
**I need** comprehensive domain entities for the session system
**So that** the app has a clear business model

**Technical Details:**
- Expand existing `TrainingSession` to full `Session` entity:
  - id, sportId, dateTime, duration, venueId, targetLevel, capacity, currentAttendees, status
- Create `Sport` entity (id, name, description, iconResource)
- Define `SkillLevel` enum (Beginner, Intermediate, Advanced)
- Define `SessionStatus` enum (Scheduled, Cancelled, Completed)
- Keep using kotlinx-datetime types (already in project)
- Location: `core/domain/src/commonMain/kotlin/.../model`

**Acceptance Criteria:**
- [ ] Session entity has all business-critical fields
- [ ] Sport entity defined
- [ ] Enums are well-defined
- [ ] Use kotlinx-datetime types
- [ ] Documented with KDoc comments

**Existing Code:** TrainingSession model exists, kotlinx-datetime already used.

---

### DB016: Extended Domain Models - Venue, User, Booking
**Story Points:** 5

**As a** developer
**I need** additional domain entities
**So that** the app can handle venues, users, and bookings

**Technical Details:**
- Create `Venue` entity (id, name, address, capacity, supportedSports)
- Expand existing `UserProfile` to include roles
- Create `Booking` entity (id, sessionId, userId, confirmedAt, status)
- Create `UserRole` enum (Athlete, Supporter, Committee)
- Location: `core/domain/src/commonMain/kotlin/.../model`

**Acceptance Criteria:**
- [ ] Venue supports multiple sports (List<Sport>)
- [ ] UserProfile expanded with roles
- [ ] Booking tracks confirmation time and status
- [ ] All entities documented

**Existing Code:** UserProfile model already exists.

---

### DB052: Create Result Wrapper for Async Operations
**Story Points:** 3

**As a** developer
**I need** a standardized Result wrapper for async operations
**So that** repositories can handle success/error/loading states consistently

**Technical Details:**
- Create `Result<T>` sealed class in `core/domain/src/commonMain/kotlin/.../common`
- Three states:
  - `data class Success<T>(val data: T) : Result<T>()`
  - `data class Error<T>(val exception: Throwable, val message: String? = null) : Result<T>()`
  - `class Loading<T> : Result<T>()`
- Add helper functions:
  - `isSuccess()`: Boolean
  - `isError()`: Boolean
  - `isLoading()`: Boolean
  - `getOrNull()`: T?
  - `getOrThrow()`: T
  - `onSuccess(action: (T) -> Unit)`: Result<T>
  - `onError(action: (Throwable) -> Unit)`: Result<T>
- Include comprehensive KDoc with usage examples
- Add unit tests demonstrating all states and helper functions
- Location: `core/domain/src/commonMain/kotlin/.../common`

**Acceptance Criteria:**
- [ ] Result sealed class defined with all three states
- [ ] All helper functions implemented and tested
- [ ] Comprehensive KDoc documentation with code examples
- [ ] Unit tests cover all states and helper functions
- [ ] Located in core/domain/common package
- [ ] No external dependencies (pure Kotlin)

**Notes:** This is foundational infrastructure that will be used by DB017, DB018, and all future repository interfaces.

---

### DB017: Session Repository Interface
**Story Points:** 3

**As a** developer
**I need** session repository interface
**So that** data layer is decoupled from UI

**Dependencies:** DB052 (Result wrapper must exist first)

**Technical Details:**
- Create `SessionRepository` interface (similar to existing `SettingsRepository` pattern)
- Use `Result<T>` sealed class from DB052
- Methods:
  - `suspend fun getUpcomingSessions(sportId: String?): Result<List<Session>>`
  - `suspend fun getSessionById(id: String): Result<Session>`
  - `suspend fun getSessionsByDate(date: LocalDate): Result<List<Session>>`
  - `suspend fun bookSession(sessionId: String, userId: String): Result<Boolean>`
  - `suspend fun cancelBooking(sessionId: String, userId: String): Result<Boolean>`
- Location: `core/domain/src/commonMain/kotlin/.../repository`

**Acceptance Criteria:**
- [ ] Repository interface defined
- [ ] All methods use suspend functions
- [ ] Uses Result<T> wrapper from core/domain/common (DB052)
- [ ] Methods documented with expected behavior
- [ ] Interfaces don't leak implementation details

**Existing Code:** SettingsRepository and TrainingRepository patterns already exist, follow same structure.

---

### DB018: Auth Repository Interface
**Story Points:** 2

**As a** developer
**I need** authentication repository interface
**So that** auth data layer is decoupled

**Technical Details:**
- Create `AuthRepository` interface (similar to existing repository pattern)
- Methods:
  - `suspend fun login(email: String, password: String): Result<User>`
  - `suspend fun signUp(name: String, email: String, password: String, sports: List<String>): Result<User>`
  - `suspend fun logout(): Result<Boolean>`
  - `suspend fun getCurrentUser(): Result<User?>`
- Use existing Result<T> wrapper
- Location: `core/domain/src/commonMain/kotlin/.../repository`

**Acceptance Criteria:**
- [ ] AuthRepository interface defined
- [ ] Methods use suspend functions
- [ ] Uses existing Result wrapper
- [ ] Methods documented

**Existing Code:** Follow SettingsRepository pattern.

---

## Epic 5: Data Layer - Fake Repositories

Enable parallel UI and backend development by creating fake repositories with realistic data. This unblocks frontend work while backend APIs are being developed, and serves as a reference implementation for the real repositories.

<!-- EPIC:DATA #19 #20 -->

**Notes:** Koin DI already set up. Add to existing DataModule.

### DB019: Fake Session Repository
**Story Points:** 5

**As a** developer
**I need** a fake session repository
**So that** I can develop UI without waiting for backend

**Technical Details:**
- Implement `FakeSessionRepository : SessionRepository` in `core/data`
- Return hardcoded list of sessions with realistic data
- Use `delay(500)` to simulate network latency
- Store bookings in memory (MutableList)
- Handle booking/cancellation logic (capacity management)
- Add to existing `DataModule` (already has SettingsRepository)

**Acceptance Criteria:**
- [ ] Returns varied, realistic session data
- [ ] Simulates network delay
- [ ] Booking decrements capacity
- [ ] Cancellation increments capacity
- [ ] Can't book when full
- [ ] Can't book same session twice
- [ ] Injectable via existing Koin setup

**Existing Code:** DataModule exists, follow SettingsRepositoryImpl pattern.

---

### DB020: Fake Auth Repository
**Story Points:** 5

**As a** developer
**I need** a fake auth repository
**So that** I can develop auth UI without backend

**Technical Details:**
- Implement `FakeAuthRepository : AuthRepository` in `core/data`
- Methods:
  - `login(email, password)` - accepts any credentials, returns mock user
  - `signUp(...)` - creates mock user
  - `logout()` - clears session
  - `getCurrentUser()` - returns stored user or null
- Store current user in memory
- Use `delay(800)` to simulate network
- Add to existing `DataModule`

**Acceptance Criteria:**
- [ ] Login succeeds with any credentials
- [ ] Sign-up creates mock user
- [ ] Logout clears current user
- [ ] getCurrentUser returns correct state
- [ ] Network delay simulated
- [ ] Injectable via existing Koin setup

**Existing Code:** DataModule exists, follow existing repository pattern.

---

## Epic 6: State Management - ViewModels

Implement reactive state management using ViewModels and StateFlow. This epic bridges the UI and data layers, managing loading states, errors, and business logic in a lifecycle-aware manner that works seamlessly across both platforms.

<!-- EPIC:DATA #21 #22 -->

**Notes:** ViewModel pattern should follow existing SettingsRepository + DataStore pattern.

### DB021: Sessions ViewModel
**Story Points:** 5

**As a** developer
**I need** a ViewModel for sessions
**So that** UI state is managed reactively

**Technical Details:**
- Create `SessionsViewModel` in feature/sessions
- Use `StateFlow` for:
  - `selectedDate: StateFlow<LocalDate>`
  - `sessions: StateFlow<List<Session>>`
  - `loadingState: StateFlow<Boolean>`
  - `errorMessage: StateFlow<String?>`
- Inject `SessionRepository` via existing Koin setup
- Load sessions when date changes
- Handle booking/cancellation actions
- Add to Koin module

**Acceptance Criteria:**
- [ ] ViewModel uses StateFlow for reactive updates
- [ ] Sessions load when date changes
- [ ] Loading and error states handled
- [ ] Booking/cancellation updates state
- [ ] Injectable via Koin
- [ ] Works on both platforms

**Existing Code:** Follow SettingsRepository + StateFlow pattern from existing code.

---

### DB022: Auth ViewModel
**Story Points:** 5

**As a** developer
**I need** a ViewModel for authentication
**So that** auth state is managed globally

**Technical Details:**
- Create `AuthViewModel` in feature/auth
- Define `AuthState` sealed class:
  - `object Unauthenticated`
  - `object Loading`
  - `data class Authenticated(val user: User)`
  - `data class Error(val message: String)`
- Use `StateFlow<AuthState>`
- Inject `AuthRepository` via Koin
- Methods: `login(email, password)`, `signUp(...)`, `logout()`, `checkAuthStatus()`
- Add to Koin module

**Acceptance Criteria:**
- [ ] AuthState covers all auth states
- [ ] StateFlow for reactive UI updates
- [ ] Login/signup/logout methods implemented
- [ ] State transitions correct
- [ ] Injectable via Koin
- [ ] Persists across configuration changes

**Existing Code:** Follow existing ViewModel + repository + Koin pattern.

---

## Epic 7: Authentication - UI

Create a secure and user-friendly authentication experience. This epic covers all auth flows including login, sign-up, password recovery, and logout, with proper validation and error handling to guide users through the process smoothly.

<!-- EPIC:DATA #23 #24 #25 #26 #27 -->

### DB023: Login Screen UI
**Story Points:** 5

**As a** returning user
**I want** to log into my account
**So that** I can access my bookings

**Technical Details:**
- Replace placeholder LoginScreen in App.kt with full implementation
- Email and password text fields
- Input validation (email format, non-empty)
- Password visibility toggle
- "Login" button
- Link to Sign-Up screen
- Link to Forgot Password
- Show validation errors inline
- Loading state during login
- Error messages in SnackBar
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Email field validates format
- [ ] Password field validates non-empty
- [ ] Password toggle works
- [ ] Login button triggers ViewModel action
- [ ] Loading indicator shows during login
- [ ] Errors display in SnackBar
- [ ] Navigation to Sign-Up works
- [ ] All strings localized in 5 languages

**Existing Code:** Placeholder LoginScreen exists in App.kt, replace with full implementation.

---

### DB024: Sign-Up Screen UI
**Story Points:** 5

**As a** new user
**I want** to create an account
**So that** I can start booking sessions

**Technical Details:**
- Create `SignUpScreen.kt` in feature/auth
- Fields: Name, Email, Password, Confirm Password
- Sport preferences selection (multi-select chips)
- Input validation (password match, email format, etc.)
- "Create Account" button
- Link to Login screen
- Loading state
- Error handling
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] All fields validated
- [ ] Password confirmation matches
- [ ] Email format validated
- [ ] Sport preferences selectable
- [ ] Create Account triggers ViewModel
- [ ] Loading state during signup
- [ ] Errors shown in SnackBar
- [ ] Link to Login works
- [ ] All strings localized in 5 languages

---

### DB025: Forgot Password UI
**Story Points:** 3

**As a** user who forgot password
**I want** to reset my password
**So that** I can regain access to my account

**Technical Details:**
- Create `ForgotPasswordScreen.kt`
- Email input field
- "Send Reset Link" button
- Success message screen
- Email validation
- Loading state
- Error handling
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Email field with validation
- [ ] Send button triggers action
- [ ] Success message shown
- [ ] Loading state during request
- [ ] Errors handled gracefully
- [ ] Can navigate back to login
- [ ] All strings localized in 5 languages

---

### DB026: Error Handling for Auth Flows
**Story Points:** 3

**As a** user
**I want** clear error messages during authentication
**So that** I understand what went wrong

**Technical Details:**
- Define error messages for common scenarios:
  - Invalid credentials
  - Network error
  - Email already exists
  - Weak password
  - Server error
- Display errors in SnackBar (Material 3)
- Use user-friendly language
- **Add all error messages to existing 5 localization files**

**Acceptance Criteria:**
- [ ] All auth errors have messages
- [ ] Messages displayed in SnackBar
- [ ] Messages are user-friendly
- [ ] Network errors distinguished from validation errors
- [ ] All messages localized in 5 languages

---

### DB027: Logout Functionality
**Story Points:** 2

**As a** user
**I want** to log out of my account
**So that** I can securely end my session

**Technical Details:**
- Add logout button to existing SettingsScreen
- Show confirmation dialog
- Call `AuthViewModel.logout()`
- Clear auth state
- Navigate to Login screen
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Logout button accessible in SettingsScreen
- [ ] Confirmation dialog prevents accidents
- [ ] Logout clears auth state
- [ ] Navigation to Login screen
- [ ] No user data remains after logout
- [ ] All strings localized in 5 languages

**Existing Code:** SettingsScreen already exists.

---

## Epic 8: Auth-Based Navigation & Routing

Implement intelligent navigation that adapts to the user's authentication state. This epic ensures users are automatically routed to the appropriate screens and that sessions persist securely across app restarts using encrypted storage.

<!-- EPIC:DATA #28 #29 -->

### DB028: Auth-Based Navigation Wrapper
**Story Points:** 3

**As a** user
**I want** automatic routing based on login status
**So that** I don't have to manually navigate

**Technical Details:**
- Update existing App.kt auth logic (currently TODO)
- Replace `isAuthenticated` mutableState with AuthViewModel
- Implement proper Auth Wrapper:
  ```kotlin
  when (authState) {
      is Authenticated -> MainNavigation()
      is Unauthenticated -> AuthNavigation()
      is Loading -> LoadingScreen()
  }
  ```
- Create `AuthNavigation` composable (Login/SignUp screens)
- `MainNavigation` already exists (MainApp function)
- Smooth transition from Splash

**Acceptance Criteria:**
- [ ] App routes based on AuthState
- [ ] Authenticated → Main app
- [ ] Unauthenticated → Login
- [ ] Loading → Loading screen
- [ ] Transition is smooth (no flicker)
- [ ] Deep links respect auth state

**Existing Code:** App.kt has TODO for auth flow, MainApp function exists.

---

### DB029: Token Persistence with Encrypted Storage
**Story Points:** 5

**As a** user
**I want** my session to persist across app restarts
**So that** I don't have to login every time

**Technical Details:**
- Expand existing DataStore usage (already used for settings)
- Android: EncryptedSharedPreferences
- iOS: Keychain
- Create `SecureStorage` expect/actual (similar to existing DataStoreFactory pattern)
- Methods: `saveAuthToken(token)`, `getAuthToken()`, `clearAuthToken()`
- AuthViewModel checks token on app launch

**Acceptance Criteria:**
- [ ] Auth token stored securely
- [ ] Android uses EncryptedSharedPreferences
- [ ] iOS uses Keychain
- [ ] Token persists across app restarts
- [ ] Token cleared on logout
- [ ] No plaintext token storage

**Existing Code:** DataStore already implemented for settings, follow same pattern.

---

## Epic 9: Social Authentication

Provide modern authentication options through Google and Apple sign-in. This epic reduces friction for new users by allowing them to authenticate with existing accounts they trust, while maintaining the same security standards as traditional email/password authentication.

<!-- EPIC:DATA #30 #31 #32 #33 #34 #35 -->

### DB030: Social Auth Infrastructure Setup
**Story Points:** 3

**As a** developer
**I need** OAuth credentials configured
**So that** social login can work

**Technical Details:**
- Register app in Google Cloud Console
- Generate OAuth 2.0 Client IDs (Android, iOS, Web)
- Enable "Sign in with Apple" in Apple Developer Portal
- Configure Services ID for Apple
- Document all credentials in secure location
- Set up redirect URLs

**Acceptance Criteria:**
- [ ] Google Cloud project created
- [ ] Android Client ID generated
- [ ] iOS Client ID generated
- [ ] Apple Sign-In capability enabled
- [ ] Credentials documented
- [ ] Development and production configs separated

---

### DB031: SocialAuthManager Interface
**Story Points:** 2

*As a** developer
**I need** a shared interface for social auth
**So that** platform implementations are consistent

**Technical Details:**
- Cre*ate `SocialAuthManager` expect/actual interface
- Methods:
  - `suspend fun signInWithGoogle(): Result<SocialAuthResult>`
  - `suspend fun signInWithApple(): Result<SocialAuthResult>`
- Create `SocialAuthResult` data class (idToken, email, name, provider)
- Define `AuthProvider` enum (Google, Apple)
- Use existing Result<T> wrapper

**Acceptance Criteria:**
- [ ] Interface defined as expect/actual
- [ ] SocialAuthResult captures user data
- [ ] Methods documented
- [ ] Platform availability checked
- [ ] Uses existing Result wrapper

---

### DB032: Android Google Sign-In Implementation
**Story Points:** 8

**As a** user on Android
**I want** to sign in with Google
**So that** I can use my Google account

**Technical Details:**
- Add dependencies:
  - `androidx.credentials:credentials`
  - `androidx.credentials:credentials-play-services-auth`
  - `com.google.android.libraries.identity.googleid:googleid`
- Implement `actual SocialAuthManager` for Android
- Use CredentialManager API
- Configure `GetGoogleIdOption` with web client ID
- Handle `GoogleIdTokenCredential`
- Extract ID token
- Error handling (cancellation, network)

**Acceptance Criteria:**
- [ ] Google Sign-In bottom sheet appears
- [ ] User can select Google account
- [ ] ID token extracted successfully
- [ ] User email and name captured
- [ ] Cancellation handled gracefully
- [ ] Network errors handled
- [ ] Tested on Android API 28+

---

### DB033: iOS Apple Sign-In Implementation
**Story Points:** 8

**As a** user on iOS
**I want** to sign in with Apple
**So that** I can use my Apple ID

**Technical Details:**
- Link `AuthenticationServices` framework
- Implement `actual SocialAuthManager` for iOS
- Use `ASAuthorizationAppleIDProvider`
- Configure requested scopes (email, fullName)
- Implement `ASAuthorizationControllerDelegate`
- Extract `identityToken` from credential
- Handle errors

**Acceptance Criteria:**
- [ ] Apple Sign-In prompt appears
- [ ] Identity token extracted
- [ ] User email captured (first time)
- [ ] User name captured (first time)
- [ ] Cancellation handled
- [ ] App has Sign-In capability
- [ ] Tested on iOS 13+

---

### DB034: Social Auth UI Integration
**Story Points:** 5

**As a** user
**I want** social login buttons on auth screens
**So that** I can choose my preferred sign-in method

**Technical Details:**
- Add social login buttons to LoginScreen and SignUpScreen
- "Continue with Google" button
- "Continue with Apple" button (iOS only)
- Use KMPAuth library if available, or custom buttons
- Style buttons according to brand guidelines
- "OR" divider between social and email login
- Integrate with AuthViewModel
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Google button visible on both platforms
- [ ] Apple button visible on iOS only
- [ ] Buttons match app design
- [ ] Buttons trigger SocialAuthManager
- [ ] Loading state during social auth
- [ ] Errors handled
- [ ] Platform guidelines followed
- [ ] All strings localized in 5 languages

---

### DB035: Social Auth Error Handling
**Story Points:** 3

**As a** user
**I want** clear feedback when social login fails
**So that** I understand what happened

**Technical Details:**
- Handle social auth errors:
  - User cancellation (silent, no error)
  - Network errors
  - Invalid credentials
  - Unknown errors
- Display in SnackBar
- Update AuthViewModel state
- **Add all error messages to existing 5 localization files**

**Acceptance Criteria:**
- [ ] User cancellation doesn't show error
- [ ] Network errors have clear message
- [ ] Invalid credentials handled
- [ ] SnackBar displays errors
- [ ] AuthState updates correctly
- [ ] All messages localized in 5 languages

---

## Epic 10: Backend API Integration

Replace fake repositories with real backend API connections. This epic establishes the HTTP client infrastructure and implements actual API calls for authentication, sessions, bookings, and user profiles, connecting the app to live data and enabling real functionality.

<!-- EPIC:DATA #36 #37 #38 #39 #40 -->

### DB036: HTTP Client Setup with Ktor
**Story Points:** 5

**As a** developer
**I need** HTTP client configured
**So that** I can make API calls

**Technical Details:**
- Add Ktor client dependencies for KMP
- Configure in `core/network` module (already exists)
- Configure plugins:
  - ContentNegotiation (JSON)
  - Auth (Bearer token)
  - Logging
  - HttpTimeout
- Create `ApiConfig` object (base URL, timeouts)
- Support multiple environments (dev, staging, prod)

**Acceptance Criteria:**
- [ ] Ktor client configured in core/network
- [ ] JSON serialization works
- [ ] Bearer token auth configured
- [ ] Logging enabled for debugging
- [ ] Timeouts configured
- [ ] Environment switching supported
- [ ] Works on both platforms

**Existing Code:** core/network module already exists.

---

### DB037: Real Authentication API Integration
**Story Points:** 5

**As a** user
**I want** my credentials validated by the backend
**So that** my account is secure

**Technical Details:**
- Create `ApiAuthRepository : AuthRepository` in `core/data`
- Implement API calls:
  - `POST /api/auth/login` → JWT token + user
  - `POST /api/auth/signup` → JWT token + user
  - `POST /api/auth/refresh` → new JWT
  - `POST /api/auth/logout` → invalidate token
- Parse JWT and user data
- Handle API errors (401, 400, 500)
- Map to user-friendly errors
- Use feature flag or DI to switch between Fake and Api repository

**Acceptance Criteria:**
- [ ] Login calls real API
- [ ] Signup calls real API
- [ ] JWT token received and stored
- [ ] Invalid credentials show error
- [ ] Network errors handled
- [ ] User data populated from API
- [ ] Can switch between fake and real via DI/config

**Existing Code:** Follow existing repository pattern, add to DataModule.

---

### DB038: Real Session API Integration
**Story Points:** 5

**As a** user
**I want** to see real training sessions
**So that** I can book actual sessions

**Technical Details:**
- Create `ApiSessionRepository : SessionRepository` in `core/data`
- Implement API calls:
  - `GET /api/sessions?date={date}&sport={sportId}`
  - `GET /api/sessions/{id}`
  - `GET /api/sessions/upcoming`
  - `GET /api/sports`
  - `GET /api/venues`
- Handle pagination if needed
- Timezone conversions (UTC ↔ local) - use existing kotlinx-datetime
- Map backend models to domain models
- Use feature flag to switch between Fake and Api repository

**Acceptance Criteria:**
- [ ] Sessions load from real API
- [ ] Sports list from API
- [ ] Venues from API
- [ ] Timezone handling correct (using kotlinx-datetime)
- [ ] Loading states work
- [ ] Empty state when no sessions
- [ ] Errors handled gracefully
- [ ] Can switch between fake and real via DI/config

**Existing Code:** Use kotlinx-datetime for timezone handling.

---

### DB039: Real Booking API Integration
**Story Points:** 5

**As a** user
**I want** to book real sessions
**So that** my attendance is registered

**Technical Details:**
- Implement booking API calls in `ApiSessionRepository`:
  - `POST /api/bookings` → create booking
  - `DELETE /api/bookings/{id}` → cancel booking
  - `GET /api/bookings/my-bookings` → user's bookings
- Handle capacity validation (backend enforces)
- Handle conflicts (already booked)
- Optimistic UI with rollback on failure
- Real-time capacity updates (poll or websocket)

**Acceptance Criteria:**
- [ ] Booking creates real reservation
- [ ] Backend enforces capacity
- [ ] Capacity updates immediately
- [ ] Cancellation works
- [ ] User can see all bookings
- [ ] Errors handled (full, conflict, etc.)
- [ ] Optimistic UI provides feedback

---

### DB040: User Profile API Integration
**Story Points:** 3

**As a** user
**I want** to update my profile
**So that** my information is accurate

**Technical Details:**
- Implement API calls:
  - `GET /api/users/me` → user profile
  - `PUT /api/users/me` → update profile
  - `POST /api/users/me/avatar` → upload picture
  - `PUT /api/users/me/sports` → update preferences
- Enhance existing SettingsScreen or create Profile screen
- Handle image upload (multipart/form-data)
- Validation before saving
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Profile loads from API
- [ ] User can update name, email, sports
- [ ] Changes save to backend
- [ ] Profile picture upload works
- [ ] Validation errors shown
- [ ] Success feedback on save
- [ ] All strings localized in 5 languages

**Existing Code:** SettingsScreen exists, UserProfile model exists.

---

## Epic 11: Advanced Session Management

Empower committee members with powerful tools to manage recurring sessions and control calendar access. This epic includes templates for automatic session generation, calendar locking to prevent over-scheduling, and waiting list functionality for popular sessions.

<!-- EPIC:DATA #41 #42 #43 #44 -->

### DB041: Session Templates - Create Template
**Story Points:** 5

**As a** committee member
**I want** to create recurring session templates
**So that** I can generate schedules quickly

**Technical Details:**
- Create `SessionTemplate` entity (dayOfWeek, startTime, duration, venue, sport, capacity, targetLevel)
- UI for creating templates
- Fields: recurrence pattern, all session fields
- Save template via API or local storage
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Template form has all necessary fields
- [ ] Day of week selection
- [ ] Time picker
- [ ] Template saves successfully
- [ ] Templates list displays created templates
- [ ] All strings localized in 5 languages

---

### DB042: Session Templates - Generate Sessions from Template
**Story Points:** 5

**As a** committee member
**I want** to generate sessions from templates
**So that** I don't have to create each session manually

**Technical Details:**
- "Generate from Templates" button
- Select date range (e.g., whole month)
- Select which templates to use
- Generate individual sessions for each recurrence
- Each session is independent (can be edited later)
- API call to create multiple sessions
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Can select date range
- [ ] Can select templates
- [ ] Sessions generated correctly
- [ ] Each session independent
- [ ] Success feedback shown
- [ ] Generated sessions appear in calendar
- [ ] All strings localized in 5 languages

---

### DB043: Calendar Access Control - Lock/Unlock Periods
**Story Points:** 5

**As a** committee member with Manage Calendar privilege
**I want** to control which dates can have sessions
**So that** we don't over-schedule

**Technical Details:**
- Implement calendar locking system
- UI for unlocking date ranges
- Enforce 3-month maximum ahead
- Enforce sequential unlocking (no gaps)
- Per-sport calendars
- API calls to lock/unlock periods
- Audit trail (who unlocked when)
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Calendar locked by default
- [ ] Committee can unlock ranges
- [ ] 3-month limit enforced
- [ ] Sequential unlocking enforced
- [ ] Audit trail recorded
- [ ] Per-sport independence
- [ ] All strings localized in 5 languages

---

### DB044: Session Capacity Management - Waiting List
**Story Points:** 5

**As a** user
**I want** to join a waiting list when session is full
**So that** I can get a spot if someone cancels

**Technical Details:**
- Add "Join Waiting List" button when session full
- Store waiting list positions
- Auto-book when spot available
- Notification when spot opens
- API support for waiting lists
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Waiting list option when full
- [ ] User added to waiting list
- [ ] Position in list shown
- [ ] Auto-booking when available
- [ ] Notification sent
- [ ] Can leave waiting list
- [ ] All strings localized in 5 languages

---

## Epic 12: User Roles & Permissions

Implement role-based access control to enable distributed committee management. This epic allows committee admins to delegate specific privileges to members, and provides tools for managing athlete skill levels across multiple sports with proper tracking and auditing.

<!-- EPIC:DATA #45 #46 -->

### DB045: Committee Member Roles - RBAC Setup
**Story Points:** 8

**As a** committee admin
**I want** to assign privileges to members
**So that** they can help manage the system

**Technical Details:**
- Implement role-based access control
- Five privileges: Manage Sessions, Manage Calendar, Manage Athletes, Manage Venues, View Analytics
- Committee admin has all privileges
- UI for assigning privileges
- Backend API for role management
- Audit trail for changes
- Expand existing UserProfile model with roles
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Privileges can be assigned
- [ ] UI restricted based on privileges
- [ ] Committee admin has all privileges
- [ ] Changes logged in audit trail
- [ ] Backend enforces permissions
- [ ] All strings localized in 5 languages

**Existing Code:** UserProfile model exists, expand it.

---

### DB046: Athlete Skill Level Management
**Story Points:** 5

**As a** committee member with Manage Athletes privilege
**I want** to update athlete skill levels
**So that** they see appropriate recommendations

**Technical Details:**
- UI for updating skill levels
- Multi-sport support (different level per sport)
- Skill level history tracking
- Require reason/note for changes
- API calls to update levels
- Display in athlete profile
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Athletes have levels per sport
- [ ] Committee can update with reason
- [ ] History tracked
- [ ] Sessions show level match
- [ ] Levels are recommendations only
- [ ] All strings localized in 5 languages

---

## Epic 13: Polish & Additional Features

Enhance the user experience with quality-of-life features that make the app more engaging and useful. This epic includes push notifications for timely reminders, profile pictures for personalization, and analytics for data-driven decision making.

<!-- EPIC:DATA #47 #48 #49 -->

### DB047: Push Notifications Setup
**Story Points:** 5

**As a** user
**I want** notifications about my bookings
**So that** I don't miss sessions

**Technical Details:**
- Integrate Firebase Cloud Messaging (FCM)
- Notification types: session reminder, cancellation, waitlist opening
- Notification permissions handling
- Settings screen for preferences (add to existing SettingsScreen)
- Deep links from notifications
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Notifications received on both platforms
- [ ] Reminder before session
- [ ] Cancellation notifications
- [ ] User can control preferences
- [ ] Deep links work
- [ ] All strings localized in 5 languages

**Existing Code:** SettingsScreen exists.

---

### DB048: Profile Picture Upload
**Story Points:** 3

**As a** user
**I want** to upload a profile picture
**So that** others can recognize me

**Technical Details:**
- Image picker (camera or gallery)
- Image cropping to square
- Resize before upload
- Upload to backend
- Display throughout app
- Default avatar if not set
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Can select from camera/gallery
- [ ] Can crop image
- [ ] Upload succeeds
- [ ] Avatar displayed everywhere
- [ ] Default avatar for new users
- [ ] All strings localized in 5 languages

---

### DB049: Analytics Dashboard for Committee
**Story Points:** 8

**As a** committee member with View Analytics privilege
**I want** to see attendance statistics
**So that** I can make informed decisions

**Technical Details:**
- Dashboard screen with charts
- Metrics: attendance rate, popular sessions, athlete participation
- Filter by sport, date range, venue
- Export to CSV/PDF
- Use charting library (e.g., Vico for Compose)
- **Add all strings to existing 5 localization files**

**Acceptance Criteria:**
- [ ] Dashboard shows key metrics
- [ ] Charts display trends
- [ ] Filters work correctly
- [ ] Data can be exported
- [ ] Only visible with privilege
- [ ] All strings localized in 5 languages

---

### DB050: Google OAuth Configuration (Android Focus)
**Story Points:** 8

**As a** developer
**I need** Google OAuth credentials configured
**So that** Android users can authenticate and the shared auth logic can be verified.

**Technical Details:**
- Register the project in the Google Cloud Console.
- Generate OAuth 2.0 Client IDs for Android (using the app's package name and SHA-1 certificate).
- Set up the OAuth Consent Screen (Internal/External for 500 users).
- Document credentials in a secure location (e.g., .env or Vault).
- Note: iOS client ID can be generated here too as it's free, but integration will be mocked.


**Acceptance Criteria:**
- [ ] Google Cloud project is active.
- [ ] Android Client ID is generated and functional.
- [ ] Authentication works on an Android device/emulator.
- [ ] Development and production Google Client IDs are separated.

---

### DB051: Apple Sign-In Configuration (iOS Focus)
**Story Points:** 8

**As a** developer
**I need** Apple Developer Portal credentials and capabilities configured
**So that** iOS users can use native "Sign in with Apple" for the booking system.

**Technical Details:**
- Enable the "Sign in with Apple" capability in the Apple Developer Program (requires $99 membership).
- Create an App ID and a Services ID for the association.
- Configure the Redirect URLs for Apple’s authentication callback.
- Generate and document the Client Secret (Key ID and Team ID).
- Replace the "Fake/Mock" implementation in the KMP iosMain source set with real credentials.

**Acceptance Criteria:**
- [ ] Apple Developer Program membership is active.
- [ ] Sign-in with Apple capability is linked to the app's Bundle ID.
- [ ] iOS users can successfully authenticate via the native Apple prompt.
- [ ] Credentials and Private Keys are stored securely.

---


## Summary

**Total User Stories:** 52
**Total Story Points:** 238

### Key Points:
- **Localization:** All 5 languages (en, pt-PT, pt-BR, es-ES, en-GB) already set up
- **Architecture:** Multi-module structure, Koin DI, repository pattern already in place
- **Theme:** Dark/light mode fully working with AppTheme
- **Existing Models:** TrainingSession, UserProfile, Language, Theme already exist
- **Existing Infrastructure:** DataStore, Settings system, basic screens already implemented

All UI stories now include: "Add all strings to existing 5 localization files"
