package com.example.tramgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tramgo.ui.theme.TramGoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tramViewModel : TramViewModel by viewModels {
            TramViewModelFactory((application as TramsApplication).repository)
        }
        setContent {
            TramGoTheme {
                TramApp(tramViewModel = tramViewModel)
            }
        }
    }
}