package com.moparvindecoder.domain.usecase

import com.moparvindecoder.domain.model.VinInfo
import com.moparvindecoder.domain.repository.VinRepository
import com.moparvindecoder.utils.Result

class DecodeVinUseCase(
    private val repository: VinRepository
) {
    suspend operator fun invoke(vin: String): Result<VinInfo> {
        return repository.decodeVin(vin)
    }
}
