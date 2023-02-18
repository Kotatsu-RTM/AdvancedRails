package com.github.kotatsu_rtm.advancedrails.model

import jp.ngt.rtm.render.PartsRenderer

class OER(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val allParts = registerParts("obj1", "obj2", "obj3", "obj5", "obj7")
}
