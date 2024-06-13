package com.example.tramgo

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class TramRepository(private val TramDao : TramDao) {
    val allTrams: Flow<List<Tram>> = TramDao.getList()
    fun getTram(id: Int) = TramDao.getDetails(id)

    suspend fun updateTram(id: Int) = TramDao.updateTram(id)
    suspend fun getTramPositions(): List<TramPosition> {
        return readTramData()
    }
}