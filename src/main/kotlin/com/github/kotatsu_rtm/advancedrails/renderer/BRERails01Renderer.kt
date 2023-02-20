package com.github.kotatsu_rtm.advancedrails.renderer

import com.github.kotatsu_rtm.advancedrails.Util
import com.github.kotatsu_rtm.advancedrails.model.BRERails01Model
import jp.ngt.rtm.rail.TileEntityLargeRailCore
import jp.ngt.rtm.rail.TileEntityLargeRailSwitchCore
import jp.ngt.rtm.rail.util.RailDir
import jp.ngt.rtm.rail.util.RailMapSwitch
import jp.ngt.rtm.rail.util.RailPosition
import jp.ngt.rtm.render.RailPartsRenderer
import net.minecraft.util.math.MathHelper
import org.lwjgl.opengl.GL11

class BRERails01Renderer : RailPartsRenderer() {
    private val model by lazy { RenderData.models[modelName] as BRERails01Model }

    override fun renderRailStatic(
        tileEntity: TileEntityLargeRailCore,
        x: Double,
        y: Double,
        z: Double,
        tickProgression: Float
    ) {
        if (isSwitchRail(tileEntity)) return
        renderStaticParts(tileEntity, x, y, z)
    }

    override fun renderRailDynamic(
        tileEntity: TileEntityLargeRailCore,
        x: Double,
        y: Double,
        z: Double,
        tickProgression: Float
    ) {
        if (!isSwitchRail(tileEntity)) return
        tileEntity as TileEntityLargeRailSwitchCore

        if (tileEntity.switch == null) return

        val position = tileEntity.railPositions.first()

        bindTexture(modelObject.textures.first().material.texture)

        GL11.glPushMatrix()
        GL11.glTranslatef(
            (x + (position.posX - position.blockX)).toFloat(),
            y.toFloat(),
            (z + (position.posZ - position.blockZ)).toFloat()
        )

        tileEntity.switch.points.forEach {
            if (it.branchDir == RailDir.NONE) {
                val railMap = it.rmMain
                val max = MathHelper.floor(railMap.length * 2.0F)
                val halfMax = MathHelper.floor((max * 4) / 5.0F)
                val startIndex = if (it.mainDirIsPositive) 0 else halfMax
                val endIndex = if (it.mainDirIsPositive) halfMax else max

                renderRailMapStatic(
                    tileEntity,
                    railMap,
                    max,
                    startIndex,
                    endIndex,
                    model.leftParts,
                    model.rightParts,
                    model.fixtureL,
                    model.fixtureR
                )
            } else {
                val tongIndex = MathHelper.floor(it.rmMain.length * 2.0F * TONG_POS)

                renderRailMapDynamic(
                    tileEntity,
                    it.rmMain,
                    it.branchDir,
                    it.mainDirIsPositive,
                    it.movement * TONG_MOVE,
                    tongIndex
                )

                renderRailMapDynamic(
                    tileEntity,
                    it.rmBranch,
                    it.branchDir.invert(),
                    it.branchDirIsPositive,
                    (1.0F - it.movement) * TONG_MOVE,
                    tongIndex
                )
            }
        }
        GL11.glPopMatrix()
    }

    override fun shouldRenderObject(
        tileEntity: TileEntityLargeRailCore,
        objectName: String,
        len: Int,
        pos: Int
    ): Boolean {
        val tag = "Ba${pos % 14 + 1}"

        return if (isSwitchRail(tileEntity)) {
            objectName == tag || model.pcs.containsName(objectName)
        } else {
            objectName == tag ||
                    model.fixtureL.containsName(modelName) ||
                    model.fixtureR.containsName(modelName) ||
                    model.leftParts.containsName(modelName) ||
                    model.rightParts.containsName(modelName)
        }
    }

    private fun renderRailMapDynamic(
        tileEntity: TileEntityLargeRailSwitchCore,
        railMap: RailMapSwitch,
        direction: RailDir,
        isPositive: Boolean,
        movement: Float,
        tongIndex: Int
    ) {
        val railLength = railMap.length
        val max = MathHelper.floor(railLength * 2.0F)
        val halfMax = MathHelper.floor((max * 4) / 5.0F)
        val startIndex = if (isPositive) 0 else halfMax
        val endIndex = if (isPositive) halfMax else max
        val startPos = tileEntity.startPoint
        val revXZ = RailPosition.REVISION[tileEntity.railPositions.first().direction.toInt()]
        val directionFixture =
            if (isPositive && direction == RailDir.LEFT || !isPositive && direction == RailDir.RIGHT) {
                -1.0F
            } else {
                1.0F
            }

        for (i in startIndex..endIndex) {
            val pos = railMap.getRailPos(max, i)
            val separationRate =
                1.0F - Util.sigmoid((if (isPositive) i else max - i) / halfMax.toFloat()) * movement * directionFixture
            val halfGaugeMovement = directionFixture * HALF_GAUGE
            val yaw = separationRate * YAW_RATE / railLength.toFloat() * if (isPositive) -1.0F else 1.0F

            setBrightness(getBrightness(tileEntity.world, pos[1].toInt(), tileEntity.y, pos[0].toInt()))

            GL11.glPushMatrix()
            GL11.glTranslatef(
                (-startPos[0] + 0.5F + revXZ[0] + pos[1]).toFloat(),
                0.0F,
                (-startPos[2] + 0.5F + revXZ[1] + pos[0]).toFloat()
            )
            GL11.glRotatef(railMap.getRailYaw(max, i), 0.0F, 1.0F, 0.0F)

            if (directionFixture == -1.0F) {
                model.rightParts.render(this)
                model.fixtureR.render(this)
            } else {
                model.leftParts.render(this)
                model.fixtureL.render(this)
            }

            GL11.glTranslatef(separationRate - halfGaugeMovement, 0.0F, 0.0F)
            GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F)
            GL11.glTranslatef(halfGaugeMovement, 0.0F, 0.0F)

            if (direction == RailDir.LEFT) {
                if (isPositive) {
                    if (i == tongIndex) {
                        model.tongBL.render(this)
                    } else if (i > tongIndex) {
                        model.leftParts.render(this)
                        model.fixtureL.render(this)
                    }
                } else {
                    if (i == max - tongIndex) {
                        model.tongFR.render(this)
                    } else if (i < max - tongIndex) {
                        model.rightParts.render(this)
                        model.fixtureR.render(this)
                    }
                }
            } else {
                if (isPositive) {
                    if (i == tongIndex) {
                        model.tongBR.render(this)
                    } else if (i > tongIndex) {
                        model.rightParts.render(this)
                        model.fixtureR.render(this)
                    }
                } else {
                    if (i == max - tongIndex) {
                        model.tongFL.render(this)
                    } else if (i < max - tongIndex) {
                        model.leftParts.render(this)
                        model.fixtureL.render(this)
                    }
                }
            }

            GL11.glPopMatrix()
        }
    }

    companion object {
        private const val TONG_MOVE = 0.35F
        private const val TONG_POS = 1.0F / 7.0F
        private const val HALF_GAUGE = 0.5647F
        private const val YAW_RATE = 450.0F
    }
}
