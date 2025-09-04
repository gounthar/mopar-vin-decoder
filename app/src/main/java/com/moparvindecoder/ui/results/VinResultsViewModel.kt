package com.moparvindecoder.ui.results

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moparvindecoder.data.repository.VinRepositoryImpl
import com.moparvindecoder.domain.model.VinInfo
import com.moparvindecoder.domain.usecase.DecodeVinUseCase
import com.moparvindecoder.utils.Result
import kotlinx.coroutines.launch

class VinResultsViewModel : ViewModel() {

    private val repository = VinRepositoryImpl()
    private val decodeVin = DecodeVinUseCase(repository)

    private val _vinInfo = MutableLiveData<Result<VinInfo>>()
    val vinInfo: LiveData<Result<VinInfo>> get() = _vinInfo

    fun decode(vin: String) {
        viewModelScope.launch {
            _vinInfo.value = decodeVin(vin)
        }
    }
}
