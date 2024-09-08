# Biometric Authentication App

This is a sample Android app implementing biometric authentication using **Jetpack Biometric** library. The app demonstrates how to use the **BiometricPrompt** to handle fingerprint, face recognition, and device credential (PIN, pattern, or password) authentication.

## Features
- Biometric authentication support for fingerprint and face recognition.
- Fallback to device credentials (PIN, pattern, or password) if biometric is unavailable or not enrolled.
- Simple and user-friendly UI built with **Jetpack Compose**.
- Automatic redirect to settings to enroll biometrics if not set up.

## Requirements
- **Biometric Support:** Fingerprint or Face recognition hardware
- **Libraries:**
  - [Jetpack Biometric](https://developer.android.com/jetpack/androidx/releases/biometric)
  - [Jetpack Compose](https://developer.android.com/jetpack/compose)

## How to Use
1. Clone the repository.
   ```bash
   git clone https://github.com/Bhavyansh03-tech/Biometric_Auth.git
   ```
2. Open the project in Android Studio.
3. Build and run the project on a physical device (biometric authentication is not supported on emulators).

## Sample Code
Here's a part of the code that manages the biometric prompt:

```kotlin
class BiometricPromptManager(
    private val activity: AppCompatActivity
) {
    private val resultChannel = Channel<BiometricResult>()
    val result = resultChannel.receiveAsFlow()

    fun showBiometricPrompt(
        title: String,
        description: String
    ) {
        val manager = BiometricManager.from(activity)
        val authenticators = if (Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else BIOMETRIC_STRONG

        val promptInfo = PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setAllowedAuthenticators(authenticators)
        
        if (Build.VERSION.SDK_INT < 30) {
            promptInfo.setNegativeButtonText("Cancel")
        }

        when(manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                resultChannel.trySend(BiometricResult.HardwareUnavailable)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                resultChannel.trySend(BiometricResult.FeatureUnavailable)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                resultChannel.trySend(BiometricResult.AuthenticationNotSet)
                return
            }
            else -> Unit
        }

        val prompt = BiometricPrompt(
            activity,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    resultChannel.trySend(BiometricResult.AuthenticationError(errorCode.toString()))
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    resultChannel.trySend(BiometricResult.AuthenticationSucceeded)
                }

                override fun onAuthenticationFailed() {
                    resultChannel.trySend(BiometricResult.AuthenticationFailed)
                }
            }
        )
        prompt.authenticate(promptInfo.build())
    }
}
```

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any improvements or bug fixes.

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -am 'Add some feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Create a new Pull Request.

## Contact

For questions or feedback, please contact [@Bhavyansh03-tech](https://github.com/Bhavyansh03-tech) on GitHub or connect with me on [LinkedIn](https://www.linkedin.com/in/bhavyansh03/).

---
