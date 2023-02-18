package com.github.kotatsu_rtm.advancedrails.model

import jp.ngt.rtm.render.PartsRenderer

class BRERails01(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val pcs = registerParts("Pcs")
    val ballasts = registerParts(
        "Ba1", "Ba2", "Ba3", "Ba4","Ba5", "Ba6", "Ba7",
        "Ba8", "Ba9", "Ba10", "Ba11", "Ba12", "Ba13", "Ba14"
    )
    val fixtureL = registerParts("FiL")
    val fixtureR = registerParts("FiR")
    val leftParts = registerParts("RaL, sideL")
    val rightParts = registerParts("RaR", "sideR")
    val tongFL = registerParts("TLF")
    val tongBL = registerParts("TLB")
    val tongFR = registerParts("TRF")
    val tongBR = registerParts("TRB")
    val anchorL = registerParts("anchorL")
    val anchorR = registerParts("anchorR")
}
