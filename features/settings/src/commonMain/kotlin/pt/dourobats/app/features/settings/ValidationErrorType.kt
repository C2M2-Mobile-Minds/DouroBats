package pt.dourobats.app.features.settings

sealed class DisplayNameError {
    object Blank : DisplayNameError()
    object TooShort : DisplayNameError()
}

sealed class EmailError {
    object Blank : EmailError()
    object InvalidFormat : EmailError()
}

sealed class PhoneError {
    object Blank : PhoneError()
    object InvalidFormat : PhoneError()
}
