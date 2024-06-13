package com.example.tramgo

import android.util.Log
import com.example.tramgo.GtfsRealtime.FeedMessage
import com.example.tramgo.GtfsRealtime.VehiclePosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.http.GET
import java.net.URL

val minTramNumbers = intArrayOf(81,150,501,515,702,399,415,911,601,921,500,600)
val maxTramNumbers = intArrayOf(149,153,514,559,712,414,460,920,630,940,500,600)
val tramNames = arrayOf("Konstal","Alfa","Combino","Tramino","Helmut","Tatra","Beta","Beta dwukierunkowa","Gamma","Gamma dwukierunkowa","Niebieska Gamma","Czerwona Gamma")

suspend fun readTramData() : List<TramPosition>
{
    Log.d("debug","readTramData called")
    val url = URL("https://www.ztm.poznan.pl/pl/dla-deweloperow/getGtfsRtFile?file=vehicle_positions.pb")
    val `is` = withContext(Dispatchers.IO) {
        url.openStream()
    }
    val fm = FeedMessage.parseFrom(`is`)
    withContext(Dispatchers.IO) {
        `is`.close()
    }
    val positions: MutableList<TramPosition> = mutableListOf<TramPosition>()

    for (entity in fm.entityList) {
        if (entity.hasVehicle()) {
            val vp = entity.vehicle
            val number = entity.vehicle.vehicle.id.toInt()

            if (number < 1000 && vp.hasPosition()) {
                val position: GtfsRealtime.Position = vp.position
                if (position.hasLatitude() && position.hasLongitude()) {

                    val latitude: Float = position.latitude
                    val longitude: Float = position.longitude

                    var tramModel: String = ""
                    var dbIndex: Int = 0

                    for (i in minTramNumbers.indices) {
                        if(number >= minTramNumbers[i] && number <= maxTramNumbers[i]) {
                            tramModel = tramNames[i]
                            dbIndex = i
                        }
                    }

                    val tramPosition = TramPosition(
                        tramNumber = number,
                        latitude = latitude, 
                        longitude = longitude,
                        dbIndex = dbIndex,
                        tramModel = tramModel)
                    positions.add(tramPosition)
                }
            }
        }
    }
    return positions
}