package com.github.kotatsu_rtm.advancedrails.model

import jp.ngt.rtm.render.PartsRenderer

class BRERails04Model(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val anchorL = registerParts("anchorL")
    val anchorR = registerParts("anchorR")
}
