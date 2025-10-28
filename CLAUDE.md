# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MoparVinDecoder is an Android application that decodes vintage Mopar (Chrysler, Dodge, Plymouth) Vehicle Identification Numbers from the 1960s-1970s era using camera-based barcode scanning and manual input.

## Development Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.moparvindecoder.domain.decoder.MoparVinDecoderTest"

# Run specific test method
./gradlew test --tests "com.moparvindecoder.domain.decoder.MoparVinDecoderTest.decode 1970 Challenger R-T vin returns correct model"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Clean build
./gradlew clean

# Generate Room database schema files
./gradlew kaptDebugKotlin
```

## Architecture

**Clean Architecture with MVVM Pattern**:
- **Domain Layer** (`domain/`): Core business logic
  - `decoder/`: `MoparVinDecoder` and `VinValidator` - VIN parsing and validation logic
  - `model/`: `DecodedVin`, `VinInfo` - Domain models
  - `repository/`: `VinRepository` interface
  - `usecase/`: `DecodeVinUseCase` - Business logic orchestration
- **Data Layer** (`data/`): Data access and persistence
  - `local/`: Room database (`AppDatabase`, `VinHistoryDao`, `VinHistoryEntity`, `AppDatabaseMigrations`)
  - `repository/`: `VinHistoryRepository`, `VinRepositoryImpl` - Repository implementations
- **UI Layer** (`ui/`): Fragment-based UI with ViewModels
  - `input/`: Manual VIN entry
  - `scanner/`: Camera-based barcode scanning
  - `results/`: Decoded VIN display
  - `history/`: Previously decoded VINs list

**Key Technologies**: 100% Kotlin, Navigation Component with SafeArgs, Room with migrations, CameraX + ML Kit for scanning, Coroutines for async operations.

**Navigation Flow**: Defined in `app/src/main/res/navigation/nav_graph.xml`
- `VinInputFragment` (start) → `VinResultsFragment` or `VinScannerFragment`
- `VinHistoryFragment` → `VinResultsFragment`
- SafeArgs generates type-safe navigation arguments

## Testing

**Framework**: JUnit 4 + Google Truth + MockK
- Unit tests in `app/src/test/`
- Instrumented tests in `app/src/androidTest/`
- Follow AAA pattern (Arrange-Act-Assert)
- Use MockK for mocking

## VIN Decoding Specifics

**Supported Formats**:
- 10-character VINs (1962-1965)
- 13-character VINs (1966-1974)

**Core Logic**:
- `MoparVinDecoder.kt`: Main decoder with format routing (parse13/parse10 methods)
- `VinValidator.kt`: Input validation with `validateOrThrow()` method
- Decoding maps: Year codes, engine codes, body styles, assembly plants
- Error handling: Clear error messages for unsupported formats or invalid codes

## Database

**Room Database**:
- Database name: `mopar_vin_db`
- Singleton pattern with thread-safe initialization in `AppDatabase.kt`
- Schema versions exported to `app/schemas/` (version 1 currently)
- Migration support via `AppDatabaseMigrations.ALL`
- Testing: Schema files available in `androidTest` assets for migration tests
- VIN history storage for previously decoded VINs

## Build Configuration

**Requirements**:
- JDK 21 (Eclipse Adoptium) - Note: Project uses JVM target 17 for compatibility
- Gradle 8.7 (wrapper included)
- Min SDK 23 (Android 6.0)
- Compile SDK 36 (Android 16)
- Target SDK 35 (Android 15)
- KAPT for Room annotation processing

**Important Files**:
- `app/build.gradle` - Main app configuration with Room schema location
- `app/src/main/res/navigation/nav_graph.xml` - Navigation flow
- `.clinerules/` - Project coding standards and guidelines

**KAPT Configuration**:
- Room schemas: `$projectDir/schemas`
- Incremental processing enabled
- Projection expansion enabled for query optimization

## Code Style Guidelines

### Formatting and Structure
- **Indentation**: 4 spaces (never tabs)
- **Line length**: 120 characters soft limit
- **Braces**: K&R style (Egyptian brackets)
- **Imports**: No wildcards, use Android Studio optimizer

### Naming Conventions
- **Classes/Objects**: PascalCase → `MoparVinDecoder`, `VinHistoryFragment`
- **Functions/Variables**: camelCase → `decodeVin()`, `engineCode`
- **Constants**: UPPER_SNAKE_CASE → `const val DATABASE_NAME = "vin_database"`
- **Resource files**: snake_case → `activity_main.xml`, `item_vin_history.xml`
- **IDs**: Prefix with component type → `@+id/vin_input_edittext`, `@+id/decode_button`
- **Packages**: lowercase, singular → `com.moparvindecoder.data.repository`

### Kotlin Best Practices
- Prefer `val` over `var` for immutability
- Use nullable types (`String?`) and safe calls (`?.`) instead of `!!`
- Keep functions small and focused
- Use `viewModelScope` for coroutines with proper exception handling

### Android Specifics
- Use Hilt for dependency injection
- Observe LiveData/Flow in lifecycle-safe scopes (`repeatOnLifecycle`)
- Enable StrictMode in dev builds
- Use `RecyclerView.setHasFixedSize(true)` and DiffUtil

## Commit Conventions

Follow **Conventional Commits** specification:

```
<type>(<scope>): <short description>

[optional body]

[optional footer(s)]
```

**Types**: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
**Scopes**: `vin-decoder`, `scanner`, `history`, `database`, `ui`, `build`

**Examples**:
- `feat(vin-decoder): implement 1966-1974 VIN parsing logic`
- `fix(scanner): resolve crash on camera permission denied`
- `docs(readme): add build instructions for contributors`

## Testing Guidelines

**Framework**: JUnit 4 + Google Truth + MockK

### Test Structure (AAA Pattern)
```kotlin
@Test
fun `decode 1970 Challenger R-T vin returns correct model`() {
    // Arrange
    val testVin = "JS23R0B100001"
    every { mockEngineRepo.getEngineDetails('R', 1970) } returns EngineDetails("426 HEMI", 426)

    // Act
    val result = decoder.decode(testVin)

    // Assert
    assertThat(result.model).isEqualTo("Challenger R/T")
}
```

### Test Naming & Location
- **Class names**: `ClassNameTest` → `MoparVinDecoderTest`
- **Method names**: Descriptive with backticks
- **File location**: Mirror source structure in `app/src/test/` and `app/src/androidTest/`
- Test only public APIs, use MockK for dependencies

## Error Handling & Best Practices

### Robustness
- Fail gracefully with clear messages
- Use Result sealed classes instead of throwing exceptions:
```kotlin
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Exception) : Result<Nothing>
}
```
- Use Timber for logging

### Resource Management
- Respect lifecycle (remove observers, unregister listeners)
- Always close Cursor, Scanner, or Closeable objects
- Use ApplicationContext to avoid memory leaks

### Performance
- Use correct coroutines dispatcher:
  - `Dispatchers.IO` for I/O work
  - `Dispatchers.Default` for CPU tasks
- Use Coil or Glide for camera previews
- Follow SOLID principles and avoid "God classes"

## Development Workflow

### When Adding New VIN Formats
1. Update validation logic in `VinValidator.kt`
2. Add parsing method in `MoparVinDecoder.kt` (e.g., `parse17()` for modern 17-char VINs)
3. Add/update decoding maps for the new format
4. Create comprehensive unit tests in `MoparVinDecoderTest.kt`
5. Update UI to handle new format if needed

### When Modifying Database Schema
1. Increment version in `AppDatabase.kt`
2. Create migration in `AppDatabaseMigrations.kt`
3. Export new schema with `./gradlew kaptDebugKotlin`
4. Test migration with instrumented tests using schema files from `app/schemas/`
5. Update `AppDatabaseMigrations.ALL` array

### Working with Navigation
- Modify `nav_graph.xml` for navigation changes
- Rebuild project after nav changes to regenerate SafeArgs classes
- SafeArgs classes location: `app/build/generated/source/navigation-args/`
- Use type-safe navigation: `findNavController().navigate(FragmentDirections.actionToDestination())`

### Debugging Tips
- Use `BuildConfig.DEBUG` flag for debug-only code paths
- StrictMode should be enabled in debug builds
- Check Room database with Device File Explorer: `/data/data/com.moparvindecoder/databases/`
- For camera issues: Test on physical device (emulator camera support varies)