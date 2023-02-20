package com.github.kotatsu_rtm.advancedrails.renderer

import com.github.kotatsu_rtm.advancedrails.model.ModelRegistry

object RenderData {
    val models = HashMap<String, ModelRegistry>()

    object OriginalScriptIdentifier {
        val renderBRERails01js = "^.*(?:1067|1435|Morido)_(?:01)?[ABLR](?:_LH)?\$".toRegex()
        val renderBRERails02js = "^.*(?:1067|1435)_02[AB][LR]\$".toRegex()
        val renderBRERails03js = "^.*(?:1067|1435)_01C(?:_[LR])?\$".toRegex()
        val renderBRERails04js = "^.*1435_CA\$".toRegex()
        val renderBRERails05js = "^.*(?:1067|1435)_03G\$".toRegex()
    }
}
