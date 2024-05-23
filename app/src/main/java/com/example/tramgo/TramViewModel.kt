package com.example.tramgo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

class TramViewModel(private val repository: TramRepository) : ViewModel() {
    //database operations
    val allTrams : LiveData<List<Tram>> = repository.allTrams.asLiveData()
    fun getTram(id: Int): LiveData<Tram> {
        return repository.getTram(id).asLiveData()
    }
    //fun getTramsByType(category: String): LiveData<List<Tram>> {
    //    return repository.getTramsByType(category).asLiveData()
    //}
}

class TramViewModelFactory(private val repository: TramRepository) : ViewModelProvider.Factory {
    override fun <T :ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TramViewModel::class.java)) {
            return TramViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}