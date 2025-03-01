package com.github.kotatsu_rtm.advancedrails.renderer

import com.github.kotatsu_rtm.advancedrails.model.AR01Model
import com.github.kotatsu_rtm.advancedrails.toRadians
import jp.ngt.rtm.rail.TileEntityLargeRailCore
import jp.ngt.rtm.render.RailPartsRenderer
import net.minecraft.util.math.MathHelper
import org.lwjgl.opengl.GL11

class AR01Renderer: RailPartsRenderer() {
    private val model by lazy { RenderData.models[modelName] as AR01Model }

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
        val max = MathHelper.floor(railMap.length * 2)
        val originPos = railMap.getRailPos(max, 0)
        val originHeight = railMap.getRailHeight(max, 0).toFloat()
        val originCantHeight = 1.5F * MathHelper.abs(MathHelper.sin(railMap.getRailRoll(max, 0).toRadians()))

        bindTexture(modelObject.textures.first().material.texture)

        GL11.glPushMatrix()
        GL11.glTranslatef(
            (x + (position.posX - position.blockX)).toFloat(),
            (y + (position.posY - position.blockY) - 0.0625).toFloat(),
            (z + (position.posZ - position.blockZ)).toFloat()
        )

        for (i in 0..max) {
            val pos = railMap.getRailPos(max, i)
            val cantHeight = 1.5F * MathHelper.abs(MathHelper.sin(railMap.getRailRoll(max, i).toRadians()))

            setBrightness(getBrightness(tileEntity.world, pos[1].toInt(), tileEntity.y, pos[0].toInt()))

            GL11.glPushMatrix()
            GL11.glTranslatef(
                (pos[1] - originPos[1]).toFloat(),
                railMap.getRailHeight(max, i).toFloat() - originHeight + originCantHeight - cantHeight,
                (pos[0] - originPos[0]).toFloat()
            )
            GL11.glRotatef(railMap.getRailYaw(max, i), 0.0F, 1.0F, 0.0F)
            GL11.glRotatef(-railMap.getRailPitch(max, i), 1.0F, 0.0F, 0.0F)
            if (currentRailIndex == 0) {
                if (modelName.contains("PF_R")) GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F)
            } else {
                if (tileEntity.subRails[currentRailIndex - 1].resourceName.contains("PF_R"))
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F)
                GL11.glTranslatef(2.0F, 1.0F, 0.0F)
            }

            model.pf01.render(this)

            GL11.glPopMatrix()
        }
        GL11.glPopMatrix()
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
    ) = false
}
