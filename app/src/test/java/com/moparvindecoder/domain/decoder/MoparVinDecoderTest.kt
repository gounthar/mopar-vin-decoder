package com.moparvindecoder.domain.decoder

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MoparVinDecoderTest {

    private val decoder = MoparVinDecoder()

    // Arrange – Act – Assert tests

    @Test
    fun `decode 13-digit 1970 Challenger HEMI returns expected fields`() {
        // Arrange
        val vin = "JS23R0B100001" // J=Challenger, S=Special, 23=2-door hardtop, R=426 HEMI, 0=1970, B=Hamtramck

        // Act
        val result = decoder.decode(vin)

        // Assert
        assertThat(result.make).isEqualTo("Dodge")
        assertThat(result.model).isEqualTo("Challenger")
        assertThat(result.carLine).isEqualTo("Challenger")
        assertThat(result.priceClass).isEqualTo("Special")
        assertThat(result.bodyStyle).isEqualTo("2-door hardtop")
        assertThat(result.engine).isEqualTo("426 HEMI")
        assertThat(result.displacement).isEqualTo(426)
        assertThat(result.horsepower).isEqualTo(425)
        assertThat(result.year).isEqualTo(1970)
        assertThat(result.assemblyPlant).isEqualTo("Hamtramck, MI")
        assertThat(result.productionSeq).isEqualTo("100001")
    }

    @Test
    fun `decode 10-digit early-60s format returns expected fields`() {
        // Arrange
        val vin = "LM3D4A1234"
        // L=Dodge Dart (early map), M=Medium, 3=2-door hardtop (approx early), D=273 V8, 4=1964, A=Lynch Road

        // Act
        val result = decoder.decode(vin)

        // Assert
        assertThat(result.make).isEqualTo("Dodge")
        assertThat(result.model).isEqualTo("Dart")
        assertThat(result.carLine).isEqualTo("Dart")
        assertThat(result.priceClass).isEqualTo("Medium")
        assertThat(result.bodyStyle).isEqualTo("2-door hardtop")
        assertThat(result.engine).isEqualTo("273 V8")
        assertThat(result.displacement).isEqualTo(273)
        assertThat(result.horsepower).isEqualTo(180)
        assertThat(result.year).isEqualTo(1964)
        assertThat(result.assemblyPlant).isEqualTo("Lynch Road, MI")
        assertThat(result.productionSeq).isEqualTo("1234")
    }

    // Validator rules

    @Test(expected = IllegalArgumentException::class)
    fun `validate rejects invalid length`() {
        // Arrange
        val vin = "JS23R0B10000" // 12 chars (invalid)

        // Act
        VinValidator.validateOrThrow(vin)

        // Assert via expected exception
    }

    @Test(expected = IllegalArgumentException::class)
    fun `validate rejects banned characters`() {
        // Arrange
        val vin = "LM3D4AI234" // contains 'I'

        // Act
        VinValidator.validateOrThrow(vin)

        // Assert via expected exception
    }

    @Test(expected = IllegalArgumentException::class)
    fun `validate rejects invalid first character`() {
        // Arrange
        val vin = "ZM3D4A1234" // 'Z' not allowed

        // Act
        VinValidator.validateOrThrow(vin)

        // Assert via expected exception
    }

    @Test(expected = IllegalArgumentException::class)
    fun `validate rejects 10-digit year outside 1962-1965`() {
        // Arrange
        val vin = "LM3D6A1234" // '6' is not a valid year code for 10-char format

        // Act
        VinValidator.validateOrThrow(vin)

        // Assert via expected exception
    }
}
