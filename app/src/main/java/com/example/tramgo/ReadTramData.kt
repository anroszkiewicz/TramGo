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
            if (vp.hasPosition()) {
                val position: GtfsRealtime.Position = vp.position
                if (position.hasLatitude() && position.hasLongitude()) {

                    val latitude: Float = position.latitude
                    val longitude: Float = position.longitude
                  //  Log.d("latitude",latitude.toString());
                   // Log.d("longitude",longitude.toString());

                    val tramPosition = TramPosition(
                        tramNumber = 0, 
                        latitude = latitude, 
                        longitude = longitude,
                        lineNumber = 0,
                        tramModel = "")
                    positions.add(tramPosition)
                }
            }
        }
    }
    return positions
}

fun processVehiclePosition(vp: VehiclePosition) {
    if (vp.hasPosition()) {
        val position: GtfsRealtime.Position = vp.position
        if (position.hasLatitude() && position.hasLongitude()) {
            val latitude: Float = position.latitude
            val longitude: Float = position.longitude
            Log.d("latitude", latitude.toString());
            Log.d("longitude", longitude.toString());
        }
    }
}