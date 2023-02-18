package com.github.kotatsu_rtm.advancedrails.model

import jp.ngt.rtm.render.PartsRenderer

class AR02Model(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val base = registerParts("Base")
    val end = registerParts("End")
    val wall = Wall(renderer)

    @Suppress("MemberVisibilityCanBePrivate")
    class Wall(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
        val wall1 = registerParts("Ba1")
        val wall2 = registerParts("Ba2")
        val wall3 = registerParts("Ba3")
        val wall4 = registerParts("Ba4")
        val wall5 = registerParts("Ba5")
        val wall6 = registerParts("Ba6")
        val wall7 = registerParts("Ba7")
        val wall8 = registerParts("Ba8")

        val list = listOf(
            wall1,
            wall2,
            wall3,
            wall4,
            wall5,
            wall6,
            wall7,
            wall8
        )
    }
}
