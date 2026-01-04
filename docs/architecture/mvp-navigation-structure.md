# MVP Navigation Structure

## Overview

The DouroBats MVP uses a **3-tab bottom navigation** structure optimized for session scheduling and booking. This document outlines the navigation architecture, content distribution, and user flows.

## Design Principles

1. **Focused on Core Use Case**: Session discovery, booking, and management
2. **Personalized Experience**: Home tab adapts to user role (athlete vs committee)
3. **Contextual Actions**: Admin features appear where they're used (FAB in Calendar, tools in Profile)
4. **No Navigation Shifts**: All users see the same 3 tabs regardless of role
5. **Industry Standard**: Follows Material Design (3-5 tabs) and iOS HIG guidelines

---

## Bottom Navigation Structure

```
┌─────────────────────────────────────┐
│  🏠 Home  │  📅 Calendar  │  👤 Profile  │
└─────────────────────────────────────┘
```

**3 Fixed Tabs (All Users)**

1. **Home** - Personalized dashboard and engagement hub
2. **Calendar** - Browse sessions, book, and create (committee)
3. **Profile** - User settings and committee tools

---

## Tab 1: 🏠 Home

### Purpose
Landing page that provides personalized content and quick access to relevant features based on user role.

### Layout

```
┌─────────────────────────────────────┐
│  Your Next Session                  │
│  ┌─────────────────────────────┐   │
│  │ 🏐 Beach Volleyball          │   │
│  │ Tonight, 6:00 PM            │   │
│  │ Downtown Court              │   │
│  │ [View Details] →            │   │
│  └─────────────────────────────┘   │
│                                     │
│  My Upcoming Sessions (3) >         │
│  ├─ Basketball - Tomorrow 7PM       │
│  ├─ Volleyball - Thu 6PM            │
│  └─ Soccer - Sat 10AM              │
│                                     │
│  📢 Announcements                   │
│  └─ New venue: Sports Complex       │
│                                     │
│  ──────────────────────────         │
│  Committee Quick Actions            │ ← Only if privileged
│  ┌─────────────────────────────┐   │
│  │ 📅 Create Session            │   │
│  │ 📊 View Statistics           │   │
│  │ 🔓 Unlock Calendar           │   │
│  └─────────────────────────────┘   │
└─────────────────────────────────────┘
```

### Content Priority (Top to Bottom)

1. **Hero Widget: Your Next Session**
   - Shows the next session user is booked for
   - Prominent, tappable card
   - Empty state: "No upcoming sessions" with CTA to browse

2. **My Upcoming Sessions**
   - List of 2-3 next sessions
   - "See all (12) →" link to view full history in Profile
   - Shows: Sport, date/time, venue

3. **Announcements** (Optional for MVP)
   - Club-wide news and updates
   - Pinned announcements at top
   - Can be added post-MVP if not ready

4. **Committee Quick Actions** (Conditional)
   - Only visible to users with committee privileges
   - Quick access to frequent admin tasks
   - Shortcuts to features also available elsewhere

### User Flows

**Regular User:**
- Sees next session + upcoming sessions
- Taps session → Opens session details
- Taps "See all" → Navigates to Profile > Booking History

**Committee Member:**
- Sees everything above +
- Committee Quick Actions card
- Taps "Create Session" → Opens create session flow
- Taps "View Statistics" → Opens Analytics (in Profile > Committee Tools)

---

## Tab 2: 📅 Calendar

### Purpose
Browse all available sessions, view session details, and book sessions. Committee members can also create and manage sessions.

### Layout

```
┌─────────────────────────────────────┐
│  [< January 2026 >]    [🔓]         │ ← Unlock button (committee)
│                                     │
│  Week Selector: Jan 8-14            │
│  ┌─────────────────────────────┐   │
│  │ M  T  W  T  F  S  S         │   │
│  │ 8  9 10 11 12 13 14         │   │
│  │       ↑ selected             │   │
│  └─────────────────────────────┘   │
│                                     │
│  Wednesday, January 10              │
│  ┌─────────────────────────────┐   │
│  │ 🏐 Beach Volleyball          │   │
│  │ 6:00 PM - 7:30 PM           │   │
│  │ Downtown Court              │   │
│  │ 15/20 spots • Intermediate  │   │
│  │                             │   │
│  │              [Book Now] →    │   │
│  └─────────────────────────────┘   │
│                                     │
│  ┌─────────────────────────────┐   │
│  │ ⚽ Soccer Practice           │   │
│  │ 7:30 PM - 9:00 PM           │   │
│  │ North Field                 │   │
│  │ 18/25 spots • All Levels    │   │
│  │ ✓ Booked                    │   │ ← User already booked
│  │              [Cancel] →      │   │
│  └─────────────────────────────┘   │
│                                     │
│                         [+] FAB      │ ← Committee only
└─────────────────────────────────────┘
```

### Features

**Calendar View:**
- Week selector (horizontal scroll)
- Optional: Month view toggle (can be added later)
- Current day highlighted
- Dates with sessions have indicator dot

**Session List:**
- Shows all sessions for selected date
- Each session card displays:
  - Sport icon/name
  - Time
  - Venue
  - Capacity (filled/total)
  - Skill level
  - Booking status (if user booked)
  - Book/Cancel button

**Empty State:**
- "No sessions scheduled for this date"
- For committee: "Be the first to create one" with CTA

### Committee Features

**1. Create Session (FAB)**
- Floating Action Button at bottom-right
- Opens create session flow
- Only visible to users with "Manage Sessions" privilege

**2. Unlock Calendar (Header Icon)**
- Top-right icon (🔓)
- Quick access to calendar unlock feature
- Opens calendar access control screen
- Only visible to users with "Manage Calendar" privilege

**3. Edit/Cancel Session (Long-Press)**
- Long-press any session card
- Bottom sheet with actions:
  - Edit Session
  - Cancel Session
  - View Attendees
  - Session Statistics
- Only available to session creator or users with "Manage Sessions" privilege

### User Flows

**Browse and Book:**
1. User lands on Calendar tab
2. Selects date from week picker
3. Views available sessions
4. Taps "Book Now" on session
5. Confirmation shown (optimistic update)
6. Session appears in Home > My Upcoming Sessions

**Committee - Create Session:**
1. Committee member taps FAB (+)
2. Create Session form opens:
   - Select sport
   - Select date/time
   - Select venue
   - Set capacity
   - Set skill level (optional recommendation)
   - Add description
3. Taps "Create"
4. Session appears in calendar
5. Success message shown

---

## Tab 3: 👤 Profile

### Purpose
User profile management, app settings, booking history, and committee administrative tools.

### Layout

```
┌─────────────────────────────────────┐
│  Profile                            │
│  ┌─────────────────────────────┐   │
│  │      [Avatar Image]          │   │
│  │                              │   │
│  │      John Doe                │   │
│  │      john@example.com        │   │
│  │                              │   │
│  │      [Edit Profile] →        │   │
│  └─────────────────────────────┘   │
│                                     │
│  My Booking History >               │
│  └─ 24 sessions attended this year  │
│                                     │
│  Preferences                        │
│  ├─ 🔔 Notifications               │
│  ├─ 🌍 Language                    │
│  ├─ 🌙 Theme (Light/Dark)          │
│  └─ ⚙️  General Settings           │
│                                     │
│  ──────────────────────────         │
│  Committee Tools                    │ ← Only if privileged
│  ├─ 📅 Sessions I Created >        │
│  ├─ 🔓 Calendar Access Control >   │
│  ├─ 🏟️  Manage Venues >            │
│  ├─ 👥 Invite Members >            │
│  └─ 📊 Analytics Dashboard >       │
│                                     │
│  About DouroBats                    │
│  Privacy Policy                     │
│  Terms of Service                   │
│                                     │
│  [Logout]                           │
└─────────────────────────────────────┘
```

### Sections

#### **1. Profile Card**
- Avatar image (tap to change)
- Name and email
- Edit Profile button → Full profile edit screen
  - Update name
  - Change email
  - Update sports preferences
  - Change password

#### **2. My Booking History**
- Collapsible section
- Shows stats: "24 sessions attended this year"
- Tap to expand → Full history screen:
  - Upcoming bookings (with cancel option)
  - Past sessions
  - Filter by sport, date range
  - Search functionality

#### **3. Preferences**
- Notifications: Push notification settings
- Language: Current selection (from existing 5 languages)
- Theme: Light/Dark mode toggle
- General Settings: Other app preferences

#### **4. Committee Tools** (Conditional)
Only visible to users with at least one committee privilege.

**Sessions I Created:**
- List of sessions created by this user
- Quick edit/cancel actions
- Filter by upcoming/past
- Visibility: Users with "Manage Sessions" privilege

**Calendar Access Control:**
- Unlock date ranges for session creation
- View currently unlocked periods
- Enforce 3-month maximum ahead
- Enforce sequential unlocking
- Audit trail of who unlocked when
- Visibility: Users with "Manage Calendar" privilege

**Manage Venues:**
- List all venues
- Add new venue
- Edit venue details
- Visibility: Users with "Manage Venues" privilege

**Invite Members:**
- Send invitation emails
- Bulk invite
- View pending invitations
- Visibility: Users with "Manage Athletes" privilege

**Analytics Dashboard:**
- Attendance statistics
- Popular sessions
- Athlete participation metrics
- Filter by sport, date range, venue
- Export to CSV/PDF
- Visibility: Users with "View Analytics" privilege

#### **5. General Sections**
- About DouroBats: App info, version
- Privacy Policy
- Terms of Service
- Logout button

---

## Content Distribution Matrix

| Feature | Home | Calendar | Profile | Visibility |
|---------|------|----------|---------|------------|
| My next session | ✓ | | | Everyone |
| My upcoming sessions (2-3) | ✓ | | | Everyone |
| My booking history (full) | | | ✓ | Everyone |
| Browse all sessions | | ✓ | | Everyone |
| Book session | | ✓ | | Everyone |
| Cancel booking | ✓ (from widget) | ✓ | ✓ (from history) | Everyone |
| Announcements | ✓ | | | Everyone |
| Create session (FAB) | | ✓ | | Committee: Manage Sessions |
| Create session (Quick Action) | ✓ | | | Committee: Manage Sessions |
| Edit/Cancel session | | ✓ | | Committee: Manage Sessions |
| Sessions I created | | | ✓ | Committee: Manage Sessions |
| Unlock calendar (header) | | ✓ | | Committee: Manage Calendar |
| Unlock calendar (full) | | | ✓ | Committee: Manage Calendar |
| Calendar access control | | | ✓ | Committee: Manage Calendar |
| Manage venues | | | ✓ | Committee: Manage Venues |
| Invite members | | | ✓ | Committee: Manage Athletes |
| Analytics dashboard | ✓ (quick action) | | ✓ | Committee: View Analytics |
| Edit profile | | | ✓ | Everyone |
| App settings | | | ✓ | Everyone |

---

## Progressive Disclosure Pattern

The app uses progressive disclosure to hide complexity from users who don't need it:

### Regular User View
- **Visible:** Home (personalized), Calendar (browse/book), Profile (basic)
- **Hidden:** All committee features

### Committee Member View
- **Visible:** Everything regular users see +
  - Home: Committee Quick Actions card
  - Calendar: FAB (Create), Header icon (Unlock), Long-press menu (Edit/Cancel)
  - Profile: Committee Tools section
- **Hidden:** Nothing (has full access)

### Implementation
```kotlin
// Example: Conditional UI rendering
if (user.hasPrivilege(Privilege.MANAGE_SESSIONS)) {
    FloatingActionButton(
        onClick = { navController.navigate("create-session") }
    ) {
        Icon(Icons.Default.Add, contentDescription = "Create Session")
    }
}

// Example: Committee Tools section
if (user.hasAnyCommitteePrivilege()) {
    CommitteeToolsSection(
        privileges = user.privileges,
        onNavigateToTool = { /* ... */ }
    )
}
```

---

## Empty States

### Home Tab
**No Upcoming Sessions:**
```
┌─────────────────────────────┐
│   📅 No sessions booked yet  │
│                             │
│   Browse the calendar to    │
│   find your next session    │
│                             │
│   [Browse Sessions]         │
└─────────────────────────────┘
```

### Calendar Tab
**No Sessions for Date:**
```
┌─────────────────────────────┐
│   No sessions scheduled     │
│   for this date             │
│                             │
│   [Committee only:]         │
│   Be the first to create one│
│   [Create Session]          │
└─────────────────────────────┘
```

### Profile - Booking History
**No Booking History:**
```
┌─────────────────────────────┐
│   You haven't attended any  │
│   sessions yet              │
│                             │
│   [Find Sessions]           │
└─────────────────────────────┘
```

---

## Navigation Best Practices

### Tab Selection
- Default landing tab: **Home**
- Tab state persists during session (user returns to last viewed tab)
- Deep links can override default (e.g., notification → specific session in Calendar)

### Tab Switching
- Smooth animations (Material/iOS standard transitions)
- No data loss on tab switch (state preserved)
- Active tab clearly indicated (color, icon style)

### Back Button Behavior
- Android back button: Exits app if on Home tab, returns to Home from other tabs
- iOS swipe back: Standard system behavior within screens

### Deep Linking
```
Routes:
- /home → Home tab
- /calendar → Calendar tab
- /calendar/{date} → Calendar tab, specific date selected
- /session/{id} → Session details (can be accessed from any tab)
- /profile → Profile tab
- /profile/history → Profile tab, booking history expanded
- /profile/committee/analytics → Profile tab, Analytics screen
```

---

## Accessibility

### Navigation Labels
- Home: "Home tab"
- Calendar: "Calendar tab, browse and book sessions"
- Profile: "Profile tab, settings and account"

### Tab Bar
- Minimum touch target: 48x48dp (Material) / 44x44pt (iOS)
- Clear labels and icons
- Sufficient color contrast for active/inactive states
- Screen reader support for all interactive elements

### Keyboard Navigation
- Tab key moves between bottom nav items
- Enter/Space activates selected tab
- Arrow keys for horizontal navigation between tabs

---

## Future Enhancements

### Post-MVP Tab Additions
When additional features are ready, the navigation can expand to 4-5 tabs:

**Option 1: Add Resources Tab**
```
│ Home │ Calendar │ Resources │ Profile │
```
Resources would include:
- Training materials
- Technique videos
- Documents

**Option 2: Add Members Tab**
```
│ Home │ Calendar │ Members │ Profile │
```
Members would include:
- Member directory
- Social features
- Leaderboards

**Maximum Structure (5 tabs):**
```
│ Home │ Calendar │ Resources │ Members │ Profile │
```

### Progressive Feature Addition
1. **MVP:** 3 tabs (Home, Calendar, Profile)
2. **v1.1:** Add Announcements to Home
3. **v1.2:** Add Resources tab (if content ready)
4. **v1.3:** Add Members tab (if social features ready)
5. **v2.0:** Full 5-tab navigation

---

## Technical Implementation Notes

### State Management
- Use ViewModel for each tab to manage state
- Preserve state during tab switches
- Handle loading/error/success states consistently

### Navigation Component
- Use Jetpack Compose Navigation (Android)
- Use NavigationStack (iOS - if applicable)
- Single source of truth for navigation state

### Role-Based Rendering
- Check user privileges at component level
- Don't fetch privileged data for non-privileged users
- Fail gracefully if user loses privileges during session

### Performance
- Lazy load tab content (don't render all tabs upfront)
- Cache recently viewed tab data
- Prefetch next likely tab (e.g., if on Home, prefetch Calendar)

---

## Design Tokens

### Tab Bar Styling

**Heights:**
- Android: 56dp
- iOS: 49pt (83pt with safe area insets)

**Colors:**
- Active tab: Primary color (defined in AppTheme)
- Inactive tab: onSurface at 60% opacity
- Background: Surface color

**Typography:**
- Tab labels: Caption style (12sp/10pt)
- Icon size: 24dp/24pt

**Spacing:**
- Icon-to-label spacing: 4dp
- Horizontal padding: 12dp per tab
- Vertical padding: 8dp top/bottom

---

## Summary

The 3-tab MVP navigation provides:
- ✅ Clear, focused user experience
- ✅ Personalized content via Home tab
- ✅ Efficient session discovery via Calendar tab
- ✅ Organized settings and admin tools via Profile tab
- ✅ No UI shifts based on user role (progressive disclosure)
- ✅ Scalable architecture for future features
- ✅ Industry-standard UX patterns

This structure supports the MVP goal of session scheduling while laying groundwork for future enhancements.
