package com.github.kotatsu_rtm.advancedrails.model

import jp.ngt.rtm.render.PartsRenderer

class Roof(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val allParts = registerParts("Pillar", "obj1")
    val light = registerParts("Lights")
}
