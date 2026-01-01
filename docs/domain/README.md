# DouroBats Domain Design

## Overview

This document defines the core domain entities, relationships, and business rules for the DouroBats training attendance management system. This represents our **Minimum Viable Domain (MVD)** - a focused, production-ready foundation optimized for **scheduling and booking training sessions**.

## Domain Context

**DouroBats** is a multi-sport association managing training sessions for various sports (Volleyball, Futsal, Padel, Basketball, Running, etc.). The system enables:
- Athletes to view and confirm attendance for training sessions
- Committee members to manage schedules with proper access control
- Supporters to view information (read-only)

### Key Stakeholders

- **Athletes**: View schedule, confirm attendance, have skill levels (Beginner/Intermediate/Advanced)
- **Supporters**: View-only access to schedules and information
- **Committee Members**: Manage sessions, venues, calendar access, and athletes (with granular privileges)

---

## Implementation Boundaries

This domain documentation represents **shared knowledge** between mobile and backend teams. The domain model, entities, and business rules apply to both, but implementation responsibilities differ:

### Backend Responsibilities (Separate Repository)

The backend is the **source of truth** and enforces all business rules:

- ✅ **Data Persistence**: Manage database transactions and data integrity
- ✅ **Business Rule Enforcement**: Validate all constraints and invariants
- ✅ **Authorization**: Verify user roles and Committee privileges
- ✅ **Use Case Execution**: Execute all business logic (confirm attendance, unlock calendar, etc.)
- ✅ **API Endpoints**: Expose REST/GraphQL endpoints for mobile consumption
- ✅ **Audit Trails**: Record all changes (assignedBy, changedBy, unlockedBy)
- ✅ **Capacity Management**: Enforce session capacity limits
- ✅ **Calendar Access Control**: Validate sequential unlocking and 3-month limit

### Mobile Responsibilities (This Repository)

The mobile app **displays and collects** data, with limited local validation:

- ✅ **UI/UX**: Display domain data to users in intuitive interfaces
- ✅ **User Input**: Collect and send data to backend via API
- ✅ **Client-Side Validation**: Fast feedback for UX (duplicates backend validation)
- ✅ **Role-Based UI**: Show/hide features based on user roles and privileges
- ✅ **Navigation**: Guide users through appropriate flows
- ✅ **Local Data**: Use repository pattern with local storage during development
- ✅ **API Integration**: Swap to backend API when ready (same repository interface)

### Development Approach

**Phase 1 - Local Development (Current):**
```kotlin
// Use local storage implementation
class LocalTrainingSessionRepository : TrainingSessionRepository {
    // SQLite / SharedPreferences / in-memory
}
```

**Phase 2 - Backend Integration (When Ready):**
```kotlin
// Swap to API implementation (same interface!)
class RemoteTrainingSessionRepository(
    private val apiClient: ApiClient
) : TrainingSessionRepository {
    // REST API calls
}
```

**Key Principle:** Mobile displays and validates for UX, but **backend is always the source of truth**.

### Validation Indicators Legend

Throughout this document, business rules include validation indicators showing which layer enforces each rule:

| Indicator | Meaning | Example |
|-----------|---------|---------|
| **✅ Backend Enforces** | Backend validates and enforces (source of truth) | Capacity limits, unique constraints |
| **⚠️ Client Validates (UX)** | Mobile checks for fast feedback (backend also enforces) | Session capacity, role checks |
| **⚠️ Client Hides UI** | Mobile hides features based on roles (backend authorizes) | Committee-only buttons |
| **⚠️ Client Filters** | Mobile filters options (backend validates selection) | Venue dropdowns by sport |
| **❌ Client Skips** | Mobile doesn't check (only backend can validate) | Unique email, race conditions |
| **(Display only)** | Informational field, no enforcement needed | Athlete level, venue capacity |

**Example Business Rule:**
- "Cannot exceed capacity" (**✅ Backend Enforces**, ⚠️ Client validates for UX)
  - Backend: Rejects API call if session is full (source of truth)
  - Mobile: Shows "Session Full" message before user attempts to confirm (fast UX feedback)

---

## Core Domain Entities (10 Total)

### 1. User

The fundamental identity in the system representing any person.

**Attributes:**
- `id`: Unique identifier (UUID)
- `name`: Full name
- `email`: Email address (unique)
- `status`: Account status (Active, Inactive, Suspended)

**Responsibilities:**
- Authentication and identity management
- Profile association
- Activity tracking

---

### 2. UserRole

Defines what a user can do in the system through role assignment with granular Committee privileges.

**Attributes:**
- `id`: UUID
- `userId`: UUID - Reference to User
- `role`: Role - ATHLETE | SUPPORTER | COMMITTEE
- `privileges`: List<CommitteePrivilege> - Only for COMMITTEE role
- `assignedBy`: UUID - Who assigned this role (audit trail)
- `assignedAt`: Timestamp - When assigned (audit trail)
- `createdAt`: Timestamp

**Roles:**
- **ATHLETE**: Can confirm attendance, has skill level
- **SUPPORTER**: Read-only access
- **COMMITTEE**: Can manage system (with specific privileges)

**Committee Privileges:**
- `MANAGE_SESSIONS`: Create, edit, cancel training sessions
- `MANAGE_CALENDAR`: Unlock/lock scheduling periods
- `MANAGE_ATHLETES`: Edit athlete levels, manage profiles
- `MANAGE_VENUES`: Add and edit venue information
- `VIEW_ANALYTICS`: Access reports and statistics

**Committee Admin:**
- Committee member with all 5 privileges = **COMMITTEE_ADMIN**
- Only ADMIN can assign/revoke privileges to other Committee members

**Business Rules:**
1. User must have at least one role (**✅ Backend Enforces**)
2. ATHLETE and SUPPORTER are mutually exclusive (**✅ Backend Enforces**, ⚠️ Client validates for UX)
3. COMMITTEE can be combined with ATHLETE or SUPPORTER (**✅ Backend Enforces**, ⚠️ Client validates for UX)
4. Only COMMITTEE_ADMIN can assign privileges (**✅ Backend Enforces**, ⚠️ Client hides UI)
5. System must have at least one COMMITTEE_ADMIN (**✅ Backend Enforces**)

**Valid Role Combinations:**
- ✅ Athlete only
- ✅ Supporter only
- ✅ Athlete + Committee
- ✅ Supporter + Committee
- ❌ Athlete + Supporter (mutually exclusive)

---

### 3. AthleteProfile

Stores athlete-specific information including skill level per sport for informational purposes.

**Attributes:**
- `id`: UUID
- `userId`: UUID - Reference to User (must have ATHLETE role)
- `sportId`: UUID - Reference to Sport
- `level`: AthleteLevel - BEGINNER | INTERMEDIATE | ADVANCED
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

**Responsibilities:**
- Track athlete skill progression per sport
- Provide context for session recommendations
- Enable level-based analytics
- Support multi-sport athletes with different skill levels

**Business Rules:**
1. User must have ATHLETE role to have AthleteProfile (**✅ Backend Enforces**, ⚠️ Client validates for UX)
2. Level is informational only - doesn't restrict session attendance (Display only)
3. Only Committee members with MANAGE_ATHLETES privilege can modify levels (**✅ Backend Enforces**, ⚠️ Client hides UI)
4. Level changes are tracked in AthleteProfileHistory (**✅ Backend Enforces**)
5. One profile per user per sport (unique: userId + sportId) (**✅ Backend Enforces**, ❌ Client skips - race conditions possible)
6. Athletes can have different levels in different sports (**✅ Backend Enforces**)

**Use Cases:**
- Help athletes find appropriate sessions (e.g., Intermediate volleyball athlete sees "Tuesday Advanced" session)
- Allow cross-level attendance (Advanced athlete can help in Beginner session)
- Track athlete progression over time per sport
- Support multi-sport athletes (e.g., João: ADVANCED in Volleyball, BEGINNER in Padel)

---

### 4. AthleteProfileHistory

Audit trail tracking changes to athlete skill levels over time.

**Attributes:**
- `id`: UUID
- `athleteProfileId`: UUID - Reference to AthleteProfile
- `oldLevel`: AthleteLevel - Previous level (null for first entry)
- `newLevel`: AthleteLevel - New level assigned
- `changedBy`: UUID - Committee member who made the change
- `changedAt`: Timestamp - When change occurred
- `reason`: String - Optional explanation for level change

**Responsibilities:**
- Maintain complete history of level changes
- Provide audit trail for Committee decisions
- Enable future analytics on athlete progression

**Business Rules:**
1. Automatically created whenever AthleteProfile.level is updated (**✅ Backend Enforces**)
2. Cannot be deleted (immutable audit trail) (**✅ Backend Enforces**)
3. Only Committee members with MANAGE_ATHLETES privilege can trigger changes (**✅ Backend Enforces**, ⚠️ Client hides UI)

**MVP Scope:**
- Data model implemented and capturing history
- UI for viewing history is a future feature

---

### 5. Sport

Represents a specific sport or activity category.

**Attributes:**
- `id`: UUID
- `name`: Sport name (e.g., "Volleyball", "Futsal", "Padel", "Running")
- `description`: Brief description of the sport
- `isActive`: Boolean - Whether the sport is currently active

**Responsibilities:**
- Categorization of training sessions
- Sport-specific configuration
- Link to sport-specific venues

**Examples:**
- Volleyball (Voleibol)
- Futsal
- Padel
- Basketball
- Running

**Business Rules:**
- Sport name must be unique (**✅ Backend Enforces**, ❌ Client skips - race conditions possible)
- Inactive sports don't show in athlete views but remain in system for historical data (**✅ Backend Enforces**, ⚠️ Client filters lists)

---

### 6. Venue

Pre-defined training locations with sport-based filtering for data consistency.

**Attributes:**
- `id`: UUID
- `name`: String - Venue name (e.g., "Pavilhão Municipal", "Campo de Padel")
- `address`: String - Full physical address
- `capacity`: Int - Informational capacity (not enforced)
- `sportTags`: List<UUID> - Which sports use this venue
- `isActive`: Boolean - Whether venue is currently available
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

**Responsibilities:**
- Provide consistent location data
- Filter venues by sport for easier session creation
- Store venue-specific information

**Business Rules:**
1. Only Committee members with MANAGE_VENUES privilege can create/edit venues (**✅ Backend Enforces**, ⚠️ Client hides UI)
2. Venue capacity is informational only - session capacity is enforced instead (Display only)
3. Sport tags enable filtering (e.g., only show Volleyball-tagged venues when creating Volleyball session) (**✅ Backend Enforces**, ⚠️ Client filters dropdowns)
4. Venue name must be unique (**✅ Backend Enforces**, ❌ Client skips - race conditions possible)

**Use Cases:**
- Committee creating Volleyball session sees dropdown of Volleyball-tagged venues
- Reduces inconsistencies like "Pavilhao Municipal" vs "Pavilhão Municipal" vs "Pav. Municipal"
- Provides centralized place for venue address updates

---

### 7. CalendarAccess

Controls when training sessions can be scheduled, with per-sport locking and audit trails.

**Attributes:**
- `id`: UUID
- `sportId`: UUID - Reference to Sport (per-sport locking)
- `startDate`: Date - Beginning of unlocked period
- `endDate`: Date - End of unlocked period
- `unlockedBy`: UUID - Committee member who unlocked this period
- `unlockedAt`: Timestamp - When period was unlocked
- `createdAt`: Timestamp

**Responsibilities:**
- Control when sessions can be scheduled
- Prevent far-future scheduling chaos
- Provide audit trail for calendar access

**Business Rules:**
1. Calendar is locked by default - no sessions can be created (**✅ Backend Enforces**)
2. Only Committee members with MANAGE_CALENDAR privilege can unlock periods (**✅ Backend Enforces**, ⚠️ Client hides UI)
3. Unlocking must be sequential (no gaps allowed) (**✅ Backend Enforces**, ⚠️ Client can pre-check)
4. Maximum unlock period: 3 months ahead of current date (**✅ Backend Enforces**, ⚠️ Client can pre-check)
5. Each sport has independent calendar access control (**✅ Backend Enforces**)
6. Cannot delete calendar access records (immutable audit trail) (**✅ Backend Enforces**)

**Example:**
```
Volleyball Calendar:
- Currently: Jan 1, 2024
- Unlocked: Jan 1 - Feb 28 (by João, Jan 1)
- Can unlock: Mar 1 - Mar 31 (sequential, within 3 months)
- Cannot unlock: May 1 - May 31 (gap in March-April)
```

**Use Cases:**
- Committee unlocks February calendar at end of January
- Different sports can have different scheduling horizons
- Audit trail shows who unlocked which periods when

---

### 8. SessionTemplate

Defines recurring weekly training session patterns for quick calendar generation.

**Attributes:**
- `id`: UUID
- `sportId`: UUID - Reference to Sport
- `name`: String - Template name (e.g., "Tuesday Advanced Volleyball")
- `dayOfWeek`: DayOfWeek - MONDAY through SUNDAY
- `startTime`: Time - Session start time
- `endTime`: Time - Session end time
- `venueId`: UUID - Reference to Venue
- `targetLevel`: SessionLevel - BEGINNER | INTERMEDIATE | ADVANCED | ALL_LEVELS
- `capacity`: Int - Maximum attendees
- `isActive`: Boolean - Whether template is currently used
- `createdBy`: UUID - Committee member who created template
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

**Responsibilities:**
- Store recurring session patterns
- Enable quick calendar generation from templates
- Maintain consistency for weekly schedules

**Business Rules:**
1. Only Committee members with MANAGE_SESSIONS privilege can create/edit templates (**✅ Backend Enforces**, ⚠️ Client hides UI)
2. Templates generate individual TrainingSession records (not live-linked) (**✅ Backend Enforces**)
3. Generated sessions can still be individually edited or cancelled (**✅ Backend Enforces**)
4. Multiple templates can exist for same day/sport (e.g., Tuesday has 2 Volleyball slots) (**✅ Backend Enforces**)

**MVP Importance:** This is CRITICAL for MVP - enables Committee to quickly populate weekly schedules.

**Example Templates (Volleyball):**
```
1. "Tuesday Advanced" - TUE 20:00-21:30, Pavilhão Municipal, ADVANCED
2. "Tuesday Intermediate" - TUE 21:30-23:00, Pavilhão Municipal, INTERMEDIATE
3. "Sunday All Levels" - SUN 17:00-18:30, Pavilhão Municipal, ALL_LEVELS
```

**Use Case:**
1. Committee unlocks February calendar
2. System offers "Generate from templates" for all active Volleyball templates
3. Creates individual TrainingSession records for each Tuesday, Sunday in February
4. Committee can then edit/cancel individual sessions as needed

---

### 9. TrainingSession

A specific scheduled training event for a sport with venue and target level.

**Attributes:**
- `id`: UUID
- `sportId`: UUID - Reference to Sport
- `venueId`: UUID - Reference to Venue
- `date`: Timestamp - Session date and time
- `targetLevel`: SessionLevel - BEGINNER | INTERMEDIATE | ADVANCED | ALL_LEVELS
- `capacity`: Int - Maximum number of attendees (enforced)
- `status`: SessionStatus - SCHEDULED | CANCELLED | COMPLETED
- `notes`: String - Optional notes for the session
- `createdBy`: UUID - Committee member who created it
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

**Responsibilities:**
- Session scheduling and management
- Capacity enforcement
- Session lifecycle management
- Target level guidance for athletes

**Business Rules:**
1. Only Committee members with MANAGE_SESSIONS privilege can create/update/delete sessions (**✅ Backend Enforces**, ⚠️ Client hides UI)
2. Can only create sessions within unlocked calendar periods (CalendarAccess) (**✅ Backend Enforces**, ⚠️ Client validates for UX)
3. Cannot confirm attendance for cancelled sessions (**✅ Backend Enforces**, ⚠️ Client validates for UX)
4. Cannot exceed capacity (enforced by Attendance logic) (**✅ Backend Enforces**, ⚠️ Client validates for UX)
5. Target level is informational - athletes of any level can attend (Client-side only)
6. Cannot create sessions in the past (**✅ Backend Enforces**, ⚠️ Client validates for UX)
7. Venue must support the session's sport (via sport tags) (**✅ Backend Enforces**, ⚠️ Client filters dropdowns)

**Example:**
```
Tuesday Volleyball - Advanced Session
- Date: Feb 6, 2024, 20:00-21:30
- Venue: Pavilhão Municipal
- Target Level: ADVANCED
- Capacity: 20
- Status: SCHEDULED
- Notes: "Bring knee pads"
- Created by: João Silva (Committee)
```

---

### 10. Attendance

The bridge entity managing the many-to-many relationship between Users (Athletes) and Training Sessions.

**Attributes:**
- `id`: UUID
- `userId`: UUID - Reference to User (must have ATHLETE role)
- `sessionId`: UUID - Reference to TrainingSession
- `status`: AttendanceStatus - PENDING | CONFIRMED | CANCELLED
- `confirmedAt`: Timestamp - When attendance was confirmed
- `cancelledAt`: Timestamp - When attendance was cancelled
- `notes`: String - Optional notes from the athlete

**Responsibilities:**
- Tracking who attends which session
- Managing confirmation and cancellation
- Capacity enforcement

**Business Rules:**
1. Only users with ATHLETE role can confirm attendance (**✅ Backend Enforces**, ⚠️ Client hides UI)
2. Cannot confirm if session is at capacity (unless replacing a cancelled attendance) (**✅ Backend Enforces**, ⚠️ Client validates for UX)
3. Cannot confirm for past sessions (**✅ Backend Enforces**, ⚠️ Client validates for UX)
4. Cannot confirm for cancelled sessions (**✅ Backend Enforces**, ⚠️ Client validates for UX)
5. Users with only SUPPORTER role are blocked from creating Attendance records (**✅ Backend Enforces**, ⚠️ Client hides UI)
6. One attendance record per user per session (unique constraint) (**✅ Backend Enforces**, ❌ Client skips - race conditions possible)
7. Cancelled attendances free up capacity immediately (**✅ Backend Enforces**)

**Example Flow:**
```
1. Maria (ATHLETE, INTERMEDIATE) views Tuesday Advanced session
2. System shows: "Target: Advanced, but all levels welcome"
3. Maria confirms attendance
4. Attendance record created with status CONFIRMED
5. Session capacity updated (19/20 slots remaining)
```

---

## Entity Relationships

### Cardinality

```
User (1) ---- (1..N) UserRole
   User must have at least one role

User (1) ---- (0..N) AthleteProfile
   User with ATHLETE role can have profiles for multiple sports

Sport (1) ---- (0..N) AthleteProfile
   Sport can have many athlete profiles at different levels

AthleteProfile (1) ---- (0..N) AthleteProfileHistory
   Profile can have many level change records

Sport (1) ---- (0..N) Venue
   Sport can be tagged to many Venues (via sportTags)

Sport (1) ---- (0..N) CalendarAccess
   Each Sport has independent calendar access records

Sport (1) ---- (0..N) SessionTemplate
   Sport can have many recurring templates

Sport (1) ---- (0..N) TrainingSession
   Sport can have many training sessions

SessionTemplate (1) ---- (0..N) TrainingSession
   Template can generate many sessions (via generation process)

Venue (1) ---- (0..N) TrainingSession
   Venue can host many sessions

Venue (N) ---- (N) Sport
   Many-to-many via sportTags list

TrainingSession (1) ---- (0..N) Attendance
   Session can have many attendance records

User (1) ---- (0..N) Attendance
   User can attend many sessions (via Attendance)
```

### Many-to-Many Relationships

**TrainingSession ↔ User** is managed through the **Attendance** entity:
- Many Athletes can attend many Training Sessions
- The Attendance entity adds metadata (status, timestamps, notes)

**Venue ↔ Sport** is managed through sport tags:
- Many Sports can use many Venues
- Venues have a `sportTags` list containing Sport IDs
- Enables sport-based filtering when creating sessions

---

## Business Rules

### Authorization Rules

#### 1. Supporter Restriction Rule
**Rule:** Supporters cannot confirm attendance for training sessions.

**Implementation:**
```kotlin
fun confirmAttendance(userId: UUID, sessionId: UUID) {
    val roles = getUserRoles(userId)

    if (!roles.contains(Role.ATHLETE)) {
        throw UnauthorizedException("Only athletes can confirm attendance")
    }

    // Continue with confirmation logic
}
```

#### 2. Committee Privilege Rules
**Rule:** Committee members need specific privileges for different management tasks.

**Implementation:**
```kotlin
fun createSession(userId: UUID, sessionData: SessionData) {
    requireCommitteePrivilege(userId, CommitteePrivilege.MANAGE_SESSIONS)
    // Continue with session creation
}

fun unlockCalendar(userId: UUID, sportId: UUID, period: DateRange) {
    requireCommitteePrivilege(userId, CommitteePrivilege.MANAGE_CALENDAR)
    // Continue with calendar unlocking
}

fun updateAthleteLevel(userId: UUID, athleteId: UUID, newLevel: AthleteLevel) {
    requireCommitteePrivilege(userId, CommitteePrivilege.MANAGE_ATHLETES)
    // Continue with level update
}

fun createVenue(userId: UUID, venueData: VenueData) {
    requireCommitteePrivilege(userId, CommitteePrivilege.MANAGE_VENUES)
    // Continue with venue creation
}

fun requireCommitteePrivilege(userId: UUID, privilege: CommitteePrivilege) {
    val roles = getUserRoles(userId)
    val committeeRole = roles.find { it.role == Role.COMMITTEE }
        ?: throw UnauthorizedException("User is not a Committee member")

    if (!committeeRole.privileges.contains(privilege)) {
        throw UnauthorizedException("Missing privilege: $privilege")
    }
}
```

#### 3. Committee Admin Rules
**Rule:** Only Committee members with all privileges (COMMITTEE_ADMIN) can assign/revoke privileges.

**Implementation:**
```kotlin
fun assignPrivilege(adminId: UUID, targetUserId: UUID, privilege: CommitteePrivilege) {
    val adminRole = getUserRoles(adminId).find { it.role == Role.COMMITTEE }
        ?: throw UnauthorizedException("Admin is not a Committee member")

    val allPrivileges = CommitteePrivilege.values().toSet()
    if (adminRole.privileges != allPrivileges) {
        throw UnauthorizedException("Only COMMITTEE_ADMIN can assign privileges")
    }

    // Continue with privilege assignment
}
```

### Capacity Rules

#### 4. Session Capacity Rule
**Rule:** Cannot confirm attendance if session is at full capacity.

**Implementation:**
```kotlin
fun confirmAttendance(userId: UUID, sessionId: UUID) {
    val session = getSession(sessionId)
    val currentAttendance = getConfirmedCount(sessionId)

    if (currentAttendance >= session.capacity) {
        throw CapacityExceededException("Session is at full capacity")
    }

    // Continue with confirmation
}
```

#### 5. Venue Capacity Rule
**Rule:** Venue capacity is informational only - session capacity is enforced instead.

**Rationale:** Same venue might host different sessions with different requirements.

### Temporal Rules

#### 6. Past Session Rule
**Rule:** Cannot confirm attendance for sessions that have already occurred.

#### 7. Cancelled Session Rule
**Rule:** Cannot confirm attendance for cancelled sessions.

### Calendar Access Rules

#### 8. Locked Calendar Rule
**Rule:** Cannot create sessions for dates outside unlocked calendar periods.

**Implementation:**
```kotlin
fun createSession(userId: UUID, sessionData: SessionData) {
    requireCommitteePrivilege(userId, CommitteePrivilege.MANAGE_SESSIONS)

    val calendarAccess = getCalendarAccess(sessionData.sportId, sessionData.date)
    if (calendarAccess == null) {
        throw UnauthorizedException("Calendar is locked for this date")
    }

    // Continue with session creation
}
```

#### 9. Sequential Unlock Rule
**Rule:** Calendar unlocking must be sequential with no gaps.

**Implementation:**
```kotlin
fun unlockCalendar(userId: UUID, sportId: UUID, startDate: Date, endDate: Date) {
    requireCommitteePrivilege(userId, CommitteePrivilege.MANAGE_CALENDAR)

    val latestUnlock = getLatestCalendarAccess(sportId)
    if (latestUnlock != null && startDate > latestUnlock.endDate.plusDays(1)) {
        throw BusinessRuleException("Cannot skip dates - must unlock sequentially")
    }

    // Continue with unlocking
}
```

#### 10. Maximum Unlock Period Rule
**Rule:** Cannot unlock calendar more than 3 months ahead.

**Implementation:**
```kotlin
fun unlockCalendar(userId: UUID, sportId: UUID, startDate: Date, endDate: Date) {
    val today = Date.now()
    val maxDate = today.plusMonths(3)

    if (endDate > maxDate) {
        throw BusinessRuleException("Cannot unlock beyond 3 months ahead")
    }

    // Continue with unlocking
}
```

### Role Assignment Rules

#### 11. Minimum Role Rule
**Rule:** User must have at least one role.

#### 12. Mutual Exclusivity Rule
**Rule:** User cannot be both ATHLETE and SUPPORTER simultaneously.

**Implementation:**
```kotlin
fun assignRole(userId: UUID, newRole: Role) {
    val existingRoles = getUserRoles(userId).map { it.role }

    if (newRole == Role.ATHLETE && existingRoles.contains(Role.SUPPORTER)) {
        throw BusinessRuleException("Cannot be both ATHLETE and SUPPORTER")
    }

    if (newRole == Role.SUPPORTER && existingRoles.contains(Role.ATHLETE)) {
        throw BusinessRuleException("Cannot be both ATHLETE and SUPPORTER")
    }

    // Continue with role assignment
}
```

#### 13. Athlete Profile Rule
**Rule:** Only users with ATHLETE role can have an AthleteProfile.

#### 14. System Admin Rule
**Rule:** System must always have at least one COMMITTEE_ADMIN.

---

## Use Cases

### UC-01: Athlete Confirms Attendance

**Actor:** Athlete

**Preconditions:**
- User has Athlete profile
- Session is not cancelled
- Session is not at capacity
- Session is in the future

**Flow:**
1. Athlete views upcoming training sessions
2. Athlete selects a session to attend
3. System validates user is an Athlete
4. System checks session capacity
5. System creates Attendance record with status "Confirmed"
6. System sends confirmation notification

**Postconditions:**
- Attendance record created
- Session capacity updated

---

### UC-02: Athlete Cancels Attendance

**Actor:** Athlete

**Preconditions:**
- User has confirmed attendance for the session

**Flow:**
1. Athlete views their confirmed sessions
2. Athlete cancels attendance
3. System updates Attendance status to "Cancelled"
4. System frees up capacity slot

**Postconditions:**
- Attendance status updated to Cancelled
- Session capacity slot freed

---

### UC-03: Committee Creates Training Session

**Actor:** Committee Member

**Preconditions:**
- User has Committee profile

**Flow:**
1. Committee member navigates to session management
2. Committee member fills session details (sport, date, location, capacity)
3. System validates user is Committee member
4. System creates TrainingSession
5. System notifies relevant athletes

**Postconditions:**
- New TrainingSession created
- Athletes notified of new session

---

### UC-04: Committee Views Attendance List

**Actor:** Committee Member

**Preconditions:**
- User has Committee profile
- Training session exists

**Flow:**
1. Committee member selects a training session
2. System displays list of confirmed athletes
3. Committee member can view attendance statistics

**Postconditions:**
- None (read-only operation)

---

### UC-05: Supporter Views Training Schedule

**Actor:** Supporter

**Preconditions:**
- User has Supporter profile

**Flow:**
1. Supporter views training schedule
2. System displays all upcoming sessions (read-only)
3. Supporter cannot confirm attendance

**Postconditions:**
- None (read-only operation)

---

## Domain Invariants

**Invariants** are rules that must always be true:

1. **User Role Requirement**: Every User must have at least one UserRole
2. **Role Mutual Exclusivity**: User cannot have both ATHLETE and SUPPORTER roles
3. **Athlete Profile Constraint**: Only users with ATHLETE role can have AthleteProfile
4. **Sport-Specific Profile**: One AthleteProfile per user per sport (unique: userId + sportId)
5. **Committee Admin Existence**: System must always have at least one COMMITTEE_ADMIN
6. **Athlete Attendance Only**: Only users with ATHLETE role can have Attendance records
7. **Valid Session Status**: TrainingSession status must be SCHEDULED, CANCELLED, or COMPLETED
8. **Capacity Constraint**: Confirmed attendance count ≤ session capacity
9. **Unique Email**: Each User email must be unique in the system
10. **Future Sessions Only**: Cannot create sessions in the past
11. **Valid Attendance Status**: Attendance status must be PENDING, CONFIRMED, or CANCELLED
12. **Calendar Sequential**: CalendarAccess periods must be sequential with no gaps
13. **Calendar Limit**: Cannot unlock calendar beyond 3 months ahead
14. **Venue Sport Tags**: TrainingSession venue must have session's sport in sportTags
15. **Unique Venue Names**: Each Venue name must be unique
16. **Unique Role Assignment**: User cannot have duplicate roles
17. **Session Within Unlock**: TrainingSession date must be within CalendarAccess period
18. **Level Change Audit**: Every AthleteProfile level change must create AthleteProfileHistory record

---

## Future Extensions

This MVD can be extended with additional domains:

### Planned Extensions

1. **Team/Squad Management**
   - Athletes belong to teams (e.g., Men's Team, Youth Team)
   - Sessions can be team-specific
   - Team statistics and performance tracking

2. **Payment Management**
   - Membership fees
   - Session fees
   - Payment history
   - Subscription management

3. **Notification System**
   - Email/SMS notifications
   - Push notifications
   - Notification preferences per user

4. **Venue Management**
   - Multiple locations/venues
   - Venue booking and availability
   - Facility information

5. **Tournament Management**
   - Competition scheduling
   - Match results
   - Standings and rankings

6. **Equipment/Inventory**
   - Sports equipment tracking
   - Borrowing system
   - Maintenance records

### Why Start Small?

This focused domain allows us to:
- ✅ Build a working MVP quickly
- ✅ Validate core functionality with real users
- ✅ Maintain clean architecture
- ✅ Add features incrementally without breaking existing code
- ✅ Iterate based on user feedback

---

## Implementation Notes

### Repository Pattern

Each entity will have a corresponding repository interface:

```kotlin
interface UserRepository {
    suspend fun findById(id: UUID): User?
    suspend fun findByEmail(email: String): User?
    suspend fun save(user: User): User
}

interface UserRoleRepository {
    suspend fun findByUserId(userId: UUID): List<UserRole>
    suspend fun findByRole(role: Role): List<UserRole>
    suspend fun save(userRole: UserRole): UserRole
    suspend fun delete(id: UUID)
}

interface AthleteProfileRepository {
    suspend fun findByUserId(userId: UUID): List<AthleteProfile>
    suspend fun findByUserIdAndSportId(userId: UUID, sportId: UUID): AthleteProfile?
    suspend fun findBySportId(sportId: UUID): List<AthleteProfile>
    suspend fun findByLevel(sportId: UUID, level: AthleteLevel): List<AthleteProfile>
    suspend fun save(profile: AthleteProfile): AthleteProfile
}

interface AthleteProfileHistoryRepository {
    suspend fun findByProfileId(profileId: UUID): List<AthleteProfileHistory>
    suspend fun save(history: AthleteProfileHistory): AthleteProfileHistory
}

interface SportRepository {
    suspend fun findById(id: UUID): Sport?
    suspend fun findActive(): List<Sport>
    suspend fun save(sport: Sport): Sport
}

interface VenueRepository {
    suspend fun findById(id: UUID): Venue?
    suspend fun findBySport(sportId: UUID): List<Venue>
    suspend fun findActive(): List<Venue>
    suspend fun save(venue: Venue): Venue
}

interface CalendarAccessRepository {
    suspend fun findBySport(sportId: UUID): List<CalendarAccess>
    suspend fun findLatest(sportId: UUID): CalendarAccess?
    suspend fun isDateUnlocked(sportId: UUID, date: Date): Boolean
    suspend fun save(calendarAccess: CalendarAccess): CalendarAccess
}

interface SessionTemplateRepository {
    suspend fun findById(id: UUID): SessionTemplate?
    suspend fun findBySport(sportId: UUID): List<SessionTemplate>
    suspend fun findActive(sportId: UUID): List<SessionTemplate>
    suspend fun save(template: SessionTemplate): SessionTemplate
}

interface TrainingSessionRepository {
    suspend fun findById(id: UUID): TrainingSession?
    suspend fun findUpcoming(sportId: UUID?): List<TrainingSession>
    suspend fun findByDateRange(sportId: UUID, startDate: Date, endDate: Date): List<TrainingSession>
    suspend fun save(session: TrainingSession): TrainingSession
}

interface AttendanceRepository {
    suspend fun findBySession(sessionId: UUID): List<Attendance>
    suspend fun findByUser(userId: UUID): List<Attendance>
    suspend fun findByUserAndSession(userId: UUID, sessionId: UUID): Attendance?
    suspend fun getConfirmedCount(sessionId: UUID): Int
    suspend fun save(attendance: Attendance): Attendance
}
```

### Use Case Pattern

Business logic will be encapsulated in Use Cases:

```kotlin
class ConfirmAttendanceUseCase(
    private val userRoleRepository: UserRoleRepository,
    private val athleteProfileRepository: AthleteProfileRepository,
    private val sessionRepository: TrainingSessionRepository,
    private val attendanceRepository: AttendanceRepository
) {
    suspend fun execute(userId: UUID, sessionId: UUID): Attendance {
        // 1. Validate user has ATHLETE role
        val roles = userRoleRepository.findByUserId(userId)
        if (!roles.any { it.role == Role.ATHLETE }) {
            throw UnauthorizedException("Only athletes can confirm attendance")
        }

        // 2. Validate session exists and is not cancelled
        val session = sessionRepository.findById(sessionId)
            ?: throw NotFoundException("Session not found")

        if (session.status == SessionStatus.CANCELLED) {
            throw BusinessRuleException("Cannot confirm for cancelled session")
        }

        // 3. Check capacity
        val confirmedCount = attendanceRepository.getConfirmedCount(sessionId)
        if (confirmedCount >= session.capacity) {
            throw CapacityExceededException("Session is at full capacity")
        }

        // 4. Create attendance record
        val attendance = Attendance(
            userId = userId,
            sessionId = sessionId,
            status = AttendanceStatus.CONFIRMED,
            confirmedAt = Timestamp.now()
        )

        // 5. Return result
        return attendanceRepository.save(attendance)
    }
}

class UnlockCalendarUseCase(
    private val userRoleRepository: UserRoleRepository,
    private val calendarAccessRepository: CalendarAccessRepository
) {
    suspend fun execute(userId: UUID, sportId: UUID, startDate: Date, endDate: Date): CalendarAccess {
        // 1. Validate user has MANAGE_CALENDAR privilege
        val roles = userRoleRepository.findByUserId(userId)
        val committeeRole = roles.find { it.role == Role.COMMITTEE }
            ?: throw UnauthorizedException("User is not a Committee member")

        if (!committeeRole.privileges.contains(CommitteePrivilege.MANAGE_CALENDAR)) {
            throw UnauthorizedException("Missing MANAGE_CALENDAR privilege")
        }

        // 2. Validate sequential unlocking
        val latest = calendarAccessRepository.findLatest(sportId)
        if (latest != null && startDate > latest.endDate.plusDays(1)) {
            throw BusinessRuleException("Calendar unlock must be sequential")
        }

        // 3. Validate 3-month limit
        val maxDate = Date.now().plusMonths(3)
        if (endDate > maxDate) {
            throw BusinessRuleException("Cannot unlock beyond 3 months ahead")
        }

        // 4. Create calendar access record
        val calendarAccess = CalendarAccess(
            sportId = sportId,
            startDate = startDate,
            endDate = endDate,
            unlockedBy = userId,
            unlockedAt = Timestamp.now()
        )

        return calendarAccessRepository.save(calendarAccess)
    }
}
```

---

## Diagrams

For visual representations of this domain design, see:
- [Class Diagram](./class-diagram.md) - Entity structure and relationships
- [ER Diagram](./er-diagram.md) - Database/entity relationships
- [Use Case Diagram](./use-cases.md) - Actor interactions

---

## Glossary

- **MVD**: Minimum Viable Domain - focused core domain model
- **UserRole**: Multi-role system determining user permissions (ATHLETE, SUPPORTER, COMMITTEE)
- **AthleteProfile**: Profile containing athlete-specific data like skill level
- **AthleteLevel**: Skill classification (BEGINNER, INTERMEDIATE, ADVANCED) - informational only
- **SessionLevel**: Target level for a training session (BEGINNER, INTERMEDIATE, ADVANCED, ALL_LEVELS)
- **Attendance**: Confirmation of user participation in a training session
- **Capacity**: Maximum number of athletes allowed in a session (enforced)
- **Venue Capacity**: Maximum capacity of a location (informational only, not enforced)
- **Committee**: Administrative role with granular privileges
- **CommitteePrivilege**: Specific permission (MANAGE_SESSIONS, MANAGE_CALENDAR, MANAGE_ATHLETES, MANAGE_VENUES, VIEW_ANALYTICS)
- **COMMITTEE_ADMIN**: Committee member with all 5 privileges - can assign privileges to others
- **Supporter**: Read-only role without attendance confirmation rights
- **Athlete**: Role that can confirm attendance for training sessions
- **Venue**: Pre-defined training location with sport tags
- **Sport Tags**: List of sports associated with a venue for filtering
- **CalendarAccess**: Unlocked date range for a sport where sessions can be scheduled
- **Sequential Unlocking**: Calendar unlock rule requiring no date gaps
- **SessionTemplate**: Recurring weekly pattern used to generate training sessions
- **Target Level**: Session's recommended skill level - doesn't restrict attendance
- **Audit Trail**: Tracking who made changes and when (assignedBy, changedBy, unlockedBy fields)