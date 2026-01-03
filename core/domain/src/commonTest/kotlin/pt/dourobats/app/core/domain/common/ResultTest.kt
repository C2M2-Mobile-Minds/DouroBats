package pt.dourobats.app.core.domain.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ResultTest {

    // Test data
    private val testData = "Test Data"
    private val testException = Exception("Test error")

    // Sample data class for testing
    data class User(val id: String, val name: String)

    @Test
    fun `isSuccess returns true when Success state`() {
        // Given
        val result = Result.Success(testData)

        // When/Then
        assertTrue(result.isSuccess())
        assertFalse(result.isError())
        assertFalse(result.isLoading())
    }

    @Test
    fun `isError returns true when Error state`() {
        // Given
        val result: Result<String> = Result.Error(testException)

        // When/Then
        assertTrue(result.isError())
        assertFalse(result.isSuccess())
        assertFalse(result.isLoading())
    }

    @Test
    fun `isLoading returns true when Loading state`() {
        // Given
        val result: Result<String> = Result.Loading()

        // When/Then
        assertTrue(result.isLoading())
        assertFalse(result.isSuccess())
        assertFalse(result.isError())
    }

    @Test
    fun `getOrNull returns data when Success state`() {
        // Given
        val result = Result.Success(testData)

        // When
        val data = result.getOrNull()

        // Then
        assertEquals(testData, data)
    }

    @Test
    fun `getOrNull returns null when Error state`() {
        // Given
        val result: Result<String> = Result.Error(testException)

        // When
        val data = result.getOrNull()

        // Then
        assertNull(data)
    }

    @Test
    fun `getOrNull returns null when Loading state`() {
        // Given
        val result: Result<String> = Result.Loading()

        // When
        val data = result.getOrNull()

        // Then
        assertNull(data)
    }

    @Test
    fun `getOrThrow returns data when Success state`() {
        // Given
        val result = Result.Success(testData)

        // When
        val data = result.getOrThrow()

        // Then
        assertEquals(testData, data)
    }

    @Test
    fun `getOrThrow throws exception when Error state`() {
        // Given
        val result: Result<String> = Result.Error(testException)

        // When/Then
        val exception = assertFailsWith<Exception> {
            result.getOrThrow()
        }
        assertEquals("Test error", exception.message)
    }

    @Test
    fun `getOrThrow throws IllegalStateException when Loading state`() {
        // Given
        val result: Result<String> = Result.Loading()

        // When/Then
        assertFailsWith<IllegalStateException> {
            result.getOrThrow()
        }
    }

    @Test
    fun `onSuccess executes action when Success state`() {
        // Given
        val result = Result.Success(testData)
        var actionExecuted = false
        var capturedData: String? = null

        // When
        result.onSuccess { data ->
            actionExecuted = true
            capturedData = data
        }

        // Then
        assertTrue(actionExecuted)
        assertEquals(testData, capturedData)
    }

    @Test
    fun `onSuccess does not execute action when Error state`() {
        // Given
        val result: Result<String> = Result.Error(testException)
        var actionExecuted = false

        // When
        result.onSuccess {
            actionExecuted = true
        }

        // Then
        assertFalse(actionExecuted)
    }

    @Test
    fun `onSuccess does not execute action when Loading state`() {
        // Given
        val result: Result<String> = Result.Loading()
        var actionExecuted = false

        // When
        result.onSuccess {
            actionExecuted = true
        }

        // Then
        assertFalse(actionExecuted)
    }

    @Test
    fun `onError executes action when Error state`() {
        // Given
        val result: Result<String> = Result.Error(testException, "Custom message")
        var actionExecuted = false
        var capturedException: Throwable? = null

        // When
        result.onError { error ->
            actionExecuted = true
            capturedException = error
        }

        // Then
        assertTrue(actionExecuted)
        assertEquals(testException, capturedException)
    }

    @Test
    fun `onError does not execute action when Success state`() {
        // Given
        val result = Result.Success(testData)
        var actionExecuted = false

        // When
        result.onError {
            actionExecuted = true
        }

        // Then
        assertFalse(actionExecuted)
    }

    @Test
    fun `onError does not execute action when Loading state`() {
        // Given
        val result: Result<String> = Result.Loading()
        var actionExecuted = false

        // When
        result.onError {
            actionExecuted = true
        }

        // Then
        assertFalse(actionExecuted)
    }

    @Test
    fun `onSuccess and onError can be chained when Success`() {
        // Given
        val result = Result.Success(testData)
        var successCalled = false
        var errorCalled = false

        // When
        result
            .onSuccess { successCalled = true }
            .onError { errorCalled = true }

        // Then
        assertTrue(successCalled)
        assertFalse(errorCalled)
    }

    @Test
    fun `onSuccess and onError can be chained when Error`() {
        // Given
        val result: Result<String> = Result.Error(testException)
        var successCalled = false
        var errorCalled = false

        // When
        result
            .onSuccess { successCalled = true }
            .onError { errorCalled = true }

        // Then
        assertFalse(successCalled)
        assertTrue(errorCalled)
    }

    @Test
    fun `Error stores custom message when provided`() {
        // Given
        val customMessage = "Custom error message"
        val result: Result<String> = Result.Error(testException, customMessage)

        // When/Then
        assertTrue(result is Result.Error)
        assertEquals(customMessage, (result as Result.Error).message)
        assertEquals(testException, result.exception)
    }

    @Test
    fun `Error uses exception message when custom message not provided`() {
        // Given
        val result: Result<String> = Result.Error(testException)

        // When/Then
        assertTrue(result is Result.Error)
        assertEquals("Test error", (result as Result.Error).message)
    }

    @Test
    fun `map transforms Success data correctly`() {
        // Given
        val user = User("123", "John Doe")
        val result = Result.Success(user)

        // When
        val mappedResult = result.map { it.name }

        // Then
        assertTrue(mappedResult is Result.Success)
        assertEquals("John Doe", (mappedResult as Result.Success).data)
    }

    @Test
    fun `map preserves Error state when Error`() {
        // Given
        val result: Result<User> = Result.Error(testException, "Load failed")

        // When
        val mappedResult = result.map { it.name }

        // Then
        assertTrue(mappedResult is Result.Error)
        assertEquals("Load failed", (mappedResult as Result.Error).message)
        assertEquals(testException, mappedResult.exception)
    }

    @Test
    fun `map preserves Loading state when Loading`() {
        // Given
        val result: Result<User> = Result.Loading()

        // When
        val mappedResult = result.map { it.name }

        // Then
        assertTrue(mappedResult is Result.Loading)
    }

    @Test
    fun `getOrElse returns data when Success state`() {
        // Given
        val result = Result.Success(testData)

        // When
        val data = result.getOrElse { "Default" }

        // Then
        assertEquals(testData, data)
    }

    @Test
    fun `getOrElse returns default when Error state`() {
        // Given
        val result: Result<String> = Result.Error(testException)
        val defaultValue = "Default"

        // When
        val data = result.getOrElse { defaultValue }

        // Then
        assertEquals(defaultValue, data)
    }

    @Test
    fun `getOrElse returns default when Loading state`() {
        // Given
        val result: Result<String> = Result.Loading()
        val defaultValue = "Default"

        // When
        val data = result.getOrElse { defaultValue }

        // Then
        assertEquals(defaultValue, data)
    }

    @Test
    fun `Success equals another Success with same data`() {
        // Given
        val result1 = Result.Success(testData)
        val result2 = Result.Success(testData)

        // When/Then
        assertEquals(result1, result2)
    }

    @Test
    fun `Error equals another Error with same exception and message`() {
        // Given
        val result1: Result<String> = Result.Error(testException, "Message")
        val result2: Result<String> = Result.Error(testException, "Message")

        // When/Then
        assertEquals(result1, result2)
    }

    @Test
    fun `Loading equals another Loading instance`() {
        // Given
        val result1: Result<String> = Result.Loading()
        val result2: Result<String> = Result.Loading()

        // When/Then
        assertEquals(result1, result2)
    }

    @Test
    fun `Success toString shows data`() {
        // Given
        val result = Result.Success(testData)

        // When
        val string = result.toString()

        // Then
        assertTrue(string.contains("Success"))
        assertTrue(string.contains(testData))
    }

    @Test
    fun `Error toString shows message and exception type`() {
        // Given
        val result: Result<String> = Result.Error(testException, "Failed")

        // When
        val string = result.toString()

        // Then
        assertTrue(string.contains("Error"))
        assertTrue(string.contains("Failed"))
        assertTrue(string.contains("Exception"))
    }

    @Test
    fun `Loading toString shows state`() {
        // Given
        val result: Result<String> = Result.Loading()

        // When
        val string = result.toString()

        // Then
        assertTrue(string.contains("Loading"))
    }

    @Test
    fun `multiple map transformations work correctly when Success`() {
        // Given
        val user = User("123", "John Doe")
        val result = Result.Success(user)

        // When
        val finalResult = result
            .map { it.name }
            .map { it.uppercase() }
            .map { it.length }

        // Then
        assertTrue(finalResult is Result.Success)
        assertEquals(8, (finalResult as Result.Success).data) // "JOHN DOE".length
    }

    @Test
    fun `chaining onSuccess multiple times executes all actions when Success`() {
        // Given
        val result = Result.Success(testData)
        var counter = 0

        // When
        result
            .onSuccess { counter++ }
            .onSuccess { counter++ }
            .onSuccess { counter++ }

        // Then
        assertEquals(3, counter)
    }
}
