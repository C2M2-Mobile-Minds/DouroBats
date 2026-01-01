# Quick Reference Guide

## All User Stories by Epic

### Epic 1: Branding & Splash Screen (15 pts)
- **DB001:** Asset Preparation (2 pts)
- **DB002:** Android Splash Screen (5 pts)
- **DB003:** iOS Splash Screen (5 pts)
- **DB004:** Splash Transition (3 pts)

### Epic 2: Navigation Refactoring (8 pts)
- **DB005:** Sport-Agnostic Icons (3 pts)
- **DB006:** Rename Navigation Labels (2 pts)
- **DB007:** Navigation Visual Polish (3 pts)

### Epic 3: Sessions & Booking - UI (36 pts)
- **DB008:** Calendar Week View (8 pts)
- **DB009:** Calendar Month View (8 pts)
- **DB010:** Session List Display (5 pts)
- **DB011:** Session Details Screen (5 pts)
- **DB012:** Booking Action UI (5 pts)
- **DB013:** Cancel Booking UI (3 pts)
- **DB014:** Empty State (2 pts)

### Epic 4: Domain Layer & Architecture (15 pts)
- **DB015:** Expand Domain Models for Sessions (5 pts)
- **DB016:** Extended Domain Models (5 pts)
- **DB017:** Session Repository Interface (3 pts)
- **DB018:** Auth Repository Interface (2 pts)

### Epic 5: Data Layer - Fake Repos (10 pts)
- **DB019:** Fake Session Repository (5 pts)
- **DB020:** Fake Auth Repository (5 pts)

### Epic 6: State Management - ViewModels (10 pts)
- **DB021:** Sessions ViewModel (5 pts)
- **DB022:** Auth ViewModel (5 pts)

### Epic 7: Authentication - UI (18 pts)
- **DB023:** Login Screen UI (5 pts)
- **DB024:** Sign-Up Screen UI (5 pts)
- **DB025:** Forgot Password UI (3 pts)
- **DB026:** Error Handling for Auth (3 pts)
- **DB027:** Logout Functionality (2 pts)

### Epic 8: Auth Navigation & Storage (8 pts)
- **DB028:** Auth-Based Navigation (3 pts)
- **DB029:** Token Persistence (5 pts)

### Epic 9: Social Authentication (29 pts)
- **DB030:** Social Auth Infrastructure (3 pts)
- **DB031:** SocialAuthManager Interface (2 pts)
- **DB032:** Android Google Sign-In (8 pts)
- **DB033:** iOS Apple Sign-In (8 pts)
- **DB034:** Social Auth UI (5 pts)
- **DB035:** Social Auth Errors (3 pts)

### Epic 10: Backend API Integration (23 pts)
- **DB036:** HTTP Client Setup (5 pts)
- **DB037:** Real Auth API (5 pts)
- **DB038:** Real Session API (5 pts)
- **DB039:** Real Booking API (5 pts)
- **DB040:** User Profile API (3 pts)

### Epic 11: Advanced Session Management (20 pts)
- **DB041:** Session Templates - Create (5 pts)
- **DB042:** Session Templates - Generate (5 pts)
- **DB043:** Calendar Access Control (5 pts)
- **DB044:** Waiting List (5 pts)

### Epic 12: User Roles & Permissions (13 pts)
- **DB045:** Committee RBAC Setup (8 pts)
- **DB046:** Athlete Skill Levels (5 pts)

### Epic 13: Polish & Additional (16 pts)
- **DB047:** Push Notifications (5 pts)
- **DB048:** Profile Picture Upload (3 pts)
- **DB049:** Analytics Dashboard (8 pts)

---

## Total Points by Epic

| Epic | Points |
|------|--------|
| Epic 1: Branding & Splash Screen | 15 |
| Epic 2: Navigation Refactoring | 8 |
| Epic 3: Sessions & Booking - UI | 36 |
| Epic 4: Domain Layer & Architecture | 15 |
| Epic 5: Data Layer - Fake Repos | 10 |
| Epic 6: State Management - ViewModels | 10 |
| Epic 7: Authentication - UI | 18 |
| Epic 8: Auth Navigation & Storage | 8 |
| Epic 9: Social Authentication | 29 |
| Epic 10: Backend API Integration | 23 |
| Epic 11: Advanced Session Management | 20 |
| Epic 12: User Roles & Permissions | 13 |
| Epic 13: Polish & Additional | 16 |
| **TOTAL** | **221** |

---

## Total Points by Category

| Category | Points |
|----------|--------|
| **Core Foundation** (Epics 1, 4, 5, 6) | 50 |
| **UI Features** (Epics 2, 3, 7, 8) | 70 |
| **Social Auth** (Epic 9) | 29 |
| **Backend Integration** (Epic 10) | 23 |
| **Advanced Features** (Epics 11, 12, 13) | 49 |
| **TOTAL** | **221** |

---

## Story Points Scale

- **2 pts:** Very small (few hours - half day)
- **3 pts:** Small (half day - 1 day)
- **5 pts:** Standard (1-2 days)
- **8 pts:** Complex (2-4 days)
- **13 pts:** Very complex (should be broken down)

---

## Quick Filters

### By Dependency Type

**No Dependencies (Can Start Anytime):**
- DB001 (Asset Preparation)
- DB005 (Sport-Agnostic Icons)
- DB015 (Expand Domain Models)

**Needs Backend APIs:**
- DB037-DB040 (Auth, Session, Booking, Profile APIs)
- DB041-DB044 (Advanced session management)
- DB045-DB046 (RBAC and skill levels)
- DB049 (Analytics)

**Needs External Services:**
- DB030 (Google/Apple OAuth setup)
- DB047 (Firebase for push notifications)

**Uses Fake Data (Development):**
- DB019, DB020 (Fake repositories - replaced by real APIs later)

---

## Stories Removed (Already Implemented)

The following stories were removed as features already exist in the codebase:
- ❌ **Localization setup** (DB008-DB011) - 5 languages already configured
- ❌ **Koin DI Setup** (DB024) - Already implemented
- ❌ **Dark Mode** (DB053) - AppTheme handles it
- ❌ **Date/Time Utilities** (DB023) - kotlinx-datetime in use

**Note:** All UI stories must add strings to the existing 5 localization files (en, pt-PT, pt-BR, es-ES, en-GB).

---

## Files in This Directory

| File | Purpose |
|------|---------|
| [README.md](./README.md) | Index and overview |
| [all-user-stories.md](./all-user-stories.md) | Complete catalog (DB001-DB049) |
| [implementation-plan.md](./implementation-plan.md) | Recommended order & dependencies |
| [quick-reference.md](./quick-reference.md) | This quick lookup guide |

---

## Next Steps

1. Review all stories in [all-user-stories.md](./all-user-stories.md)
2. Organize on project board (MVP, Ready, Blocked, Future)
3. Start with foundation stories (Group 1 in implementation plan)
4. Use fake repositories to unblock UI development
5. Integrate backend APIs as they become available
