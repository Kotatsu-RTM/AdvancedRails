package com.github.kotatsu_rtm.advancedrails.model

import jp.ngt.rtm.render.PartsRenderer

class BRERails02(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val pcs = registerParts("Pcs")
    val ballasts = registerParts(
        "Ba1", "Ba2", "Ba3", "Ba4","Ba5", "Ba6", "Ba7",
        "Ba8", "Ba9", "Ba10", "Ba11", "Ba12", "Ba13", "Ba14"
    )
    val fixtureL = registerParts("FiL")
    val fixtureR = registerParts("FiR")
    val leftParts = registerParts("RaL", "sideL")
    val rightParts = registerParts("RaR", "sideR")
    val tongFL = registerParts("TLF")
    val tongBL = registerParts("TLB")
    val tongFR = registerParts("TRF")
    val tongBR = registerParts("TRB")
    val grooveL = registerParts("grooveL")
    val grooveR = registerParts("grooveR")
    val joint = registerParts("joint")
    val anchorL = registerParts("anchorL")
    val anchorR = registerParts("anchorR")
    val wallE = registerParts("wallE")
    val wallL = WallL(renderer)
    val wallR = WallR(renderer)

    @Suppress("MemberVisibilityCanBePrivate")
    class WallL(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
        val wall1 = registerParts("wallL_1")
        val wall2 = registerParts("wallL_2")
        val wall3 = registerParts("wallL_3")
        val wall4 = registerParts("wallL_4")
        val wall5 = registerParts("wallL_5")
        val wall6 = registerParts("wallL_6")
        val wall7 = registerParts("wallL_7")
        val wall8 = registerParts("wallL_8")
        val wall9 = registerParts("wallL_9")
        val wall10 = registerParts("wallL_10")
        val wall11 = registerParts("wallL_11")
        val wall12 = registerParts("wallL_12")
        val wall13 = registerParts("wallL_13")

        val list = listOf(
            wall1,
            wall2,
            wall3,
            wall4,
            wall5,
            wall6,
            wall7,
            wall8,
            wall9,
            wall10,
            wall11,
            wall12,
            wall13
        )
    }

    @Suppress("MemberVisibilityCanBePrivate")
    class WallR(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
        val wall1 = registerParts("wallR_1")
        val wall2 = registerParts("wallR_2")
        val wall3 = registerParts("wallR_3")
        val wall4 = registerParts("wallR_4")
        val wall5 = registerParts("wallR_5")
        val wall6 = registerParts("wallR_6")
        val wall7 = registerParts("wallR_7")
        val wall8 = registerParts("wallR_8")
        val wall9 = registerParts("wallR_9")
        val wall10 = registerParts("wallR_10")
        val wall11 = registerParts("wallR_11")
        val wall12 = registerParts("wallR_12")
        val wall13 = registerParts("wallR_13")

        val list = listOf(
            wall1,
            wall2,
            wall3,
            wall4,
            wall5,
            wall6,
            wall7,
            wall8,
            wall9,
            wall10,
            wall11,
            wall12,
            wall13
        )
    }
}
