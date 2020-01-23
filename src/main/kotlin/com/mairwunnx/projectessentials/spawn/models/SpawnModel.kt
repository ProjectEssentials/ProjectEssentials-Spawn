package com.mairwunnx.projectessentials.spawn.models

import kotlinx.serialization.Serializable

@Serializable
data class SpawnModel(
    var worldId: Int = 0,
    var xPos: Double = 0.5,
    var yPos: Double = 100.0,
    var zPos: Double = 0.5,
    var yaw: Float = 0f,
    var pitch: Float = 0f
)
