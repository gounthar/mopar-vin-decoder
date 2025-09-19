# Mopar VIN Decoder

An Android application for decoding vintage Mopar (Chrysler, Dodge, Plymouth) Vehicle Identification Numbers from the 1960s-1970s era.

## Features

- **Camera Scanning**: Use your device's camera to scan VIN barcodes
- **Manual Entry**: Type VIN numbers directly with real-time validation
- **Historical Data**: View previously decoded VINs stored locally
- **Detailed Results**: Get comprehensive vehicle information including make, model, year, engine, and more

## Supported VIN Formats

- **10-character VINs** (1962-1965)
- **13-character VINs** (1966-1974)

## Requirements

- Android 5.0 (API level 21) or higher
- Camera permission for barcode scanning

## Development

### Setup

- **JDK**: Eclipse Adoptium JDK 21
- **Gradle**: 8.7 (uses wrapper)
- **Min SDK**: 21
- **Target SDK**: 34

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

### Architecture

Built using **Clean Architecture** with **MVVM** pattern:

- **Domain Layer**: VIN decoding logic and validation
- **Data Layer**: Room database with Repository pattern
- **UI Layer**: Fragment-based navigation with ViewModels

### Technologies

- **Kotlin** (100% Kotlin codebase)
- **Android Navigation Component** with SafeArgs
- **Room Database** with migration support
- **CameraX + ML Kit** for barcode scanning
- **View Binding**
- **Coroutines** for async operations

### Testing

Uses **JUnit 4 + Google Truth + MockK** with AAA pattern (Arrange-Act-Assert).

## Code Style

This project follows the coding standards defined in `.clinerules/` including conventional commits and Kotlin formatting guidelines.