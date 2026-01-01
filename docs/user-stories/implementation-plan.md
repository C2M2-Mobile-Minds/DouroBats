# Implementation Plan

## Overview

This document provides a recommended order for implementing DouroBats user stories, including dependencies and suggested grouping.

**Note:** This is a suggestion. You can organize stories differently on your project board based on priorities and team capacity.

---

## Recommended Implementation Order

### Group 1: Foundation (Must Come First)

These stories provide the foundation for everything else.

| Story | Points | Why First | Dependencies |
|-------|--------|-----------|--------------|
| **DB001** Asset Preparation | 2 | Needed for splash screens | None |
| **DB015** Expand Domain Models for Sessions | 5 | Foundation for all features | None |
| **DB016** Extended Domain Models | 5 | Needed for repositories | DB015 |
| **DB017** Session Repository Interface | 3 | Contracts for data layer | DB015, DB016 |
| **DB018** Auth Repository Interface | 2 | Contracts for data layer | DB015, DB016 |

**Subtotal: 17 points**

---

### Group 2: Splash & Branding (Quick Wins)

Can be done in parallel with Group 1.

| Story | Points | Can Parallelize | Dependencies |
|-------|--------|-----------------|--------------|
| **DB002** Android Splash | 5 | Yes (with DB003) | DB001 |
| **DB003** iOS Splash | 5 | Yes (with DB002) | DB001 |
| **DB004** Splash Transition | 3 | No | DB002, DB003 |

**Subtotal: 13 points**

---

### Group 3: Navigation Updates (Independent)

Can be done in parallel with other groups.

| Story | Points | Can Parallelize | Dependencies |
|-------|--------|-----------------|--------------|
| **DB005** Sport-Agnostic Icons | 3 | Yes | None |
| **DB006** Rename Navigation Labels | 2 | Yes | DB005 |
| **DB007** Navigation Visual Polish | 3 | No | DB005, DB006 |

**Subtotal: 8 points**

---

### Group 4: Fake Repositories (Enables UI Development)

Build these so UI developers can work without backend.

| Story | Points | Can Parallelize | Dependencies |
|-------|--------|-----------------|--------------|
| **DB019** Fake Session Repository | 5 | Yes (with DB020) | DB017 |
| **DB020** Fake Auth Repository | 5 | Yes (with DB019) | DB018 |

**Subtotal: 10 points**

---

### Group 5: ViewModels (State Management)

Manage state for features.

| Story | Points | Can Parallelize | Dependencies |
|-------|--------|-----------------|--------------|
| **DB021** Sessions ViewModel | 5 | Yes (with DB022) | DB019 |
| **DB022** Auth ViewModel | 5 | Yes (with DB021) | DB020 |

**Subtotal: 10 points**

---

### Group 6: Authentication UI (Core Feature)

Complete auth flow before sessions.

| Story | Points | Order | Dependencies |
|-------|--------|-------|--------------|
| **DB023** Login Screen UI | 5 | 1st | DB022 |
| **DB024** Sign-Up Screen UI | 5 | 2nd | DB022 |
| **DB025** Forgot Password UI | 3 | 3rd | DB022 |
| **DB026** Error Handling for Auth | 3 | 4th | DB023, DB024 |
| **DB027** Logout Functionality | 2 | 5th | DB022 |
| **DB028** Auth-Based Navigation | 3 | 6th | DB022, DB023 |
| **DB029** Token Persistence | 5 | 7th | DB022 |

**Subtotal: 26 points**

---

### Group 7: Sessions & Booking UI (Core Feature)

Main user-facing feature.

| Story | Points | Order | Dependencies |
|-------|--------|-------|--------------|
| **DB008** Calendar Week View | 8 | 1st | DB021 |
| **DB009** Calendar Month View | 8 | 2nd | DB021 |
| **DB010** Session List Display | 5 | 3rd | DB008, DB021 |
| **DB011** Session Details Screen | 5 | 4th | DB010 |
| **DB012** Booking Action UI | 5 | 5th | DB011, DB021 |
| **DB013** Cancel Booking UI | 3 | 6th | DB012 |
| **DB014** Empty State | 2 | 7th | DB010 |

**Subtotal: 36 points**

---

### Group 8: Backend Integration (When APIs Available)

Replace fake repositories with real API calls.

**Important:** Only start these when corresponding backend endpoints are ready.

| Story | Points | Replaces | Backend Dependency |
|-------|--------|----------|-------------------|
| **DB036** HTTP Client Setup (Ktor) | 5 | - | Base URL, auth scheme |
| **DB037** Real Auth API | 5 | DB020 | `/api/auth/*` endpoints |
| **DB038** Real Session API | 5 | DB019 | `/api/sessions/*` endpoints |
| **DB039** Real Booking API | 5 | DB019 | `/api/bookings/*` endpoints |
| **DB040** User Profile API | 3 | - | `/api/users/*` endpoints |

**Subtotal: 23 points**

**Note:** Use feature flags or DI to switch between fake and real repositories during development.

---

### Group 9: Social Authentication (Optional Enhancement)

Can be added after core auth works.

| Story | Points | Order | Dependencies |
|-------|--------|-------|--------------|
| **DB030** Social Auth Infrastructure | 3 | 1st | External (Google/Apple setup) |
| **DB031** SocialAuthManager Interface | 2 | 2nd | None |
| **DB032** Android Google Sign-In | 8 | 3rd | DB030, DB031 |
| **DB033** iOS Apple Sign-In | 8 | 4th | DB030, DB031 |
| **DB034** Social Auth UI Integration | 5 | 5th | DB032, DB033 |
| **DB035** Social Auth Error Handling | 3 | 6th | DB034 |

**Subtotal: 29 points**

---

### Group 10: Advanced Session Management (Committee Features)

For power users managing the system.

| Story | Points | Feature | Dependencies |
|-------|--------|---------|--------------|
| **DB041** Create Session Templates | 5 | Templates | DB038 (Real Session API) |
| **DB042** Generate from Templates | 5 | Templates | DB041 |
| **DB043** Calendar Access Control | 5 | Lock/Unlock | DB038 |
| **DB044** Waiting List | 5 | Capacity mgmt | DB039 |

**Subtotal: 20 points**

---

### Group 11: User Roles & Permissions (Complex Feature)

Requires backend RBAC support.

| Story | Points | Feature | Dependencies |
|-------|--------|---------|--------------|
| **DB045** Committee RBAC Setup | 8 | Roles/privileges | Backend RBAC API |
| **DB046** Athlete Skill Levels | 5 | Skill management | Backend API |

**Subtotal: 13 points**

---

### Group 12: Polish & Additional Features

Can be added incrementally based on priority.

| Story | Points | Feature | Dependencies |
|-------|--------|---------|--------------|
| **DB047** Push Notifications | 5 | Notifications | Firebase setup |
| **DB048** Profile Picture Upload | 3 | Avatar | DB040 |
| **DB049** Analytics Dashboard | 8 | Charts | Backend analytics API |

**Subtotal: 16 points**

---

## Suggested Sprint Plan

### Sprint 1: Foundation (2-3 weeks)
- Group 1: Foundation (17 pts)
- Group 2: Splash & Branding (13 pts)
- **Total: 30 points**

**Deliverable:** App launches with branded splash, domain models in place

---

### Sprint 2: Navigation & Auth UI (2-3 weeks)
- Group 3: Navigation (8 pts)
- Group 4: Fake Repositories (10 pts)
- Group 5: ViewModels (10 pts)
- Group 6: Auth UI (26 pts)
- **Total: 54 points**

**Deliverable:** Users can sign up, login, logout (with fake auth)

---

### Sprint 3: Sessions & Booking (2-3 weeks)
- Group 7: Sessions & Booking UI (36 pts)
- **Total: 36 points**

**Deliverable:** Users can browse calendar and book sessions (with fake data)

---

### Sprint 4: Backend Integration (2-3 weeks)
- Group 8: Backend APIs (23 pts)
- **Total: 23 points**

**Deliverable:** App connected to real backend, all features use live data

**Note:** Timing depends on backend API availability

---

### Sprint 5+: Enhancements (Ongoing)
- Group 9: Social Auth (29 pts) - Optional
- Group 10: Advanced Session Mgmt (20 pts)
- Group 11: Roles & Permissions (13 pts)
- Group 12: Polish (16 pts)

**Total: 78 points for enhancements**

---

## Dependencies Map

### Critical Path (Longest Chain)

```
DB001 (Assets)
  ↓
DB002/DB003 (Splash Screens)
  ↓
DB015 (Domain Models)
  ↓
DB016 (Extended Domain Models)
  ↓
DB017 (Session Repository Interface)
  ↓
DB019 (Fake Session Repo)
  ↓
DB021 (Sessions ViewModel)
  ↓
DB008 (Calendar UI)
  ↓
DB010 (Session List)
  ↓
DB012 (Booking UI)
```

**Estimated Duration:** ~8-10 weeks for critical path

---

## Parallelization Strategies

### Two Developers

**Developer 1: Backend/Data Focus**
- Group 1: Foundation
- Group 4: Fake Repositories
- Group 5: ViewModels
- Group 8: Backend Integration

**Developer 2: UI/Frontend Focus**
- Group 2: Splash & Branding
- Group 3: Navigation
- Group 6: Auth UI
- Group 7: Sessions UI

**Timeline:** ~6-7 weeks for MVP with good coordination

---

### Three+ Developers

**Developer 1: Core Backend**
- Foundation, repositories, ViewModels

**Developer 2: Auth**
- Auth UI, social auth, token management

**Developer 3: Sessions**
- Calendar UI, booking flow, session management

**Developer 4: Polish** (Part-time or parallel)
- Dark mode, analytics

**Timeline:** ~5-6 weeks for MVP

---

## External Dependencies

### Third-Party Services

| Story | Service | Setup Required |
|-------|---------|----------------|
| DB030-DB035 | Google/Apple OAuth | Console/Portal setup, credentials |
| DB047 | Firebase FCM | Firebase project, credentials |
| DB049 | Analytics backend | Backend analytics API |

### Backend APIs

| Story | API Endpoints | Status |
|-------|---------------|--------|
| DB037 | `/api/auth/*` | 🔴 Waiting |
| DB038 | `/api/sessions/*` | 🔴 Waiting |
| DB039 | `/api/bookings/*` | 🔴 Waiting |
| DB040 | `/api/users/*` | 🔴 Waiting |
| DB045 | `/api/roles/*` | 🔴 Waiting |

**Recommendation:** Start with fake repositories (DB019, DB020) to unblock UI development while backend is being built.

---

## Risk Mitigation

### High-Risk Stories

1. **DB008/DB009 (Calendar UI)** - Custom UI component, complex
   - **Mitigation:** Consider using library or start with simple week view only

2. **DB032/DB033 (Social Auth)** - Platform-specific, external dependencies
   - **Mitigation:** Defer until after core auth works, use standard libraries

3. **DB036-DB040 (Backend Integration)** - Depends on backend availability
   - **Mitigation:** Use fake repositories, implement feature flags for easy swapping

4. **DB045 (RBAC)** - Complex business logic
   - **Mitigation:** Start simple (just check user role), iterate on permissions

---

## MVP Definition

**Minimum viable product should include:**

✅ **Groups 1-7** (Foundation, Splash, Navigation, Auth, Sessions)
- **Total: ~154 points**
- **Duration: ~8-10 weeks (1-2 devs)**

**Deliverable:**
- Branded app with splash screen
- User can sign up, login, logout
- User can browse sessions by date
- User can book and cancel sessions
- Works on Android and iOS

**What can wait:**
- Real backend integration (use fakes for MVP validation)
- Social authentication
- Advanced session management
- Roles and permissions
- Analytics

---

## Next Steps

1. **Review and prioritize** - Decide which stories are MVP vs future
2. **Set up project board** - Create columns: Backlog, Ready, In Progress, Review, Done
3. **Assign first sprint** - Start with Group 1 (Foundation)
4. **Establish sprint cadence** - 2-3 week sprints recommended
5. **Plan demos** - Show progress at end of each sprint

---

## Notes

- Use **feature flags** or **DI configuration** to switch between fake and real repositories
- Consider **user feedback** after MVP before building all enhancements
- **Backend integration** timing is flexible - fake data allows frontend to progress independently
- All UI stories must add strings to existing 5 localization files (en, pt-PT, pt-BR, es-ES, en-GB)
