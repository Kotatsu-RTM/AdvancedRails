package com.github.kotatsu_rtm.advancedrails.model

import jp.ngt.rtm.render.PartsRenderer

class BRERails04(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val anchorL = registerParts("anchorL")
    val anchorR = registerParts("anchorR")
}
