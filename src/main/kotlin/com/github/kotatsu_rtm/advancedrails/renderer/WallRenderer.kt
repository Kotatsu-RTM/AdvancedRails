package com.github.kotatsu_rtm.advancedrails.renderer

import jp.ngt.rtm.rail.TileEntityLargeRailCore
import jp.ngt.rtm.render.RailPartsRenderer

class WallRenderer: RailPartsRenderer() {
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
        objName: String,
        len: Int,
        pos: Int
    ): Boolean {
        if (pos % 8 != 0 && objName == "obj2") return false
        return true
    }
}
