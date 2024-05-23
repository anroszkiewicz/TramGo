package com.example.tramgo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Tram::class], version = 1, exportSchema = false)
    abstract class TramRoomDatabase : RoomDatabase(){
    abstract fun TramDao(): TramDao

    companion object {
        @Volatile
        private var INSTANCE: TramRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TramRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TramRoomDatabase::class.java,
                    "TramGoDatabase.db"
                ).createFromAsset("databases/TramGoDatabase.db").build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}