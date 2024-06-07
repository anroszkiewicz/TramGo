package com.example.tramgo

import android.util.Log
import com.example.tramgo.GtfsRealtime.FeedMessage
import com.example.tramgo.GtfsRealtime.VehiclePosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL


suspend fun readTramData()
{
    val url = URL("https://www.ztm.poznan.pl/pl/dla-deweloperow/getGtfsRtFile?file=vehicle_positions.pb")
    val `is` = withContext(Dispatchers.IO) {
        url.openStream()
    }
    val fm = FeedMessage.parseFrom(`is`)
    withContext(Dispatchers.IO) {
        `is`.close()
    }

    for (entity in fm.entityList) {
        if (entity.hasVehicle()) {
            val vp = entity.vehicle
            processVehiclePosition(vp);
        }
    }
}

fun processVehiclePosition(vp: VehiclePosition) {
    if (vp.hasPosition()) {
        val position: GtfsRealtime.Position = vp.position
        if (position.hasLatitude() && position.hasLongitude()) {
            val latitude: Float = position.latitude
            val longitude: Float = position.longitude
            Log.d("latitude",latitude.toString());
            Log.d("longitude",longitude.toString());
        }
    }
}