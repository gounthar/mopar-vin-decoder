package com.moparvindecoder.domain.model

data class VinInfo(
    val vin: String,
    val year: String? = null,
    val make: String? = "Mopar",
    val model: String? = null,
    val plant: String? = null,
    val engine: String? = null,
    val productionSeq: String? = null
)
