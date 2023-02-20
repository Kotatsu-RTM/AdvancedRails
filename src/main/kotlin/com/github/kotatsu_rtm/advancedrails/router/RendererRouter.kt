package com.github.kotatsu_rtm.advancedrails.router

import com.github.kotatsu_rtm.advancedrails.model.*
import com.github.kotatsu_rtm.advancedrails.renderer.*
import jp.ngt.rtm.render.ModelObject
import jp.ngt.rtm.render.PartsRenderer
import jp.ngt.rtm.render.RailPartsRenderer

object RendererRouter {
    @JvmStatic
    fun init(renderer: PartsRenderer<*, *>, modelObject: ModelObject) {
        mapOf(
            RenderData.OriginalScriptIdentifier.renderBRERails01js to Pair(::BRERailsModel, ::BRERailsRenderer),
            RenderData.OriginalScriptIdentifier.renderBRERails02js to Pair(::BRERailsModel, ::BRERailsRenderer),
            RenderData.OriginalScriptIdentifier.renderBRERails03js to Pair(::BRERailsModel, ::BRERailsRenderer),
            RenderData.OriginalScriptIdentifier.renderBRERails04js to Pair(::BRERailsModel, ::BRERailsRenderer),
            RenderData.OriginalScriptIdentifier.renderBRERails05js to Pair(::BRERailsModel, ::BRERailsRenderer),
            "^.*PF_[LR]_101\$".toRegex() to Pair(::AR01Model, ::AR01Renderer), //RenderAR_01.js
            "^.*Youheki_[LR]\$".toRegex() to Pair(::AR02Model, ::AR02Renderer), //RenderAR_02.js
            "^P_B_OER2\$".toRegex() to Pair(::OERModel, ::OERRenderer), //RenderOER.js
            "^P_T_OER2\$".toRegex() to Pair(::OERModel, ::OERRenderer), //RenderOER2.js
            "^.*(?:1067|1435)_ZXN_[LR]\$".toRegex() to Pair(::OERModel, ::OERRenderer), //RenderOER3.js
            "^P_R_WALL3[LR]\$".toRegex() to Pair(::WallModel, ::WallRenderer), //RenderWALL1.js
            "^P_R2?_(?:BLUE_|GREEN_|LIGHTBLUE_|ORANGE_|XNP|SHINJUKU)?[379][LR]?\$".toRegex()
                    to Pair(::RoofModel, ::RoofRenderer) //RenderYANE1.js
        ).forEach { (regex, modelRendererPair) ->
            if (!regex.matches(renderer.modelName)) return@forEach
            RenderData.models[renderer.modelName] = modelRendererPair.first.invoke(renderer)
            replaceRenderer(
                modelObject,
                deepCopy(RailPartsRenderer::class.java, renderer, modelRendererPair.second.invoke())
            )
        }
    }

    private inline fun <reified T: PartsRenderer<*, *>> replaceRenderer(modelObject: ModelObject, newRenderer: T) {
        if (modelObject.renderer !is T)
            modelObject::class.java
                .getDeclaredField("renderer")
                .apply { isAccessible = true }
                .set(modelObject, newRenderer)
    }

    private fun <T> deepCopy(clazz: Class<*>, instance: Any, newInstance: T): T {
        if (clazz.superclass != null) deepCopy(clazz.superclass, instance, newInstance)

        clazz.declaredFields.forEach {
            try {
                it.isAccessible = true
                it.set(newInstance, it.get(instance))
            } catch (_: Exception) {
                //Do nothing
            }
        }

        return newInstance
    }
}
