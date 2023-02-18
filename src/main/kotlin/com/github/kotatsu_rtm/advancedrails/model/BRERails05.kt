package com.github.kotatsu_rtm.advancedrails.model

import jp.ngt.rtm.render.PartsRenderer

class BRERails05(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val pcs = registerParts("Pcs")
    val fixtureL = registerParts("FiL")
    val fixtureR = registerParts("FiR")
    val leftParts = registerParts("RaL", "sideL")
    val rightParts = registerParts("RaR", "sideR")
    val start = registerParts("start")
    val end = registerParts("end")
    val tongFL = registerParts("TLF")
    val tongBL = registerParts("TLB")
    val tonFR = registerParts("TRF")
    val tongBR = registerParts("TRB")
    val anchorL = registerParts("anchorL")
    val anchorR = registerParts("anchorR")
}
