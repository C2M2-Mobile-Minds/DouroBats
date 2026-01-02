# DB036: HTTP Client Setup with Ktor

## Overview

This document explains the architectural decisions and implementation details for setting up the Ktor HTTP client in the DouroBats MVP application.

**User Story:**
> As a developer, I need HTTP client configured so that I can make API calls

**Status:** ✅ Completed

**Implementation Date:** 2026-01-02

---

## Architecture Overview

The HTTP client implementation follows the project's existing Clean Architecture patterns and integrates seamlessly with the KMP (Kotlin Multiplatform) structure.

### Module Structure

```
core/network/
├── src/
│   ├── commonMain/
│   │   └── kotlin/pt/dourobats/app/core/network/
│   │       ├── config/           # Environment and API configuration
│   │       ├── auth/             # Authentication token provider
│   │       ├── client/           # HTTP client factory and configuration
│   │       └── di/               # Koin dependency injection module
│   ├── androidMain/
│   │   └── kotlin/pt/dourobats/app/core/network/client/
│   │       └── HttpEngine.android.kt  # OkHttp engine
│   ├── iosMain/
│   │   └── kotlin/pt/dourobats/app/core/network/client/
│   │       └── HttpEngine.ios.kt      # Darwin engine
│   └── commonTest/
│       └── kotlin/pt/dourobats/app/core/network/client/
│           └── HttpClientFactoryTest.kt  # Unit tests
```

---

## Technology Choices

### Why Ktor 3.0.2?

**Selected:** Ktor 3.0.2 (latest stable as of implementation date)

**Reasons:**
1. **Native KMP Support:** Ktor is built specifically for Kotlin Multiplatform
2. **Kotlin-First Design:** Idiomatic Kotlin with coroutines and type safety
3. **Plugin Architecture:** Modular configuration with ContentNegotiation, Auth, Logging, etc.
4. **Lightweight:** Smaller footprint compared to alternatives like Retrofit
5. **Active Development:** JetBrains-backed with regular updates
6. **Kotlinx Serialization Integration:** Seamless JSON handling with kotlinx.serialization
7. **Platform-Specific Engines:** OkHttp for Android, URLSession (Darwin) for iOS

**Alternatives Considered:**
- **Retrofit:** Android-only, not KMP-native
- **Apollo (GraphQL):** Overkill for REST APIs
- **Plain URLConnection:** Too low-level, no built-in features

---

## Design Decisions

### 1. Environment Management

**Approach:** Simple enum-based configuration

**File:** `core/network/src/commonMain/kotlin/pt/dourobats/app/core/network/config/Environment.kt`

```kotlin
enum class Environment(val baseUrl: String, val enableLogging: Boolean) {
    DEVELOPMENT("http://localhost:8080", true),
    STAGING("https://staging-api.dourobats.pt", true),
    PRODUCTION("https://api.dourobats.pt", false)
}
```

**Why This Approach?**
- ✅ **Simple and Pragmatic:** No build variants or complex configuration needed in Phase 1
- ✅ **Easy to Extend:** Can be replaced with BuildConfig or feature flags in Phase 2
- ✅ **Clear Intent:** Each environment's configuration is explicit
- ✅ **Type-Safe:** Compile-time safety for environment properties

**Alternatives Considered:**
- **BuildConfig with Gradle:** Adds complexity, not needed yet (no backend integration)
- **Runtime Configuration:** Could use DataStore, but overkill for current phase
- **Build Variants:** Android-specific, harder to manage for KMP

**Future Enhancement (Phase 2):**
```kotlin
// With build variants
android {
    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"http://localhost:8080\"")
        }
        release {
            buildConfigField("String", "API_URL", "\"https://api.dourobats.pt\"")
        }
    }
}
```

---

### 2. Platform-Specific HTTP Engines

**Approach:** expect/actual pattern for platform engines

**Why?**
- ✅ **Follows Project Patterns:** Matches existing DataStore implementation
- ✅ **Clean Abstraction:** Common code doesn't know about platforms
- ✅ **Optimal Performance:** Native engines for each platform
  - **Android:** OkHttp (industry standard, battle-tested)
  - **iOS:** Darwin/URLSession (native Apple networking)

**Files:**
- `HttpEngine.kt` (commonMain - expect declaration)
- `HttpEngine.android.kt` (androidMain - OkHttp actual)
- `HttpEngine.ios.kt` (iosMain - Darwin actual)

**Code Example:**
```kotlin
// commonMain
expect fun createHttpEngine(): HttpClientEngine

// androidMain
actual fun createHttpEngine() = OkHttp.create()

// iosMain
actual fun createHttpEngine() = Darwin.create()
```

---

### 3. Plugin Configuration Strategy

**Approach:** Extension functions for each plugin

**File:** `core/network/src/commonMain/kotlin/pt/dourobats/app/core/network/client/HttpClientConfig.kt`

**Why Extension Functions?**
- ✅ **Single Responsibility:** Each function configures one plugin
- ✅ **Composable:** Easy to add/remove plugins
- ✅ **Testable:** Can test configuration logic in isolation
- ✅ **Readable:** Clear intent with descriptive names

**Plugins Configured:**

#### 1. ContentNegotiation (JSON)
```kotlin
fun HttpClientConfig<*>.configureJsonSerialization() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true  // Resilient to API changes
            isLenient = true
            prettyPrint = true
        })
    }
}
```
- **ignoreUnknownKeys:** Backend can add fields without breaking mobile app
- **isLenient:** Handles less strict JSON formats

#### 2. HttpTimeout
```kotlin
fun HttpClientConfig<*>.configureTimeout() {
    install(HttpTimeout) {
        requestTimeoutMillis = 30_000  // 30 seconds
        connectTimeoutMillis = 15_000  // 15 seconds
        socketTimeoutMillis = 30_000   // 30 seconds
    }
}
```
- **Industry Standard Values:** Based on HTTP client best practices
- **Balance:** Long enough for slow networks, short enough to avoid hanging

#### 3. Logging
```kotlin
fun HttpClientConfig<*>.configureLogging() {
    if (ApiConfig.enableLogging) {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL  // Headers, body, info
        }
    }
}
```
- **Conditional:** Only enabled in dev/staging (not production)
- **LogLevel.ALL:** Full debugging info during development

#### 4. Bearer Token Auth
```kotlin
fun HttpClientConfig<*>.configureAuth(tokenProvider: TokenProvider) {
    install(Auth) {
        bearer {
            loadTokens {
                tokenProvider.getToken()?.let {
                    BearerTokens(accessToken = it, refreshToken = "")
                }
            }
        }
    }
}
```
- **Deferred Auth:** Uses NoOpTokenProvider in Phase 1, ready for real auth in Phase 2
- **Automatic Header Injection:** Ktor adds `Authorization: Bearer <token>` to requests

---

### 4. Token Provider Interface

**Approach:** Interface with NoOp implementation for Phase 1

**File:** `core/network/src/commonMain/kotlin/pt/dourobats/app/core/network/auth/TokenProvider.kt`

```kotlin
interface TokenProvider {
    suspend fun getToken(): String?
    suspend fun clearToken()
}

class NoOpTokenProvider : TokenProvider {
    override suspend fun getToken(): String? = null
    override suspend fun clearToken() {}
}
```

**Why This Design?**
- ✅ **Prepared for Future:** Auth module can provide real implementation later
- ✅ **No Breaking Changes:** Switching from NoOp to real provider is seamless
- ✅ **Follows Repository Pattern:** Matches existing project architecture
- ✅ **Suspend Functions:** Ready for async token retrieval (e.g., from DataStore)

**Phase 2 Implementation Example:**
```kotlin
class DataStoreTokenProvider(
    private val dataStore: DataStore<Preferences>
) : TokenProvider {
    override suspend fun getToken(): String? {
        return dataStore.data.first()[TOKEN_KEY]
    }
    override suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }
}
```

---

### 5. Koin DI Integration

**Approach:** Single networkModule with HttpClient and TokenProvider

**File:** `core/network/src/commonMain/kotlin/pt/dourobats/app/core/network/di/NetworkModule.kt`

```kotlin
val networkModule = module {
    single<TokenProvider> { NoOpTokenProvider() }
    single<HttpClient> {
        HttpClientFactory.create(
            engine = createHttpEngine(),
            tokenProvider = get()
        )
    }
}
```

**Why This Pattern?**
- ✅ **Follows Project Convention:** Matches existing dataModule pattern
- ✅ **Singleton Scope:** HttpClient should be reused (recommended by Ktor)
- ✅ **Dependency Injection:** TokenProvider can be swapped in Phase 2
- ✅ **Clean Separation:** Network layer is self-contained

**Shared Koin Initialization:**

**File:** `composeApp/src/commonMain/kotlin/pt/dourobats/app/di/KoinInitializer.kt`

```kotlin
fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(networkModule, dataModule, settingsModule)
    }
}
```

**Benefits:**
- ✅ **Platform-Agnostic:** iOS and Android use same initialization
- ✅ **Single Source of Truth:** Module list maintained in one place
- ✅ **Reduces Duplication:** No need to repeat module list in each platform

---

## Configuration Parameters

### Timeouts

| Parameter | Value | Reasoning |
|-----------|-------|-----------|
| **Request Timeout** | 30s | Maximum time to wait for complete response |
| **Connect Timeout** | 15s | Maximum time to establish connection |
| **Socket Timeout** | 30s | Maximum idle time between data packets |

**Why These Values?**
- Based on [HTTP client best practices](https://www.oreilly.com/library/view/http-the-definitive/1565925092/)
- Long enough for slow mobile networks (3G, edge cases)
- Short enough to avoid indefinite hanging

### Base URLs

| Environment | URL | Logging Enabled |
|-------------|-----|-----------------|
| **Development** | `http://localhost:8080` | ✅ Yes |
| **Staging** | `https://staging-api.dourobats.pt` | ✅ Yes |
| **Production** | `https://api.dourobats.pt` | ❌ No |

---

## Testing Strategy

**File:** `core/network/src/commonTest/kotlin/pt/dourobats/app/core/network/client/HttpClientFactoryTest.kt`

**Test Coverage:**
- ✅ HttpClient creation with MockEngine
- ✅ Successful HTTP request handling
- ✅ JSON serialization/deserialization (via ContentNegotiation)

**MockEngine Benefits:**
- No real network calls in tests
- Fast, deterministic tests
- Can simulate various responses (success, errors, timeouts)

**Example Test:**
```kotlin
@Test
fun `HttpClient successfully makes request with mock engine`() = runTest {
    val mockEngine = MockEngine { request ->
        respond(
            content = ByteReadChannel("""{"message":"success"}"""),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }

    val client = HttpClientFactory.create(mockEngine, NoOpTokenProvider())
    val response = client.get("http://test.com/api/test")

    assertEquals(HttpStatusCode.OK, response.status)
}
```

---

## Usage Examples

### Injecting HttpClient in Repositories

**Future implementation example:**

```kotlin
// In core/data module
class ApiTrainingRepository(
    private val httpClient: HttpClient
) : TrainingRepository {

    override suspend fun getTrainingSessions(): Result<List<TrainingSession>> {
        return try {
            val response = httpClient.get("/api/training/sessions")
            val dtos = response.body<List<TrainingSessionDto>>()
            Result.success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// In DI module
val trainingModule = module {
    single<TrainingRepository> {
        ApiTrainingRepository(get())  // get() injects HttpClient
    }
}
```

### Making API Calls

```kotlin
// GET request
val sessions = httpClient.get("/api/training/sessions")
    .body<List<TrainingSessionDto>>()

// POST request with body
val newSession = httpClient.post("/api/training/sessions") {
    setBody(CreateSessionRequest(title = "Morning Practice"))
}.body<TrainingSessionDto>()

// With auth token (automatic via TokenProvider)
// HttpClient will automatically add: Authorization: Bearer <token>
```

---

## Future Phase 2 Enhancements

When backend API is ready, the following enhancements will be added:

### 1. Real Token Provider
```kotlin
class DataStoreTokenProvider(
    private val dataStore: DataStore<Preferences>
) : TokenProvider {
    override suspend fun getToken(): String? {
        return dataStore.data.first()[TOKEN_KEY]
    }
}

// Update networkModule
val networkModule = module {
    single<TokenProvider> { DataStoreTokenProvider(get()) }  // Replace NoOp
    single<HttpClient> { HttpClientFactory.create(createHttpEngine(), get()) }
}
```

### 2. Build Variants for Environment Switching
```kotlin
android {
    buildTypes {
        debug { buildConfigField("String", "ENV", "\"DEVELOPMENT\"") }
        release { buildConfigField("String", "ENV", "\"PRODUCTION\"") }
    }
}

// Update Environment.kt
val current = when (BuildConfig.ENV) {
    "DEVELOPMENT" -> DEVELOPMENT
    "STAGING" -> STAGING
    "PRODUCTION" -> PRODUCTION
    else -> DEVELOPMENT
}
```

### 3. Enhanced Error Handling
```kotlin
sealed class NetworkException : Exception() {
    data class HttpError(val code: Int, val message: String) : NetworkException()
    data class Timeout(override val message: String) : NetworkException()
    data class NoInternet(override val message: String) : NetworkException()
}

// In HttpClientConfig
expectSuccess = false  // Handle errors manually

// In repositories
when (response.status.value) {
    in 200..299 -> Result.success(response.body())
    401 -> throw NetworkException.HttpError(401, "Unauthorized")
    else -> throw NetworkException.HttpError(response.status.value, "Error")
}
```

### 4. Refresh Token Logic
```kotlin
fun HttpClientConfig<*>.configureAuth(tokenProvider: TokenProvider) {
    install(Auth) {
        bearer {
            loadTokens {
                tokenProvider.getToken()?.let {
                    BearerTokens(accessToken = it, refreshToken = "")
                }
            }

            refreshTokens {
                // Call refresh endpoint
                val newToken = authRepository.refreshToken()
                tokenProvider.saveToken(newToken)
                BearerTokens(accessToken = newToken, refreshToken = "")
            }
        }
    }
}
```

### 5. Custom Logging
```kotlin
object CustomKtorLogger : Logger {
    override fun log(message: String) {
        // Send to analytics/crash reporting
        if (ApiConfig.environment == PRODUCTION) {
            analyticsService.logNetworkEvent(message)
        } else {
            println(message)
        }
    }
}
```

---

## Acceptance Criteria

All acceptance criteria from the user story have been met:

- ✅ **Ktor client configured in core/network**
  - `HttpClientFactory` creates configured client
  - All plugins set up (ContentNegotiation, Auth, Logging, HttpTimeout)

- ✅ **JSON serialization works**
  - kotlinx.serialization integrated via ContentNegotiation plugin
  - `ignoreUnknownKeys = true` for API resilience

- ✅ **Bearer token auth configured**
  - Auth plugin with bearer token support
  - `TokenProvider` interface for future auth implementation
  - `NoOpTokenProvider` for Phase 1

- ✅ **Logging enabled for debugging**
  - Logging plugin configured with LogLevel.ALL
  - Conditionally enabled based on environment (dev/staging only)

- ✅ **Timeouts configured**
  - Request timeout: 30s
  - Connect timeout: 15s
  - Socket timeout: 30s

- ✅ **Environment switching supported**
  - `Environment` enum with DEVELOPMENT, STAGING, PRODUCTION
  - Easy to extend with build variants in Phase 2

- ✅ **Works on both platforms**
  - OkHttp engine for Android
  - Darwin (URLSession) engine for iOS
  - expect/actual pattern for platform abstraction

---

## Dependencies Added

### Version Catalog (`gradle/libs.versions.toml`)

**Version:**
```toml
ktor = "3.0.2"
```

**Libraries:**
```toml
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" }
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
ktor-client-auth = { module = "io.ktor:ktor-client-auth", version.ref = "ktor" }
ktor-client-logging = { module = "io.ktor:ktor-client-logging", version.ref = "ktor" }
ktor-client-mock = { module = "io.ktor:ktor-client-mock", version.ref = "ktor" }
```

### Module Dependencies (`core/network/build.gradle.kts`)

**Common:**
- ktor-client-core
- ktor-client-content-negotiation
- ktor-serialization-kotlinx-json
- ktor-client-auth
- ktor-client-logging

**Android:**
- ktor-client-okhttp

**iOS:**
- ktor-client-darwin

**Test:**
- ktor-client-mock
- kotlinx-coroutines-test

---

## Trade-offs and Limitations

### Current Phase 1 Limitations

1. **No Real Authentication:**
   - Using `NoOpTokenProvider` that returns null
   - Auth plugin is configured but inactive until Phase 2

2. **Static Environment Configuration:**
   - Hardcoded to `DEVELOPMENT` environment
   - No runtime switching or build variant support yet

3. **Basic Error Handling:**
   - `expectSuccess = true` throws exceptions for HTTP errors
   - No custom error types or retry logic yet

4. **Simple Logging:**
   - Uses Ktor's SIMPLE logger
   - No integration with analytics/crash reporting

### Why These Trade-offs?

- ✅ **Pragmatic for Phase 1:** No backend integration yet, so advanced features aren't needed
- ✅ **Easy to Extend:** All limitations can be addressed in Phase 2 without refactoring
- ✅ **Follows YAGNI Principle:** Don't implement what you don't need yet
- ✅ **Production-Ready Foundation:** Architecture supports future enhancements

---

## References

- [Ktor Documentation](https://ktor.io/docs/client.html)
- [Ktor KMP Guide](https://ktor.io/docs/kmp-overview.html)
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)
- [Koin Documentation](https://insert-koin.io/)
- [DouroBats Architecture Docs](../architecture.md)
- [DouroBats Data Flow](../data-flow.md)

---

## Conclusion

The HTTP client setup provides a solid, extensible foundation for API integration while remaining pragmatic for the current phase. All design decisions align with the project's existing patterns and can be seamlessly extended when backend integration is needed in Phase 2.

The implementation successfully balances:
- **Simplicity:** Easy to understand and maintain
- **Extensibility:** Ready for future enhancements
- **Platform Support:** Works seamlessly on Android and iOS
- **Best Practices:** Industry-standard configurations and patterns
