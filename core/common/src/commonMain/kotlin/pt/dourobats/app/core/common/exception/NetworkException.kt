package pt.dourobats.app.core.common.exception

class NetworkException(
    message: String = "Network error. Please check your connection.",
    cause: Throwable? = null,
) : Exception(message, cause)
