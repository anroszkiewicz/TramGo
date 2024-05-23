package com.example.tramgo

import android.util.DisplayMetrics
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun TramList(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TramViewModel
    ) {
        Surface(modifier = modifier, color = MaterialTheme.colorScheme.background) {
            val trams by viewModel.allTrams.observeAsState(listOf())
            Grid(navController, trams)
        }
}

@Composable
fun Grid(navController: NavController, trams: List<Tram>) {

    val displayMetrics: DisplayMetrics = LocalContext.current.resources.displayMetrics
    val dpHeight = (displayMetrics.heightPixels / displayMetrics.density).toInt()

    Column {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.height(dpHeight.dp) //necessary so the size is not infinite
        ) {
            items(trams) {
                val itemIndex = it.id
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .padding(4.dp)
                        .size(width = 240.dp, height = 200.dp)
                        .clickable {
                            navController.navigate(route = "TramDetails/$itemIndex")
                        }
                ) {
                    Box {
                        Text(
                            text = it.displayName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
