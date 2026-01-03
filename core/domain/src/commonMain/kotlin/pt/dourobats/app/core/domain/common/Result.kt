package pt.dourobats.app.core.domain.common

/**
 * A sealed class representing the result of an asynchronous operation.
 *
 * This wrapper provides a standardized way to handle the three possible states of async operations:
 * - [Loading]: Operation is in progress
 * - [Success]: Operation completed successfully with data
 * - [Error]: Operation failed with an exception
 *
 * ## Usage Example
 *
 * ### Basic Usage
 * ```kotlin
 * suspend fun fetchUser(id: String): Result<User> {
 *     return try {
 *         val user = api.getUser(id)
 *         Result.Success(user)
 *     } catch (e: Exception) {
 *         Result.Error(e, "Failed to fetch user")
 *     }
 * }
 * ```
 *
 * ### Collecting in UI
 * ```kotlin
 * viewModel.userState.collectAsState().value.let { result ->
 *     when (result) {
 *         is Result.Loading -> LoadingIndicator()
 *         is Result.Success -> UserProfile(user = result.data)
 *         is Result.Error -> ErrorMessage(result.message ?: "Unknown error")
 *     }
 * }
 * ```
 *
 * ### Using Helper Functions
 * ```kotlin
 * result
 *     .onSuccess { user -> println("Loaded: ${user.name}") }
 *     .onError { error -> logger.error("Failed", error) }
 *
 * val user: User? = result.getOrNull()
 * val userOrThrow: User = result.getOrThrow()
 * ```
 *
 * ### In Repository
 * ```kotlin
 * override suspend fun getSession(id: String): Result<Session> {
 *     return try {
 *         Result.Loading()
 *         val session = apiClient.get("/sessions/$id").body<Session>()
 *         Result.Success(session)
 *     } catch (e: Exception) {
 *         Result.Error(e, "Failed to load session")
 *     }
 * }
 * ```
 *
 * @param T The type of data returned on success
 */
sealed class Result<out T> {

    /**
     * Represents an operation in progress.
     *
     * Use this state to show loading indicators in the UI.
     *
     * @param T The type of data that will be returned on success
     */
    class Loading<T> : Result<T>() {
        override fun equals(other: Any?): Boolean = other is Loading<*>
        override fun hashCode(): Int = Loading::class.hashCode()
        override fun toString(): String = "Result.Loading"
    }

    /**
     * Represents a successful operation with data.
     *
     * @param data The successful result data
     * @param T The type of data returned
     */
    data class Success<T>(val data: T) : Result<T>() {
        override fun toString(): String = "Result.Success(data=$data)"
    }

    /**
     * Represents a failed operation with error information.
     *
     * @param exception The throwable that caused the failure
     * @param message Optional human-readable error message (defaults to exception message)
     * @param T The type of data that was expected
     */
    data class Error<T>(
        val exception: Throwable,
        val message: String? = exception.message
    ) : Result<T>() {
        override fun toString(): String = "Result.Error(message=$message, exception=${exception::class.simpleName})"
    }

    /**
     * Returns `true` if this result is [Success], `false` otherwise.
     *
     * Example:
     * ```kotlin
     * if (result.isSuccess()) {
     *     println("Operation succeeded!")
     * }
     * ```
     */
    fun isSuccess(): Boolean = this is Success

    /**
     * Returns `true` if this result is [Error], `false` otherwise.
     *
     * Example:
     * ```kotlin
     * if (result.isError()) {
     *     logger.error("Operation failed")
     * }
     * ```
     */
    fun isError(): Boolean = this is Error

    /**
     * Returns `true` if this result is [Loading], `false` otherwise.
     *
     * Example:
     * ```kotlin
     * if (result.isLoading()) {
     *     showLoadingSpinner()
     * }
     * ```
     */
    fun isLoading(): Boolean = this is Loading

    /**
     * Returns the data if this is [Success], or `null` otherwise.
     *
     * Example:
     * ```kotlin
     * val user: User? = result.getOrNull()
     * user?.let { println(it.name) }
     * ```
     *
     * @return The success data or null
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    /**
     * Returns the data if this is [Success], or throws the exception if [Error].
     *
     * Example:
     * ```kotlin
     * try {
     *     val user: User = result.getOrThrow()
     *     println(user.name)
     * } catch (e: Exception) {
     *     println("Failed: ${e.message}")
     * }
     * ```
     *
     * @return The success data
     * @throws Throwable if this is [Error]
     * @throws IllegalStateException if this is [Loading]
     */
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
        is Loading -> throw IllegalStateException("Cannot get data from Loading state")
    }

    /**
     * Executes the given [action] if this is [Success], and returns this result unchanged.
     *
     * This is useful for side effects like logging or analytics.
     *
     * Example:
     * ```kotlin
     * result
     *     .onSuccess { user ->
     *         analytics.track("UserLoaded", user.id)
     *     }
     *     .onError { error ->
     *         logger.error("Load failed", error)
     *     }
     * ```
     *
     * @param action The action to execute with the success data
     * @return This result unchanged (for chaining)
     */
    fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) {
            action(data)
        }
        return this
    }

    /**
     * Executes the given [action] if this is [Error], and returns this result unchanged.
     *
     * This is useful for side effects like logging or error reporting.
     *
     * Example:
     * ```kotlin
     * result.onError { error ->
     *     crashReporter.report(error)
     *     logger.error("Operation failed", error)
     * }
     * ```
     *
     * @param action The action to execute with the error exception
     * @return This result unchanged (for chaining)
     */
    fun onError(action: (Throwable) -> Unit): Result<T> {
        if (this is Error) {
            action(exception)
        }
        return this
    }

    /**
     * Transforms the data if this is [Success], returning a new [Result] with the transformed data.
     *
     * Example:
     * ```kotlin
     * val userResult: Result<User> = getUser()
     * val nameResult: Result<String> = userResult.map { user -> user.name }
     * ```
     *
     * @param transform The transformation function
     * @return A new Result with transformed data, or the same Error/Loading state
     */
    fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(exception, message)
        is Loading -> Loading()
    }

    /**
     * Returns the data if this is [Success], or the result of [defaultValue] otherwise.
     *
     * Example:
     * ```kotlin
     * val user: User = result.getOrElse { User.GUEST }
     * ```
     *
     * @param defaultValue A function providing the default value
     * @return The success data or default value
     */
    fun getOrElse(defaultValue: () -> @UnsafeVariance T): @UnsafeVariance T = when (this) {
        is Success -> data
        else -> defaultValue()
    }
}
