package pt.dourobats.app.core.common.exception

abstract class ValidationException(message: String) : Exception(message) {
    class RequiredField(fieldName: String) : ValidationException("$fieldName is required")
    class Generic(message: String) : ValidationException(message)
}
