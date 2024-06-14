package com.example.tramgo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TramViewModel(private val repository: TramRepository) : ViewModel() {
    val location: MutableLiveData<LatLng> by lazy {MutableLiveData<LatLng>(LatLng(0.0, 0.0))}
    //database operations
    val allTrams : LiveData<List<Tram>> = repository.allTrams.asLiveData()
    fun getTram(id: Int): LiveData<Tram> {
        return repository.getTram(id).asLiveData()
    }
    fun markTramAsVisited(id: Int) {
        viewModelScope.launch {
            //val tram = getTram(id).value
            Log.d("debug","trying to change tram status")
            Log.d("debug",id.toString())
            Log.d("debug","changing tram status")
            //    tram.visited = 1
            repository.updateTram(id)
        }
    }

    //internet access
    private val _positions = MutableLiveData<List<TramPosition>>()
    val positions = _positions as LiveData<List<TramPosition>>
    fun updateTramPositions() {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    // Calling the repository is safe as it moves execution off
                    // the main thread
                    _positions.postValue(repository.getTramPositions())
                } catch (error: Exception) {
                    // Show error message to user
                    Log.d("debug", error.toString())
                }

            }
        }

    //map
    val displayDialog: MutableLiveData<Int> by lazy {MutableLiveData<Int>(0)}
    var displayName: String = ""
}

class TramViewModelFactory(private val repository: TramRepository) : ViewModelProvider.Factory {
    override fun <T :ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TramViewModel::class.java)) {
            return TramViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}