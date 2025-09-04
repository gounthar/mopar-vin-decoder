package com.moparvindecoder.domain.decoder

/**
 * Validation and normalization for classic Mopar VINs.
 *
 * Supported formats:
 * - 10 characters (approx. 1962–1965)
 * - 13 characters (1966–1974)
 *
 * Rules:
 * - Length must be 10 or 13
 * - Uppercase A–Z and 0–9 only (no I, O, Q)
 * - First character must be one of: B, J, L, R, V, W, X
 * - Year code restrictions by format:
 *      - 13-char: [6,7,8,9,0,1,2,3,4] => 1966..1974
 *      - 10-char: [2,3,4,5]           => 1962..1965
 */
object VinValidator {

    fun normalize(vin: String): String {
        return vin.trim().uppercase()
    }

    /**
     * Validates and returns the normalized VIN or throws IllegalArgumentException.
     */
    fun validateOrThrow(vin: String): String {
        val normalized = normalize(vin)

        if (normalized.length != 10 && normalized.length != 13) {
            throw IllegalArgumentException("VIN length must be 10 or 13 characters for supported classic formats (1962–1974).")
        }

        if (normalized.any { it == 'I' || it == 'O' || it == 'Q' }) {
            throw IllegalArgumentException("VIN cannot contain I, O, or Q.")
        }

        if (!normalized.all { it.isLetterOrDigit() }) {
            throw IllegalArgumentException("VIN must contain only letters and digits.")
        }

        val first = normalized.first()
        val validFirst = setOf('B', 'J', 'L', 'R', 'V', 'W', 'X')
        if (first !in validFirst) {
            throw IllegalArgumentException("Invalid first character '$first'. Expected one of ${validFirst.joinToString(",")}.")
        }

        // Year code sanity checks by format
        if (normalized.length == 13) {
            val yearChar = normalized[5]
            val validYearChars = setOf('6', '7', '8', '9', '0', '1', '2', '3', '4')
            if (yearChar !in validYearChars) {
                throw IllegalArgumentException("Unsupported model year code '$yearChar' for 13-character VIN.")
            }
        } else { // length == 10
            val yearChar = normalized[4] // 5th character in the 10-char format
            val validYearChars = setOf('2', '3', '4', '5')
            if (yearChar !in validYearChars) {
                throw IllegalArgumentException("Unsupported model year code '$yearChar' for 10-character VIN.")
            }
        }

        return normalized
    }
}
