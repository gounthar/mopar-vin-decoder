package com.moparvindecoder.domain.decoder

import com.moparvindecoder.domain.model.DecodedVin

/**
 * Mopar VIN decoder supporting:
 *  - 10-character VINs (circa 1962–1965)
 *  - 13-character VINs (1966–1974)
 *
 * Routing is based on VIN length. Uses code maps for year, engine, body, and plant.
 *
 * Notes:
 *  - Real-world Mopar codes vary by division and year. This decoder focuses on common cases
 *    and provides sensible defaults when a code is unknown.
 *  - Validation is handled by VinValidator. Callers should use validateOrThrow(...) first.
 */
class MoparVinDecoder {

    fun decode(rawVin: String): DecodedVin {
        val vin = VinValidator.validateOrThrow(rawVin)
        return when (vin.length) {
            13 -> parse13(vin)
            10 -> parse10(vin)
            else -> error("Unsupported VIN length after validation: ${vin.length}")
        }
    }

    // region 13-char (1966–1974): C P BB E Y P SSSSSS
    // Example: JS23R0B100001
    // 0: Car Line (J)
    // 1: Price class (S)
    // 2-3: Body style (23)
    // 4: Engine code (R)
    // 5: Year (0 -> 1970, etc.)
    // 6: Assembly Plant (B)
    // 7-12: Production sequence (100001)
    private fun parse13(vin: String): DecodedVin {
        val carLineCode = vin[0]
        val priceClassCode = vin[1]
        val bodyCode = "${vin[2]}${vin[3]}"
        val engineCode = vin[4]
        val yearCode = vin[5]
        val plantCode = vin[6]
        val seq = vin.substring(7)

        val (make, model, carLine) = carLineInfo13[carLineCode] ?: Triple("Mopar", null, null)
        val priceClass = priceClassMap[priceClassCode]
        val bodyStyle = bodyStyleMap13[bodyCode]
        val engineInfo = engineMap66to74[engineCode]
        val year = yearMap66to74[yearCode]
        val plant = plantMap[plantCode]

        return DecodedVin(
            make = make ?: "Mopar",
            model = model,
            carLine = carLine,
            priceClass = priceClass,
            bodyStyle = bodyStyle,
            engine = engineInfo?.name,
            displacement = engineInfo?.dispCi,
            horsepower = engineInfo?.hp,
            year = year,
            assemblyPlant = plant,
            productionSeq = seq
        )
    }
    // endregion

    // region 10-char (~1962–1965): C P B E Y P SSSS (approximation)
    // Assumed layout for early 60s:
    // 0: Car Line
    // 1: Price class
    // 2: Body (single-digit for early formats)
    // 3: Engine
    // 4: Year ('2'..'5' => 1962..1965)
    // 5: Plant
    // 6-9: Sequence (4 digits)
    private fun parse10(vin: String): DecodedVin {
        val carLineCode = vin[0]
        val priceClassCode = vin[1]
        val bodyCode = vin[2]
        val engineCode = vin[3]
        val yearCode = vin[4]
        val plantCode = vin[5]
        val seq = vin.substring(6)

        val (make, model, carLine) = carLineInfoEarly[carLineCode] ?: Triple("Mopar", null, null)
        val priceClass = priceClassMap[priceClassCode]
        val bodyStyle = bodyStyleMapEarly[bodyCode]
        val engineInfo = engineMapEarly[engineCode] ?: engineMap66to74[engineCode]
        val year = yearMap62to65[yearCode]
        val plant = plantMap[plantCode]

        return DecodedVin(
            make = make ?: "Mopar",
            model = model,
            carLine = carLine,
            priceClass = priceClass,
            bodyStyle = bodyStyle,
            engine = engineInfo?.name,
            displacement = engineInfo?.dispCi,
            horsepower = engineInfo?.hp,
            year = year,
            assemblyPlant = plant,
            productionSeq = seq
        )
    }
    // endregion

    // region Mappings

    // 1966–1974
    private val yearMap66to74 = mapOf(
        '6' to 1966, '7' to 1967, '8' to 1968, '9' to 1969,
        '0' to 1970, '1' to 1971, '2' to 1972, '3' to 1973, '4' to 1974
    )

    // 1962–1965
    private val yearMap62to65 = mapOf(
        '2' to 1962, '3' to 1963, '4' to 1964, '5' to 1965
    )

    // Car line to (make, model, carLine descriptor) — 13-char era emphasis
    private val carLineInfo13: Map<Char, Triple<String?, String?, String?>> = mapOf(
        'J' to Triple("Dodge", "Challenger", "Challenger"),
        'B' to Triple("Plymouth", "Barracuda", "Barracuda"),
        'X' to Triple("Dodge", "Charger", "Charger"),
        'W' to Triple("Dodge", "Coronet", "Coronet"),
        'R' to Triple("Plymouth", "Belvedere/Satellite", "Belvedere/Satellite"),
        'L' to Triple("Dodge", "Dart", "Dart"),
        'V' to Triple("Plymouth", "Valiant", "Valiant")
    )

    // Early 60s car line — fallback to similar divisions
    private val carLineInfoEarly: Map<Char, Triple<String?, String?, String?>> = mapOf(
        'B' to Triple("Plymouth", "B-series", "B-series"),
        'J' to Triple("Dodge", "J-series", "J-series"),
        'L' to Triple("Dodge", "Dart", "Dart"),
        'R' to Triple("Plymouth", "Belvedere", "Belvedere"),
        'V' to Triple("Plymouth", "Valiant", "Valiant"),
        'W' to Triple("Dodge", "Polara/Coronet", "Polara/Coronet"),
        'X' to Triple("Dodge", "Charger", "Charger")
    )

    // Price/trim class (common Mopar)
    private val priceClassMap = mapOf(
        'L' to "Low",
        'M' to "Medium",
        'H' to "High",
        'P' to "Premium",
        'S' to "Special",
        'X' to "Special",
        'D' to "Deluxe",
        'C' to "Custom"
    )

    // Body styles (13-char, two-digit)
    private val bodyStyleMap13 = mapOf(
        "21" to "2-door sedan",
        "23" to "2-door hardtop",
        "27" to "Convertible",
        "29" to "2-door sports hardtop",
        "41" to "4-door sedan",
        "43" to "4-door hardtop",
        "45" to "Wagon (6-passenger)",
        "46" to "Wagon (9-passenger)"
    )

    // Early body styles (single digit approximation)
    private val bodyStyleMapEarly = mapOf(
        '1' to "2-door sedan",
        '3' to "2-door hardtop",
        '7' to "Convertible",
        '9' to "2-door sports hardtop",
        '4' to "4-door sedan",
        '5' to "Wagon (6-passenger)",
        '6' to "Wagon (9-passenger)"
    )

    // Engine maps
    private data class EngineInfo(val name: String, val dispCi: Int?, val hp: Int?)

    // 1966–1974 common engines
    private val engineMap66to74 = mapOf(
        'R' to EngineInfo("426 HEMI", 426, 425),
        'V' to EngineInfo("440 6-bbl (Six Pack/Six Barrel)", 440, 390),
        'U' to EngineInfo("440 4-bbl", 440, 375),
        'N' to EngineInfo("383 4-bbl", 383, 335),
        'H' to EngineInfo("340 4-bbl", 340, 275),
        'G' to EngineInfo("318 2-bbl", 318, 230),
        'L' to EngineInfo("225 Slant-6", 225, 145)
    )

    // Early 60s engines (approx)
    private val engineMapEarly = mapOf(
        'A' to EngineInfo("170 Slant-6", 170, 101),
        'B' to EngineInfo("225 Slant-6", 225, 145),
        'D' to EngineInfo("273 V8", 273, 180),
        'G' to EngineInfo("318 V8", 318, 230),
        'H' to EngineInfo("361/383 V8", 361, null)
    )

    // Plants (commonly used Mopar codes; not exhaustive)
    private val plantMap = mapOf(
        'A' to "Lynch Road, MI",
        'B' to "Hamtramck, MI",
        'C' to "Jefferson, MI",
        'D' to "Belvidere, IL",
        'E' to "Los Angeles, CA",
        'F' to "Newark, DE",
        'G' to "St. Louis, MO",
        'H' to "Detroit, MI",
        'N' to "Windsor, ON"
    )

    // endregion
}
