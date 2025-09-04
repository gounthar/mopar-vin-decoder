package com.moparvindecoder.domain.repository

import com.moparvindecoder.domain.model.VinInfo
import com.moparvindecoder.utils.Result

interface VinRepository {
    suspend fun decodeVin(vin: String): Result<VinInfo>
    // Future: add history persistence, scanner assistance, etc.
}
