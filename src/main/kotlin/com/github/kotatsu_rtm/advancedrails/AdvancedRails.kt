package com.github.kotatsu_rtm.advancedrails

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Mod(modid = AdvancedRails.MOD_ID, name = AdvancedRails.MOD_NAME, version = AdvancedRails.MOD_VERSION)
@EventBusSubscriber(modid = AdvancedRails.MOD_ID)
class AdvancedRails {
    companion object {
        const val MOD_ID = "advancedrails"
        const val MOD_NAME = "AdvancedRails"
        const val MOD_VERSION = "1.5"
    }
}
