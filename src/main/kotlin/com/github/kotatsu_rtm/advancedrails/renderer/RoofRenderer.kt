package com.github.kotatsu_rtm.advancedrails.renderer

import com.github.kotatsu_rtm.advancedrails.model.RoofModel
import jp.ngt.rtm.rail.TileEntityLargeRailCore
import jp.ngt.rtm.render.RailPartsRenderer
import org.lwjgl.opengl.GL11
import kotlin.math.floor

class RoofRenderer: RailPartsRenderer() {
    private val model by lazy { RenderData.models[modelName] as RoofModel }

    override fun renderRailStatic(
        tileEntity: TileEntityLargeRailCore,
        x: Double,
        y: Double,
        z: Double,
        tickProgression: Float
    ) {
        if (isSwitchRail(tileEntity)) return

        val position = tileEntity.railPositions.first()
        val railMap = tileEntity.getRailMap(null)
        val max = floor(railMap.length * 2.0).toInt()
        val originPos = railMap.getRailPos(max, 0)
        val originHeight = railMap.getRailHeight(max, 0).toFloat()

        GL11.glPushMatrix()
        GL11.glTranslatef(
            (x + position.posX).toFloat() - position.blockX,
            (y + position.posY).toFloat() - position.blockY - 0.0625F,
            (z + position.posZ).toFloat() - position.blockZ
        )

        bindTexture(modelObject.textures.first().material.texture)

        for (i in 0..max step 5) {
            val pos = railMap.getRailPos(max, i)

            GL11.glPushMatrix()
            GL11.glTranslatef(
                (pos[1] - originPos[1]).toFloat(),
                railMap.getRailHeight(max, i).toFloat() - originHeight,
                (pos[0] - originPos[0]).toFloat()
            )
            GL11.glRotatef(railMap.getRailYaw(max, i), 0.0F, 1.0F, 0.0F)
            GL11.glRotatef(railMap.getRailPitch(max, i), 1.0F, 0.0F, 0.0F)

            setBrightness(240)
            model.light.render(this)

            GL11.glPopMatrix()
        }

        GL11.glPopMatrix()

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
        if (objName == "obj1" || (pos % 18 == 0 && objName == "Pillar")) return true
        return false
    }
}
