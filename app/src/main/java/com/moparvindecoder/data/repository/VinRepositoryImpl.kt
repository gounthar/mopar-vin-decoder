package com.moparvindecoder.data.repository

import com.moparvindecoder.domain.model.VinInfo
import com.moparvindecoder.domain.repository.VinRepository
import com.moparvindecoder.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class VinRepositoryImpl : VinRepository {

    override suspend fun decodeVin(vin: String): Result<VinInfo> = withContext(Dispatchers.Default) {
        // Stubbed implementation for scaffolding purposes.
        // Simulate a little work and return a placeholder result.
        return@withContext try {
            delay(100) // simulate I/O/CPU work
            val trimmed = vin.trim().uppercase()
            if (trimmed.isEmpty()) {
                Result.Error(IllegalArgumentException("VIN cannot be empty"))
            } else {
                // Very naive placeholders
                val year = when (trimmed.getOrNull(9)) {
                    'A' -> "2010"
                    'B' -> "2011"
                    'C' -> "2012"
                    'D' -> "2013"
                    'E' -> "2014"
                    'F' -> "2015"
                    else -> null
                }
                Result.Success(
                    VinInfo(
                        vin = trimmed,
                        year = year,
                        make = "Mopar",
                        model = null,
                        plant = null,
                        engine = null
                    )
                )
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
