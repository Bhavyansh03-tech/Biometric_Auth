package com.example.biometricAuth

sealed interface BiometricResult {
    data object HardwareUnavailable : BiometricResult
    data object FeatureUnavailable : BiometricResult
    data object AuthenticationFailed : BiometricResult
    data object AuthenticationSucceeded : BiometricResult
    data object AuthenticationNotSet : BiometricResult
    data class AuthenticationError(val errorCode: String) : BiometricResult
}