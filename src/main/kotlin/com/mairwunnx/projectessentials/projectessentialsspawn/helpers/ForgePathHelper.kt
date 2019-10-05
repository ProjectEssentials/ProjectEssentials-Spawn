package com.mairwunnx.projectessentials.projectessentialsspawn.helpers

import com.mairwunnx.projectessentials.projectessentialsspawn.enums.ForgeRootPaths
import net.minecraft.client.Minecraft
import java.io.File

// TODO: MOVE TO CORE PROJECT!!!

private val clientRootDir by lazy {
    Minecraft.getInstance().gameDir.absolutePath
}
private val serverRootDir by lazy {
    File(".").absolutePath
}

fun getRootPath(pathType: ForgeRootPaths): String {
    return when (pathType) {
        ForgeRootPaths.CLIENT -> clientRootDir
        ForgeRootPaths.SERVER -> serverRootDir
    }
}
