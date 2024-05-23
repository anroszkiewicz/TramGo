package com.example.tramgo

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TramsApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { TramRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TramRepository(database.TramDao()) }
}