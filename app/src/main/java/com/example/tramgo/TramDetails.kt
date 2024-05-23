package com.example.tramgo

import android.annotation.SuppressLint
import android.os.SystemClock
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TramDetails(
    modifier: Modifier = Modifier,
    navController: NavController,
    index: Int,
    viewModel: TramViewModel
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        val tram = viewModel.getTram(index).observeAsState().value
        if (tram != null) {
            DetailCard(tram)
        }
    }
}

@SuppressLint("Recycle")
@Composable
fun DetailCard(tram: Tram) {
    Column (modifier = Modifier
        .padding(20.dp)
        .fillMaxWidth()) {

        Text(tram.displayName, modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold)
        Text(tram.fullName, modifier = Modifier.padding(10.dp))
        if(tram.visited == 0) {
            Text("Nieodblokowany", modifier = Modifier.padding(10.dp))
        }
        else {
            Text("Odblokowany", modifier = Modifier.padding(10.dp))
        }
        Text("Liczba składów w Poznaniu: " + tram.number.toString(), modifier = Modifier.padding(10.dp))
    }
}
