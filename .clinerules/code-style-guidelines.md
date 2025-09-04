# Code Style Guidelines

**Project**: Mopar VIN Decoder (Android)  
**Language**: Kotlin  
**Architecture**: MVVM + Clean Architecture Principles

---

## 1. Formatting and Structure

- **Indentation**: Use 4 spaces (never tabs).
- **Line length**: Soft limit of 120 characters.
- **Braces**: Use K&R style (Egyptian brackets).
  ```kotlin
  // Good
  fun decodeVin(vin: String): DecodedVin {
      // code
  }

  // Bad
  fun decodeVin(vin: String): DecodedVin
  {
      // code
  }
Imports: No .* wildcards. Use Android Studio’s optimizer.

## 2. Naming Conventions
   Classes/Objects: PascalCase → MoparVinDecoder, VinHistoryFragment

Functions/Variables: camelCase → decodeVin(), engineCode

Constants: UPPER_SNAKE_CASE → const val DATABASE_NAME = "vin_database"

Resource files (XML): snake_case → activity_main.xml, item_vin_history.xml

IDs: Prefix with component type → @+id/vin_input_edittext, @+id/decode_button

Packages: lowercase, singular → com.moparvindecoder.data.repository

## 3. Kotlin Best Practices
   Prefer val over var for immutability.

Use nullable types (String?) and safe calls (?.) instead of !!.

Keep functions small and focused. Prefer default arguments over overloads.

Coroutines: Use viewModelScope. Handle exceptions with try/catch or CoroutineExceptionHandler.

## 4. Android Specifics
   Use @VisibleForTesting and @SuppressLint only with justification.

Use Hilt or FragmentFactory for dependency injection.

Always observe LiveData/Flow in lifecycle-safe scopes (repeatOnLifecycle).
