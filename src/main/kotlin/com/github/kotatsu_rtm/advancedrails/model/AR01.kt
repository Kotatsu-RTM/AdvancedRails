package com.github.kotatsu_rtm.advancedrails.model

import jp.ngt.rtm.render.PartsRenderer

class AR01(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val pf01 = registerParts("PF_01")
}
