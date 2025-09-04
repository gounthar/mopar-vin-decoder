package com.moparvindecoder.ui.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VinInputViewModel : ViewModel() {

    private val _vin = MutableLiveData("")
    val vin: LiveData<String> get() = _vin

    fun onVinChanged(value: String) {
        _vin.value = value.uppercase()
    }
}
