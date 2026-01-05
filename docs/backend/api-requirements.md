# Backend API Requirements

## Overview

This document specifies all API endpoints required by the DouroBats mobile application. The backend should implement a RESTful API with JWT-based authentication.

**Base URL:** `https://api.dourobats.pt` (production)
**Staging URL:** `https://staging-api.dourobats.pt`
**Development URL:** `http://localhost:8080`

**Authentication:** JWT Bearer tokens
**Content-Type:** `application/json`
**Date/Time Format:** ISO 8601 (UTC)

---

## Table of Contents

1. [Authentication & Authorization](#1-authentication--authorization)
2. [User Management](#2-user-management)
3. [Sessions (Training Sessions)](#3-sessions-training-sessions)
4. [Bookings](#4-bookings)
5. [Sports](#5-sports)
6. [Venues](#6-venues)
7. [Committee & Admin](#7-committee--admin)
8. [Analytics](#8-analytics)
9. [Error Handling](#9-error-handling)
10. [Data Models](#10-data-models)

---

## 1. Authentication & Authorization

### POST /api/auth/signup
Register a new user account.

**Request:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123!",
  "sports": ["volleyball", "basketball"],
  "language": "en"
}
```

**Response (201 Created):**
```json
{
  "user": {
    "id": "user_abc123",
    "name": "John Doe",
    "email": "john@example.com",
    "sports": ["volleyball", "basketball"],
    "roles": ["athlete"],
    "privileges": [],
    "skillLevels": {},
    "createdAt": "2026-01-04T10:00:00Z"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "refresh_token_xyz..."
}
```

**Validation:**
- Email must be unique and valid format
- Password minimum 8 characters, at least one uppercase, one lowercase, one number
- Name required, 2-100 characters
- Sports must be from valid sports list

**Errors:**
- 400: Invalid email format, weak password, invalid sports
- 409: Email already registered

---

### POST /api/auth/login
Authenticate existing user.

**Request:**
```json
{
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```

**Response (200 OK):**
```json
{
  "user": {
    "id": "user_abc123",
    "name": "John Doe",
    "email": "john@example.com",
    "sports": ["volleyball", "basketball"],
    "roles": ["athlete", "committee"],
    "privileges": ["MANAGE_SESSIONS", "VIEW_ANALYTICS"],
    "skillLevels": {
      "volleyball": "INTERMEDIATE",
      "basketball": "BEGINNER"
    },
    "avatarUrl": "https://cdn.dourobats.pt/avatars/user_abc123.jpg",
    "createdAt": "2026-01-04T10:00:00Z"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "refresh_token_xyz..."
}
```

**Errors:**
- 401: Invalid credentials
- 404: User not found

---

### POST /api/auth/refresh
Refresh access token using refresh token.

**Request:**
```json
{
  "refreshToken": "refresh_token_xyz..."
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "new_refresh_token_abc..."
}
```

**Errors:**
- 401: Invalid or expired refresh token

---

### POST /api/auth/logout
Invalidate current session tokens.

**Headers:** `Authorization: Bearer {token}`

**Response (204 No Content)**

**Notes:**
- Backend should blacklist the token until expiry
- Client should clear tokens from secure storage

---

### POST /api/auth/forgot-password
Request password reset email.

**Request:**
```json
{
  "email": "john@example.com"
}
```

**Response (200 OK):**
```json
{
  "message": "If this email exists, a password reset link has been sent"
}
```

**Notes:**
- Always return 200 even if email doesn't exist (security)
- Send email with reset token valid for 1 hour

---

### POST /api/auth/reset-password
Reset password using token from email.

**Request:**
```json
{
  "token": "reset_token_from_email",
  "newPassword": "NewSecurePassword123!"
}
```

**Response (200 OK):**
```json
{
  "message": "Password reset successful"
}
```

**Errors:**
- 400: Invalid or expired token
- 400: Weak password

---

### POST /api/auth/google
Authenticate via Google Sign-In.

**Request:**
```json
{
  "idToken": "google_id_token_from_client",
  "provider": "GOOGLE"
}
```

**Response (200 OK or 201 Created):**
```json
{
  "user": { /* same as login response */ },
  "token": "jwt_token",
  "refreshToken": "refresh_token",
  "isNewUser": false
}
```

**Notes:**
- Verify Google ID token with Google's API
- Create new user if first time (isNewUser: true)
- Associate Google ID with user account

**Errors:**
- 400: Invalid ID token
- 401: Google verification failed

---

### POST /api/auth/apple
Authenticate via Apple Sign-In.

**Request:**
```json
{
  "idToken": "apple_identity_token",
  "authorizationCode": "apple_auth_code",
  "provider": "APPLE",
  "user": {
    "email": "user@privaterelay.appleid.com",
    "firstName": "John",
    "lastName": "Doe"
  }
}
```

**Response (200 OK or 201 Created):**
```json
{
  "user": { /* same as login response */ },
  "token": "jwt_token",
  "refreshToken": "refresh_token",
  "isNewUser": true
}
```

**Notes:**
- Verify Apple identity token
- User info only provided on first sign-in
- Store Apple user identifier for future logins

---

## 2. User Management

### GET /api/users/me
Get current user profile.

**Headers:** `Authorization: Bearer {token}`

**Response (200 OK):**
```json
{
  "id": "user_abc123",
  "name": "John Doe",
  "email": "john@example.com",
  "sports": ["volleyball", "basketball"],
  "roles": ["athlete", "committee"],
  "privileges": ["MANAGE_SESSIONS", "VIEW_ANALYTICS"],
  "skillLevels": {
    "volleyball": "INTERMEDIATE",
    "basketball": "BEGINNER"
  },
  "avatarUrl": "https://cdn.dourobats.pt/avatars/user_abc123.jpg",
  "language": "en",
  "theme": "DARK",
  "notificationPreferences": {
    "sessionReminders": true,
    "sessionCancellations": true,
    "waitlistOpenings": true
  },
  "createdAt": "2026-01-04T10:00:00Z",
  "updatedAt": "2026-01-04T15:30:00Z"
}
```

---

### PUT /api/users/me
Update current user profile.

**Headers:** `Authorization: Bearer {token}`

**Request:**
```json
{
  "name": "John Smith",
  "sports": ["volleyball", "basketball", "soccer"],
  "language": "pt-PT",
  "theme": "LIGHT",
  "notificationPreferences": {
    "sessionReminders": false,
    "sessionCancellations": true,
    "waitlistOpenings": true
  }
}
```

**Response (200 OK):**
```json
{
  "id": "user_abc123",
  "name": "John Smith",
  /* ... updated fields ... */
  "updatedAt": "2026-01-04T16:00:00Z"
}
```

**Validation:**
- Name: 2-100 characters
- Sports: Must be from valid sports list
- Language: One of ["en", "pt-PT", "pt-BR", "es-ES", "en-GB"]
- Theme: One of ["LIGHT", "DARK", "SYSTEM"]

---

### POST /api/users/me/avatar
Upload profile picture.

**Headers:**
- `Authorization: Bearer {token}`
- `Content-Type: multipart/form-data`

**Request:**
```
Form field: "avatar" (image file)
Accepted formats: JPEG, PNG
Max size: 5MB
```

**Response (200 OK):**
```json
{
  "avatarUrl": "https://cdn.dourobats.pt/avatars/user_abc123.jpg?v=2",
  "updatedAt": "2026-01-04T16:15:00Z"
}
```

**Notes:**
- Backend should resize/crop to 512x512
- Store on CDN
- Return CDN URL with cache-busting parameter

**Errors:**
- 400: Invalid file format, file too large
- 413: Payload too large

---

### PUT /api/users/me/password
Change password.

**Headers:** `Authorization: Bearer {token}`

**Request:**
```json
{
  "currentPassword": "OldPassword123!",
  "newPassword": "NewSecurePassword456!"
}
```

**Response (200 OK):**
```json
{
  "message": "Password updated successfully"
}
```

**Errors:**
- 400: Weak new password
- 401: Current password incorrect

---

### GET /api/users/{userId}
Get public user profile (for viewing other members).

**Headers:** `Authorization: Bearer {token}`

**Response (200 OK):**
```json
{
  "id": "user_xyz789",
  "name": "Jane Doe",
  "sports": ["volleyball"],
  "skillLevels": {
    "volleyball": "ADVANCED"
  },
  "avatarUrl": "https://cdn.dourobats.pt/avatars/user_xyz789.jpg",
  "createdAt": "2025-12-01T10:00:00Z"
}
```

**Notes:**
- Only returns public fields (no email, no privileges)
- Used for viewing other members in session attendee lists

---

## 3. Sessions (Training Sessions)

### GET /api/sessions
Get list of sessions with filters.

**Headers:** `Authorization: Bearer {token}`

**Query Parameters:**
- `date` (optional): ISO date (e.g., "2026-01-10") - filter by specific date
- `startDate` (optional): ISO date - filter from this date
- `endDate` (optional): ISO date - filter until this date
- `sportId` (optional): Filter by sport
- `venueId` (optional): Filter by venue
- `status` (optional): One of ["SCHEDULED", "CANCELLED", "COMPLETED"]
- `page` (optional): Page number (default: 1)
- `limit` (optional): Items per page (default: 50, max: 100)

**Response (200 OK):**
```json
{
  "sessions": [
    {
      "id": "session_001",
      "sportId": "volleyball",
      "sportName": "Beach Volleyball",
      "sportIcon": "🏐",
      "dateTime": "2026-01-10T18:00:00Z",
      "duration": 90,
      "venueId": "venue_001",
      "venueName": "Downtown Court",
      "venueAddress": "123 Main St, Porto",
      "targetLevel": "INTERMEDIATE",
      "capacity": 20,
      "currentAttendees": 15,
      "status": "SCHEDULED",
      "description": "Regular Friday evening beach volleyball",
      "createdBy": "user_abc123",
      "createdByName": "John Doe",
      "isUserBooked": true,
      "createdAt": "2026-01-01T10:00:00Z",
      "updatedAt": "2026-01-04T12:00:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 50,
    "total": 150,
    "totalPages": 3
  }
}
```

**Notes:**
- Returns sessions user can view (all SCHEDULED sessions + user's own bookings)
- `isUserBooked` indicates if authenticated user has booked this session
- Sort by dateTime ascending by default

---

### GET /api/sessions/upcoming
Get upcoming sessions for user (shortcuts for common query).

**Headers:** `Authorization: Bearer {token}`

**Query Parameters:**
- `sportId` (optional): Filter by sport
- `limit` (optional): Max sessions to return (default: 20)

**Response (200 OK):**
```json
{
  "sessions": [
    /* same session object structure as GET /api/sessions */
  ]
}
```

**Notes:**
- Returns only SCHEDULED sessions
- Filters out past sessions (dateTime >= now)
- Sorted by dateTime ascending

---

### GET /api/sessions/{sessionId}
Get single session details.

**Headers:** `Authorization: Bearer {token}`

**Response (200 OK):**
```json
{
  "id": "session_001",
  "sportId": "volleyball",
  "sportName": "Beach Volleyball",
  "sportIcon": "🏐",
  "dateTime": "2026-01-10T18:00:00Z",
  "duration": 90,
  "venueId": "venue_001",
  "venueName": "Downtown Court",
  "venueAddress": "123 Main St, Porto",
  "targetLevel": "INTERMEDIATE",
  "capacity": 20,
  "currentAttendees": 15,
  "status": "SCHEDULED",
  "description": "Regular Friday evening beach volleyball",
  "createdBy": "user_abc123",
  "createdByName": "John Doe",
  "isUserBooked": true,
  "attendees": [
    {
      "userId": "user_abc123",
      "userName": "John Doe",
      "userAvatar": "https://cdn.dourobats.pt/avatars/user_abc123.jpg",
      "confirmedAt": "2026-01-04T12:00:00Z"
    }
  ],
  "createdAt": "2026-01-01T10:00:00Z",
  "updatedAt": "2026-01-04T12:00:00Z"
}
```

**Notes:**
- Includes full attendee list
- Used for session details screen

**Errors:**
- 404: Session not found

---

### POST /api/sessions
Create new session (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `MANAGE_SESSIONS` privilege

**Request:**
```json
{
  "sportId": "volleyball",
  "dateTime": "2026-01-15T18:00:00Z",
  "duration": 90,
  "venueId": "venue_001",
  "targetLevel": "INTERMEDIATE",
  "capacity": 20,
  "description": "Friday evening beach volleyball"
}
```

**Response (201 Created):**
```json
{
  "id": "session_002",
  "sportId": "volleyball",
  /* ... full session object ... */
  "createdAt": "2026-01-04T16:00:00Z"
}
```

**Validation:**
- dateTime must be in the future
- dateTime must be within unlocked calendar period (business logic)
- duration: 30-240 minutes
- capacity: 1-100
- sportId must exist
- venueId must exist
- targetLevel: One of ["BEGINNER", "INTERMEDIATE", "ADVANCED", null]

**Business Logic:**
- Check if dateTime falls within unlocked calendar period for this sport
- If not unlocked, return 403 with appropriate error message

**Errors:**
- 400: Validation errors
- 403: User lacks MANAGE_SESSIONS privilege
- 403: Calendar period not unlocked for this date/sport
- 404: Sport or venue not found

---

### PUT /api/sessions/{sessionId}
Update existing session (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `MANAGE_SESSIONS` privilege OR user is session creator

**Request:**
```json
{
  "dateTime": "2026-01-15T19:00:00Z",
  "duration": 120,
  "venueId": "venue_002",
  "targetLevel": "ADVANCED",
  "capacity": 25,
  "description": "Updated description"
}
```

**Response (200 OK):**
```json
{
  "id": "session_002",
  /* ... updated session object ... */
  "updatedAt": "2026-01-04T17:00:00Z"
}
```

**Notes:**
- Cannot change sportId after creation
- If capacity reduced below currentAttendees, return 400
- Notify all attendees if dateTime, venueId, or status changes

**Errors:**
- 400: Invalid updates, capacity too low
- 403: Unauthorized (not creator and lacks privilege)
- 404: Session not found

---

### DELETE /api/sessions/{sessionId}
Cancel session (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `MANAGE_SESSIONS` privilege OR user is session creator

**Response (204 No Content)**

**Notes:**
- Sets status to "CANCELLED"
- Does NOT delete from database (soft delete)
- Notify all attendees
- Release all bookings

**Errors:**
- 403: Unauthorized
- 404: Session not found

---

## 4. Bookings

### POST /api/bookings
Book a session.

**Headers:** `Authorization: Bearer {token}`

**Request:**
```json
{
  "sessionId": "session_001"
}
```

**Response (201 Created):**
```json
{
  "id": "booking_001",
  "sessionId": "session_001",
  "userId": "user_abc123",
  "status": "CONFIRMED",
  "confirmedAt": "2026-01-04T18:00:00Z",
  "createdAt": "2026-01-04T18:00:00Z"
}
```

**Business Logic:**
- Check if session has available capacity
- Check if user already booked this session
- Check if session is SCHEDULED (not CANCELLED or COMPLETED)
- Decrement available capacity atomically

**Errors:**
- 400: Already booked, session full, session not available
- 404: Session not found

---

### DELETE /api/bookings/{bookingId}
Cancel a booking.

**Headers:** `Authorization: Bearer {token}`

**Response (204 No Content)**

**Business Logic:**
- Only user who made booking can cancel
- Cannot cancel if session already started (dateTime < now)
- Increment available capacity atomically
- If waiting list exists, notify next person

**Errors:**
- 403: Not your booking
- 400: Session already started
- 404: Booking not found

---

### GET /api/bookings/my-bookings
Get current user's bookings.

**Headers:** `Authorization: Bearer {token}`

**Query Parameters:**
- `status` (optional): One of ["UPCOMING", "PAST", "CANCELLED"]
- `page` (optional): Page number
- `limit` (optional): Items per page

**Response (200 OK):**
```json
{
  "bookings": [
    {
      "id": "booking_001",
      "sessionId": "session_001",
      "session": {
        /* full session object */
      },
      "status": "CONFIRMED",
      "confirmedAt": "2026-01-04T18:00:00Z",
      "createdAt": "2026-01-04T18:00:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 50,
    "total": 24,
    "totalPages": 1
  },
  "stats": {
    "totalAttended": 24,
    "totalCancelled": 3,
    "upcomingCount": 3
  }
}
```

**Notes:**
- UPCOMING: Sessions with dateTime >= now and status CONFIRMED
- PAST: Sessions with dateTime < now
- CANCELLED: Bookings with status CANCELLED or session status CANCELLED

---

## 5. Sports

### GET /api/sports
Get list of all sports.

**Headers:** `Authorization: Bearer {token}` (optional for public access)

**Response (200 OK):**
```json
{
  "sports": [
    {
      "id": "volleyball",
      "name": "Beach Volleyball",
      "icon": "🏐",
      "description": "Team sport played on sand courts",
      "skillLevels": ["BEGINNER", "INTERMEDIATE", "ADVANCED"],
      "active": true,
      "createdAt": "2025-01-01T00:00:00Z"
    },
    {
      "id": "basketball",
      "name": "Basketball",
      "icon": "🏀",
      "description": "Team sport played on indoor courts",
      "skillLevels": ["BEGINNER", "INTERMEDIATE", "ADVANCED"],
      "active": true,
      "createdAt": "2025-01-01T00:00:00Z"
    }
  ]
}
```

**Notes:**
- Returns all active sports
- Used for sport selection in filters and signup

---

### POST /api/sports
Create new sport (admin only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** Admin role

**Request:**
```json
{
  "id": "soccer",
  "name": "Soccer",
  "icon": "⚽",
  "description": "Most popular sport worldwide",
  "skillLevels": ["BEGINNER", "INTERMEDIATE", "ADVANCED"]
}
```

**Response (201 Created):**
```json
{
  "id": "soccer",
  /* ... full sport object ... */
  "createdAt": "2026-01-04T18:00:00Z"
}
```

**Errors:**
- 403: Unauthorized (not admin)
- 409: Sport ID already exists

---

## 6. Venues

### GET /api/venues
Get list of all venues.

**Headers:** `Authorization: Bearer {token}`

**Query Parameters:**
- `sportId` (optional): Filter venues that support this sport
- `active` (optional): true/false (default: true)

**Response (200 OK):**
```json
{
  "venues": [
    {
      "id": "venue_001",
      "name": "Downtown Court",
      "address": "123 Main St, Porto, Portugal",
      "coordinates": {
        "latitude": 41.1579,
        "longitude": -8.6291
      },
      "capacity": 20,
      "supportedSports": ["volleyball", "basketball"],
      "facilities": ["parking", "showers", "lockers"],
      "active": true,
      "createdAt": "2025-06-01T10:00:00Z",
      "updatedAt": "2026-01-01T12:00:00Z"
    }
  ]
}
```

---

### POST /api/venues
Create new venue (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `MANAGE_VENUES` privilege

**Request:**
```json
{
  "name": "North Field",
  "address": "456 North Ave, Porto, Portugal",
  "coordinates": {
    "latitude": 41.1700,
    "longitude": -8.6400
  },
  "capacity": 30,
  "supportedSports": ["soccer", "volleyball"],
  "facilities": ["parking", "lighting"]
}
```

**Response (201 Created):**
```json
{
  "id": "venue_003",
  /* ... full venue object ... */
  "createdAt": "2026-01-04T19:00:00Z"
}
```

**Validation:**
- name: Required, 2-100 characters
- address: Required
- capacity: 1-500
- supportedSports: At least one, must be valid sport IDs

**Errors:**
- 400: Validation errors
- 403: Unauthorized (lacks MANAGE_VENUES privilege)

---

### PUT /api/venues/{venueId}
Update venue (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `MANAGE_VENUES` privilege

**Request:**
```json
{
  "name": "North Field (Updated)",
  "capacity": 35,
  "facilities": ["parking", "lighting", "showers"]
}
```

**Response (200 OK):**
```json
{
  "id": "venue_003",
  /* ... updated venue object ... */
  "updatedAt": "2026-01-04T19:30:00Z"
}
```

---

### DELETE /api/venues/{venueId}
Deactivate venue (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `MANAGE_VENUES` privilege

**Response (204 No Content)**

**Notes:**
- Soft delete (set active = false)
- Cannot delete if venue has future scheduled sessions

**Errors:**
- 400: Venue has future sessions
- 403: Unauthorized

---

## 7. Committee & Admin

### POST /api/calendar/unlock
Unlock calendar period for session creation (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `MANAGE_CALENDAR` privilege

**Request:**
```json
{
  "sportId": "volleyball",
  "startDate": "2026-02-01",
  "endDate": "2026-02-28"
}
```

**Response (201 Created):**
```json
{
  "id": "unlock_001",
  "sportId": "volleyball",
  "startDate": "2026-02-01",
  "endDate": "2026-02-28",
  "unlockedBy": "user_abc123",
  "unlockedByName": "John Doe",
  "createdAt": "2026-01-04T20:00:00Z"
}
```

**Business Logic:**
- Enforce 3-month maximum ahead limit
- Enforce sequential unlocking (no gaps)
- Per-sport calendars are independent

**Validation:**
- endDate - startDate <= 3 months
- startDate must be sequential with existing unlocked periods (no gaps)

**Errors:**
- 400: Exceeds 3-month limit, creates gap in unlocked periods
- 403: Unauthorized (lacks MANAGE_CALENDAR privilege)

---

### GET /api/calendar/unlocked-periods
Get unlocked calendar periods.

**Headers:** `Authorization: Bearer {token}`

**Requires:** `MANAGE_CALENDAR` privilege

**Query Parameters:**
- `sportId` (optional): Filter by sport

**Response (200 OK):**
```json
{
  "periods": [
    {
      "id": "unlock_001",
      "sportId": "volleyball",
      "startDate": "2026-01-01",
      "endDate": "2026-01-31",
      "unlockedBy": "user_abc123",
      "unlockedByName": "John Doe",
      "createdAt": "2025-12-15T10:00:00Z"
    }
  ]
}
```

---

### POST /api/committee/invite
Invite new member (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `MANAGE_ATHLETES` privilege

**Request:**
```json
{
  "email": "newmember@example.com",
  "name": "New Member",
  "message": "Welcome to DouroBats!"
}
```

**Response (201 Created):**
```json
{
  "invitationId": "invite_001",
  "email": "newmember@example.com",
  "status": "PENDING",
  "expiresAt": "2026-01-11T20:00:00Z",
  "createdAt": "2026-01-04T20:00:00Z"
}
```

**Notes:**
- Sends invitation email with signup link
- Invitation expires in 7 days

**Errors:**
- 400: Email already registered
- 403: Unauthorized

---

### PUT /api/users/{userId}/skill-level
Update user's skill level for a sport (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `MANAGE_ATHLETES` privilege

**Request:**
```json
{
  "sportId": "volleyball",
  "level": "ADVANCED",
  "reason": "Promoted after excellent performance in tournament"
}
```

**Response (200 OK):**
```json
{
  "userId": "user_xyz789",
  "sportId": "volleyball",
  "level": "ADVANCED",
  "updatedBy": "user_abc123",
  "updatedByName": "John Doe",
  "reason": "Promoted after excellent performance in tournament",
  "updatedAt": "2026-01-04T21:00:00Z"
}
```

**Notes:**
- Creates audit trail entry
- Skill level is a recommendation, not a restriction

---

### PUT /api/users/{userId}/privileges
Assign committee privileges (admin only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** Admin role

**Request:**
```json
{
  "privileges": ["MANAGE_SESSIONS", "VIEW_ANALYTICS"]
}
```

**Response (200 OK):**
```json
{
  "userId": "user_xyz789",
  "privileges": ["MANAGE_SESSIONS", "VIEW_ANALYTICS"],
  "assignedBy": "admin_user_001",
  "assignedByName": "Admin User",
  "updatedAt": "2026-01-04T21:30:00Z"
}
```

**Valid Privileges:**
- `MANAGE_SESSIONS`
- `MANAGE_CALENDAR`
- `MANAGE_ATHLETES`
- `MANAGE_VENUES`
- `VIEW_ANALYTICS`

**Notes:**
- Creates audit trail entry
- User gets "committee" role if they have any privilege

---

## 8. Analytics

### GET /api/analytics/attendance
Get attendance statistics (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `VIEW_ANALYTICS` privilege

**Query Parameters:**
- `sportId` (optional): Filter by sport
- `venueId` (optional): Filter by venue
- `startDate` (required): ISO date
- `endDate` (required): ISO date

**Response (200 OK):**
```json
{
  "period": {
    "startDate": "2026-01-01",
    "endDate": "2026-01-31"
  },
  "totalSessions": 45,
  "totalBookings": 680,
  "totalAttendance": 650,
  "attendanceRate": 0.96,
  "averageAttendeesPerSession": 14.4,
  "byDay": [
    {
      "day": "MONDAY",
      "sessions": 8,
      "bookings": 120,
      "attendance": 115
    }
  ],
  "bySport": [
    {
      "sportId": "volleyball",
      "sportName": "Beach Volleyball",
      "sessions": 20,
      "bookings": 320,
      "attendanceRate": 0.97
    }
  ],
  "byVenue": [
    {
      "venueId": "venue_001",
      "venueName": "Downtown Court",
      "sessions": 15,
      "bookings": 240,
      "attendanceRate": 0.95
    }
  ]
}
```

**Notes:**
- attendanceRate = (actual attendees / total bookings)
- Date range max 1 year

**Errors:**
- 400: Invalid date range (> 1 year)
- 403: Unauthorized (lacks VIEW_ANALYTICS privilege)

---

### GET /api/analytics/popular-sessions
Get most popular session times/types (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `VIEW_ANALYTICS` privilege

**Query Parameters:**
- `sportId` (optional): Filter by sport
- `startDate` (required): ISO date
- `endDate` (required): ISO date
- `limit` (optional): Max results (default: 10)

**Response (200 OK):**
```json
{
  "popularTimes": [
    {
      "dayOfWeek": "FRIDAY",
      "hour": 18,
      "averageAttendance": 18.5,
      "sessionCount": 12
    }
  ],
  "popularSports": [
    {
      "sportId": "volleyball",
      "sportName": "Beach Volleyball",
      "totalSessions": 20,
      "averageAttendance": 16.2
    }
  ],
  "popularVenues": [
    {
      "venueId": "venue_001",
      "venueName": "Downtown Court",
      "totalSessions": 15,
      "averageAttendance": 17.8
    }
  ]
}
```

---

### GET /api/analytics/user-participation
Get user participation metrics (committee only).

**Headers:** `Authorization: Bearer {token}`

**Requires:** `VIEW_ANALYTICS` privilege

**Query Parameters:**
- `sportId` (optional): Filter by sport
- `startDate` (required): ISO date
- `endDate` (required): ISO date

**Response (200 OK):**
```json
{
  "totalActiveUsers": 120,
  "newUsersThisPeriod": 15,
  "topParticipants": [
    {
      "userId": "user_xyz789",
      "userName": "Jane Doe",
      "sessionsAttended": 28,
      "attendanceRate": 0.98
    }
  ],
  "participationBySkillLevel": {
    "BEGINNER": 45,
    "INTERMEDIATE": 60,
    "ADVANCED": 15
  }
}
```

---

## 9. Error Handling

### Standard Error Response Format

All errors should return consistent JSON structure:

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid request parameters",
    "details": [
      {
        "field": "email",
        "message": "Email format is invalid"
      },
      {
        "field": "password",
        "message": "Password must be at least 8 characters"
      }
    ],
    "timestamp": "2026-01-04T22:00:00Z",
    "path": "/api/auth/signup"
  }
}
```

### Error Codes

| HTTP Status | Error Code | Description |
|------------|------------|-------------|
| 400 | VALIDATION_ERROR | Request validation failed |
| 400 | INVALID_REQUEST | Malformed request |
| 401 | UNAUTHORIZED | Missing or invalid authentication |
| 403 | FORBIDDEN | Insufficient permissions |
| 404 | NOT_FOUND | Resource not found |
| 409 | CONFLICT | Resource conflict (e.g., duplicate email) |
| 422 | UNPROCESSABLE_ENTITY | Semantic errors |
| 429 | RATE_LIMIT_EXCEEDED | Too many requests |
| 500 | INTERNAL_ERROR | Server error |
| 503 | SERVICE_UNAVAILABLE | Service temporarily unavailable |

### Rate Limiting

Apply rate limiting headers to all responses:

```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1704409200
```

**Limits:**
- Authentication endpoints: 10 requests/minute
- Read endpoints: 100 requests/minute
- Write endpoints: 30 requests/minute

---

## 10. Data Models

### User
```typescript
{
  id: string              // Unique user ID
  name: string            // Full name
  email: string           // Email (unique)
  sports: string[]        // List of sport IDs
  roles: string[]         // ["athlete", "supporter", "committee"]
  privileges: string[]    // Committee privileges
  skillLevels: {          // Skill level per sport
    [sportId: string]: "BEGINNER" | "INTERMEDIATE" | "ADVANCED"
  }
  avatarUrl?: string      // CDN URL to avatar image
  language: string        // One of ["en", "pt-PT", "pt-BR", "es-ES", "en-GB"]
  theme: string           // "LIGHT" | "DARK" | "SYSTEM"
  notificationPreferences: {
    sessionReminders: boolean
    sessionCancellations: boolean
    waitlistOpenings: boolean
  }
  createdAt: string       // ISO 8601 timestamp
  updatedAt: string       // ISO 8601 timestamp
}
```

### Session
```typescript
{
  id: string              // Unique session ID
  sportId: string         // FK to Sport
  sportName: string       // Denormalized for convenience
  sportIcon: string       // Emoji or icon identifier
  dateTime: string        // ISO 8601 timestamp (UTC)
  duration: number        // Minutes
  venueId: string         // FK to Venue
  venueName: string       // Denormalized
  venueAddress: string    // Denormalized
  targetLevel?: string    // "BEGINNER" | "INTERMEDIATE" | "ADVANCED" | null
  capacity: number        // Max attendees
  currentAttendees: number // Current bookings count
  status: string          // "SCHEDULED" | "CANCELLED" | "COMPLETED"
  description?: string    // Optional description
  createdBy: string       // User ID who created
  createdByName: string   // Denormalized
  isUserBooked: boolean   // True if authenticated user booked this session
  attendees?: Array<{     // Only in GET /sessions/{id}
    userId: string
    userName: string
    userAvatar?: string
    confirmedAt: string
  }>
  createdAt: string       // ISO 8601 timestamp
  updatedAt: string       // ISO 8601 timestamp
}
```

### Booking
```typescript
{
  id: string              // Unique booking ID
  sessionId: string       // FK to Session
  userId: string          // FK to User
  status: string          // "CONFIRMED" | "CANCELLED"
  confirmedAt: string     // ISO 8601 timestamp
  cancelledAt?: string    // ISO 8601 timestamp (if cancelled)
  createdAt: string       // ISO 8601 timestamp
}
```

### Sport
```typescript
{
  id: string              // Unique identifier (e.g., "volleyball")
  name: string            // Display name
  icon: string            // Emoji or icon identifier
  description?: string    // Optional description
  skillLevels: string[]   // ["BEGINNER", "INTERMEDIATE", "ADVANCED"]
  active: boolean         // Whether sport is active
  createdAt: string       // ISO 8601 timestamp
}
```

### Venue
```typescript
{
  id: string              // Unique venue ID
  name: string            // Venue name
  address: string         // Full address
  coordinates?: {         // Optional GPS coordinates
    latitude: number
    longitude: number
  }
  capacity: number        // Max capacity
  supportedSports: string[] // List of sport IDs
  facilities: string[]    // ["parking", "showers", "lockers", "lighting"]
  active: boolean         // Whether venue is active
  createdAt: string       // ISO 8601 timestamp
  updatedAt: string       // ISO 8601 timestamp
}
```

### CalendarUnlock
```typescript
{
  id: string              // Unique unlock ID
  sportId: string         // FK to Sport
  startDate: string       // ISO date (YYYY-MM-DD)
  endDate: string         // ISO date (YYYY-MM-DD)
  unlockedBy: string      // User ID who unlocked
  unlockedByName: string  // Denormalized
  createdAt: string       // ISO 8601 timestamp
}
```

---

## Implementation Notes

### Authentication & Security
- Use JWT with RS256 algorithm
- Access tokens: 15 minutes expiry
- Refresh tokens: 30 days expiry
- Store refresh tokens in secure database table with revocation support
- Implement token blacklist for logout
- Hash passwords with bcrypt (cost factor 12)
- Enforce HTTPS in production
- Implement CORS with whitelist of allowed origins

### Performance
- Implement database indexing:
  - Users: email (unique), id
  - Sessions: dateTime, sportId, venueId, status
  - Bookings: sessionId, userId, composite (sessionId + userId unique)
- Use connection pooling
- Implement caching for:
  - GET /api/sports (cache 1 hour)
  - GET /api/venues (cache 15 minutes)
  - User profile (cache 5 minutes with invalidation on update)
- Paginate all list endpoints (default 50, max 100 items)

### Database Constraints
- Users.email: UNIQUE
- Bookings (sessionId, userId): UNIQUE (prevent double booking)
- Sessions.currentAttendees: CHECK (currentAttendees <= capacity)
- Calendar period overlaps: Prevent overlapping periods for same sport

### Notifications
Implement webhook/push notification system for:
- Session reminder (24 hours before)
- Session cancellation (immediate)
- Session time/venue change (immediate)
- Waitlist spot available (immediate)
- New announcement (immediate)

### Audit Trail
Log the following events:
- User privilege changes (who, what, when, why)
- Skill level changes (who, what, when, reason)
- Calendar unlocks (who, sport, period, when)
- Session creates/updates/cancels (who, what, when)

Store in separate audit_log table:
```typescript
{
  id: string
  entityType: string  // "USER" | "SESSION" | "CALENDAR"
  entityId: string
  action: string      // "CREATE" | "UPDATE" | "DELETE" | "PRIVILEGE_CHANGE"
  performedBy: string // User ID
  changes: JSON       // Old and new values
  reason?: string     // Optional reason/note
  createdAt: string
}
```

### Testing Requirements
- Unit tests for all business logic
- Integration tests for all endpoints
- Load testing for:
  - Concurrent bookings (race conditions)
  - Session creation
  - User authentication
- Edge cases:
  - Double booking prevention
  - Capacity overflow handling
  - Timezone edge cases
  - Expired token handling

---

## API Versioning

Current version: **v1**

Base path: `/api/v1/*` (optional, can use `/api/*` for v1)

Future versions will use:
- `/api/v2/*`
- HTTP Header: `API-Version: 2`

---

## Deployment Environments

### Development
- URL: `http://localhost:8080`
- Database: Local PostgreSQL
- Auth: Relaxed (no email verification)

### Staging
- URL: `https://staging-api.dourobats.pt`
- Database: Staging database
- Auth: Full verification enabled
- Used for testing before production

### Production
- URL: `https://api.dourobats.pt`
- Database: Production database (with backups)
- Auth: Full verification, rate limiting, monitoring
- CDN: CloudFlare or AWS CloudFront
- Monitoring: Application Performance Monitoring (APM)

---

## Summary

This API specification covers:
- ✅ Authentication (signup, login, social auth, password reset)
- ✅ User management (profile, avatar, preferences)
- ✅ Session management (CRUD, filtering, searching)
- ✅ Booking system (create, cancel, history)
- ✅ Sports and venues management
- ✅ Committee/admin features (calendar unlock, privileges, invitations)
- ✅ Analytics (attendance, participation, popular sessions)
- ✅ Comprehensive error handling
- ✅ Complete data models

**Total Endpoints:** 40+

**Priority for MVP:**
1. Authentication (signup, login, logout)
2. Sessions (GET, POST, PUT, DELETE)
3. Bookings (POST, DELETE, GET my-bookings)
4. Sports (GET)
5. Venues (GET)
6. User profile (GET /users/me)

**Post-MVP:**
- Social authentication
- Calendar unlock system
- Committee privileges
- Analytics
- Advanced features (waiting list, notifications)

This specification should provide a complete guide for backend development aligned with all mobile app user stories.
