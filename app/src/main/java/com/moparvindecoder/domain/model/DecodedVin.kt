package com.moparvindecoder.domain.model

data class DecodedVin(
    val make: String = "Mopar",
    val model: String? = null,
    val carLine: String? = null,
    val priceClass: String? = null,
    val bodyStyle: String? = null,
    val engine: String? = null,
    val displacement: Int? = null,
    val horsepower: Int? = null,
    val year: Int? = null,
    val assemblyPlant: String? = null,
    val productionSeq: String? = null
)
