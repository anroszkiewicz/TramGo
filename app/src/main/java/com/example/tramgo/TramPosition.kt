package com.example.tramgo

import kotlinx.serialization.Serializable

//@Serializable
class TramPosition (
    val tramNumber: Int,
    val latitude: Float,
    val longitude: Float,
    val dbIndex: Int,
    val tramModel: String
)