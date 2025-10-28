package com.moparvindecoder.data.repository

import com.moparvindecoder.domain.decoder.MoparVinDecoder
import com.moparvindecoder.domain.decoder.VinValidator
import com.moparvindecoder.domain.model.VinInfo
import com.moparvindecoder.domain.repository.VinRepository
import com.moparvindecoder.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VinRepositoryImpl : VinRepository {
    private val decoder = MoparVinDecoder()

    override suspend fun decodeVin(vin: String): Result<VinInfo> = withContext(Dispatchers.Default) {
        return@withContext try {
            val decoded = decoder.decode(vin)
            Result.Success(
                VinInfo(
                    vin = VinValidator.normalize(vin),
                    year = decoded.year?.toString(),
                    make = decoded.make,
                    model = decoded.model,
                    plant = decoded.assemblyPlant,
                    engine = decoded.engine,
                    productionSeq = decoded.productionSeq
                )
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
