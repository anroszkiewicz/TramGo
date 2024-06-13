package com.example.tramgo

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "Trams")
data class Tram(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "displayName") val displayName: String,
    @ColumnInfo(name = "fullName") val fullName: String,
    @ColumnInfo(name = "visited") var visited: Int,
    @ColumnInfo(name = "number") val number: Int
)

@Dao
interface TramDao {
    @Query("SELECT * FROM Trams")
    fun getList(): Flow<List<Tram>>

    //@Query("SELECT * FROM Trams WHERE type = :category")
    //fun getListByType(category: String): Flow<List<Tram>>

    @Query("SELECT * FROM Trams WHERE id = :index")
    fun getDetails(index: Int): Flow<Tram>

    @Query("UPDATE Trams SET visited = 1 WHERE id = :index")
    suspend fun updateTram(index: Int)
}

@Database(entities = [Tram::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun TramDao(): TramDao
}

