package com.example.tramgo

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.maps.android.compose.GoogleMapComposable
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberMarkerState


private const val LOCATION_PERMISSION_REQUEST_CODE = 1
@Composable
fun TramMap(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TramViewModel
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        var properties = MapProperties()
        if (ContextCompat.checkSelfPermission(
                LocalContext.current,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                LocalContext.current,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            properties = MapProperties(isMyLocationEnabled = true)
        }
        else {
            ActivityCompat.requestPermissions(
                LocalContext.current as Activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        val condition = true
        LaunchedEffect(key1 = condition) {
            while(condition==true) {
                Log.d("debug","launched effect called")
                viewModel.updateTramPositions()
                delay(10000)
            }
        }

        val singapore = LatLng(52.34, 16.86)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
        val tramPositions = viewModel.positions.observeAsState()
        GoogleMap(
            modifier = Modifier.height(100.dp),
            properties = properties,
            cameraPositionState = cameraPositionState
        ){
            tramPositions.value?.let { BasicMarkersMapContent(trams = it) }
        }

    }
}

@Composable
@GoogleMapComposable
fun BasicMarkersMapContent(
    trams: List<TramPosition>,
    onTramClick: (Marker) -> Boolean = { false }
) {
    trams.forEach { tram ->
        Log.d("tram", tram.latitude.toString() + " " + tram.longitude.toString())
        Marker(
            state = MarkerState(position = LatLng(tram.latitude.toDouble(), tram.longitude.toDouble())),
            title = tram.tramModel,
            //snippet = ,
            //tag = ,
            onClick = { marker ->
                onTramClick(marker)
                false
            },
            zIndex = 2f
        )
    }
}
