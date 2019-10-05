package com.mairwunnx.projectessentials.projectessentialsspawn.helpers

import com.mairwunnx.projectessentials.projectessentialsspawn.PART_OF_MOD
import com.mairwunnx.projectessentials.projectessentialsspawn.enums.ForgeRootPaths
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import java.io.File

// TODO: MOVE TO CORE PROJECT!!!

internal val CONFIG_FOLDER = root + File.separator + "config"
internal val MOD_CONFIG_FOLDER =
    CONFIG_FOLDER + File.separator + PART_OF_MOD.replace(" ", "")
internal val SPAWN_CONFIG = MOD_CONFIG_FOLDER + File.separator + "spawn.json"

private val root: String
    get() {
        var rootPath = ""
        DistExecutor.runWhenOn(Dist.CLIENT) {
            Runnable {
                rootPath = getRootPath(ForgeRootPaths.CLIENT)
            }
        }
        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER) {
            Runnable {
                rootPath = getRootPath(ForgeRootPaths.SERVER)
            }
        }
        return rootPath
    }
