package com.moparvindecoder.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VinHistoryViewModel : ViewModel() {

    private val _history = MutableLiveData<List<String>>(emptyList())
    val history: LiveData<List<String>> get() = _history

    // Placeholder to add to history; in real app this would persist to DB
    fun addToHistory(vin: String) {
        val current = _history.value?.toMutableList() ?: mutableListOf()
        current.add(0, vin)
        _history.value = current
    }
}
