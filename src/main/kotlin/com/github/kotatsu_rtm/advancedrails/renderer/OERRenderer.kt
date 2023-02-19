package com.github.kotatsu_rtm.advancedrails.renderer

import jp.ngt.rtm.rail.TileEntityLargeRailCore
import jp.ngt.rtm.render.RailPartsRenderer

class OERRenderer: RailPartsRenderer() {
    override fun renderRailStatic(
        tileEntity: TileEntityLargeRailCore,
        x: Double,
        y: Double,
        z: Double,
        tickProgression: Float
    ) {
        renderStaticParts(tileEntity, x, y, z)
    }

    override fun renderRailDynamic(
        tileEntity: TileEntityLargeRailCore,
        x: Double,
        y: Double,
        z: Double,
        tickProgression: Float
    ) {
        //Do nothing
    }

    override fun shouldRenderObject(
        tileEntity: TileEntityLargeRailCore,
        objectName: String,
        len: Int,
        pos: Int
    ): Boolean {
        when (modelName) {
            "P_B_OER2" -> { //RenderOER.js
                if ((pos % 5 != 0 && objectName == "obj2") || (pos % 25 != 0 && objectName == "obj3")) return false
                return true
            }
            "P_T_OER2" -> { //RenderOER2.js
                if (
                    (pos % 15 != 0 && objectName == "obj7") ||
                    (pos % 8 != 0 && objectName == "obj1") ||
                    (pos % 8 == 0 && objectName == "obj5")
                    ) return false
                return true
            }
            else -> { //RenderOER3.js
                if (objectName == "obj2" || (pos % 3 == 0 && objectName == "obj1")) return true
                return false
            }
        }
    }
}
