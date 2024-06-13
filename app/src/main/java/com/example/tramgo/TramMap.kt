package com.example.tramgo

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay


private const val LOCATION_PERMISSION_REQUEST_CODE = 1
@Composable
fun TramMap(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TramViewModel
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        val location = viewModel.location.observeAsState()
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
        val context = LocalContext.current
        LaunchedEffect(key1 = context) {
            while(true) {
                Log.d("debug","launched effect called")
                viewModel.updateTramPositions()
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
                    val locationResult = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    locationResult.addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            val lastKnownLocation = task.result
                            if (lastKnownLocation != null) {
                                viewModel.location.value = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                            }
                        } else {
                            Log.d("Location", "Current location is null. Using defaults.")
                        }
                    }
                }
                delay(10000)
            }
        }

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(52.4, 16.935), 10f)
        }
        val tramPositions = viewModel.positions.observeAsState()
        GoogleMap(
            modifier = Modifier.height(100.dp),
            properties = properties,
            cameraPositionState = cameraPositionState
        ){
            tramPositions.value?.let { BasicMarkersMapContent(trams = it, location = location, navController = navController, viewModel = viewModel) }
        }

    }
}

@Composable
@GoogleMapComposable
fun BasicMarkersMapContent(
    trams: List<TramPosition>,
    location: State<LatLng?>,
    navController: NavController,
    viewModel: TramViewModel,
    onTramClick: (id: Int, marker: Marker) -> Boolean = { id, marker ->
        val arr = floatArrayOf(0f);
        val latitude = location.value?.latitude ?: 0.0
        val longitude = location.value?.longitude ?: 0.0
        Location.distanceBetween(
            latitude,
            longitude,
            marker.position.latitude,
            marker.position.longitude,
            arr
        )
        if(arr[0] < 50) {
            viewModel.markTramAsVisited(id)
            navController.navigate(route = "TramDetails/$id")
        }
        false
    }
) {
    trams.forEach { tram ->
        Log.d("tram", tram.latitude.toString() + " " + tram.longitude.toString())
        Marker(
            state = MarkerState(position = LatLng(tram.latitude.toDouble(), tram.longitude.toDouble())),
            title = tram.tramModel,
            //snippet = ,
            //tag = ,
            onClick = {
                onTramClick(tram.dbIndex, it)
            },
            zIndex = 2f
        )
    }
}
