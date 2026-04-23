package pt.dourobats.app.features.settings.ui

internal sealed class DisplayNameError {
    object Blank : DisplayNameError()
    object TooShort : DisplayNameError()
}

internal sealed class EmailError {
    object Blank : EmailError()
    object InvalidFormat : EmailError()
}

internal sealed class PhoneError {
    object Blank : PhoneError()
    object InvalidFormat : PhoneError()
}
