package com.github.kotatsu_rtm.advancedrails.renderer

import com.github.kotatsu_rtm.advancedrails.model.ModelRegistry

object RenderData {
    val models = HashMap<String, ModelRegistry>()

    object OriginalScriptIdentifier {
        val renderBERRails01js = "^.*(?:1067|1435|Morido)_(?:01)?[ABLR](?:_LH)?\$".toRegex()
        val renderBERRails02js = "^.*(?:1067|1435)_02[AB][LR]\$".toRegex()
        val renderBERRails03js = "^.*(?:1067|1435)_01C(?:_[LR])?\$".toRegex()
        val renderBERRails04js = "^.*1435_CA\$".toRegex()
        val renderBERRails05js = "^.*(?:1067|1435)_03G\$".toRegex()
    }
}
