package com.github.kotatsu_rtm.advancedrails.model

import jp.ngt.rtm.render.Parts
import jp.ngt.rtm.render.PartsRenderer

abstract class ModelRegistry(private val renderer: PartsRenderer<*, *>) {
    protected fun registerParts(vararg name: String): Parts {
        return renderer.registerParts(Parts(*name))
    }
}
