package com.example.tramgo

import kotlinx.coroutines.flow.Flow

class TramRepository(private val TramDao : TramDao) {
    val allTrams: Flow<List<Tram>> = TramDao.getList()
    fun getTram(id: Int) = TramDao.getDetails(id)
    //fun getTramsByType(category: String) = TramDao.getListByType(category)
}