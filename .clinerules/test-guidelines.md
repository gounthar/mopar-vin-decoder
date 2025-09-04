# Test Generation Guidelines

**Project**: Mopar VIN Decoder  
**Stack**: JUnit 4, Truth, MockK

---

## 1. Test Structure (AAA)

All tests follow **Arrange – Act – Assert**:

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

## 2. Naming & Location
- Class names: ClassNameTest → MoparVinDecoderTest
- Method names: Descriptive with backticks
- File location: Mirror source structure
  - app/src/main/.../MoparVinDecoder.kt
  - app/src/test/.../MoparVinDecoderTest.kt
- Instrumented tests: Place in app/src/androidTest/...

## 3. Methodologies & Tools
 - Test only public APIs, not private details.
 - Use MockK for external dependencies.
 - Use @ParameterizedTest for VIN variations.
 - For ViewModels: use InstantTaskExecutorRule + TestCoroutineDispatcher.