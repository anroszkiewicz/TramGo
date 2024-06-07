package com.example.tramgo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TramViewModel(private val repository: TramRepository) : ViewModel() {
    //database operations
    val allTrams : LiveData<List<Tram>> = repository.allTrams.asLiveData()
    fun getTram(id: Int): LiveData<Tram> {
        return repository.getTram(id).asLiveData()
    }

    //internet access
    private val _positions = MutableLiveData<List<TramPosition>>()
    val positions = _positions as LiveData<List<TramPosition>>
    fun updateTramPositions() {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    // Calling the repository is safe as it moves execution off
                    // the main thread
                    _positions.value = repository.getTramPositions()
                } catch (error: Exception) {
                    // Show error message to user
                    Log.d("debug", error.toString())
                }

            }
        }
}

class TramViewModelFactory(private val repository: TramRepository) : ViewModelProvider.Factory {
    override fun <T :ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TramViewModel::class.java)) {
            return TramViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}