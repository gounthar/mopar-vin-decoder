package com.moparvindecoder.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VinScannerViewModel : ViewModel() {

    private val _status = MutableLiveData("Waiting for camera permission…")
    val status: LiveData<String> get() = _status

    fun setStatus(message: String) {
        _status.value = message
    }
}
