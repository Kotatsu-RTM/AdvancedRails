package com.github.kotatsu_rtm.advancedrails.model

import jp.ngt.rtm.render.PartsRenderer

class Wall(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val allParts = registerParts("obj1", "obj2")
}
