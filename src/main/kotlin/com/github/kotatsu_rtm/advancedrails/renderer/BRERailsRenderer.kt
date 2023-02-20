package com.github.kotatsu_rtm.advancedrails.renderer

import com.github.kotatsu_rtm.advancedrails.Util
import com.github.kotatsu_rtm.advancedrails.model.*
import com.github.kotatsu_rtm.advancedrails.toRadians
import jp.ngt.rtm.rail.TileEntityLargeRailCore
import jp.ngt.rtm.rail.TileEntityLargeRailSwitchCore
import jp.ngt.rtm.rail.util.Point
import jp.ngt.rtm.rail.util.RailDir
import jp.ngt.rtm.rail.util.RailMapSwitch
import jp.ngt.rtm.rail.util.RailPosition
import jp.ngt.rtm.render.RailPartsRenderer
import net.minecraft.util.math.MathHelper
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.GL11
import kotlin.math.PI
import kotlin.math.atan

class BRERailsRenderer: RailPartsRenderer() {
    private val model by lazy { RenderData.models[modelName] as BRERailsModel }
    private val originalFile by lazy { OriginalScriptFile.fromModelName(modelName) }

    override fun renderRailStatic(
        tileEntity: TileEntityLargeRailCore,
        x: Double,
        y: Double,
        z: Double,
        tickProgression: Float
    ) {
        if (originalFile == OriginalScriptFile.BRERAILS05) {
            renderRailStatic2InBRERails5(tileEntity, x.toFloat(), y.toFloat(), z.toFloat())
        } else {
            renderRailStatic2(tileEntity, x.toFloat(), y.toFloat(), z.toFloat())
        }

        renderStaticParts(tileEntity, x, y, z)
    }

    override fun renderRailDynamic(
        tileEntity: TileEntityLargeRailCore,
        x: Double,
        y: Double,
        z: Double,
        tickProgression: Float
    ) {
        if (originalFile == OriginalScriptFile.BRERAILS04) return
        if (!isSwitchRail(tileEntity)) return
        tileEntity as TileEntityLargeRailSwitchCore
        if (tileEntity.switch == null) return

        val position = tileEntity.railPositions.first()
        val points = tileEntity.switch.points

        bindTexture(modelObject.textures.first().material.texture)

        GL11.glPushMatrix()
        GL11.glTranslatef(
            (x + position.posX - position.blockX).toFloat(),
            y.toFloat(),
            (z + position.posZ - position.blockZ).toFloat()
        )

        points.forEach {
            renderPoint(tileEntity, it)
        }

        GL11.glPopMatrix()
    }

    override fun shouldRenderObject(
        tileEntity: TileEntityLargeRailCore,
        objectName: String,
        len: Int,
        pos: Int
    ): Boolean {
        if (originalFile == OriginalScriptFile.BRERAILS04) return false

        val ballastIndex =
            if (originalFile == OriginalScriptFile.BRERAILS01 || originalFile == OriginalScriptFile.BRERAILS02) {
                (pos % 14) + 1
            } else {
                (pos % 8) + 1
            }
        val tag = "Ba$ballastIndex"

        if (isSwitchRail(tileEntity)) {
            if (originalFile != OriginalScriptFile.BRERAILS05 && objectName == tag) return true
            if (
                (originalFile == OriginalScriptFile.BRERAILS01
                    || originalFile == OriginalScriptFile.BRERAILS02
                    || originalFile == OriginalScriptFile.BRERAILS05)
                && model.pcs.containsName(objectName)
                ) return true
            if (originalFile == OriginalScriptFile.BRERAILS03 && model.base.containsName(objectName)) return true
        } else {
            if (
                model.fixtureL.containsName(objectName)
                || model.fixtureR.containsName(objectName)
                || model.leftParts.containsName(objectName)
                || model.rightParts.containsName(objectName)
                ) return true

            if (originalFile == OriginalScriptFile.BRERAILS05) {
                val max = MathHelper.floor(tileEntity.getRailMap(null).length * 2.0F)

                if (model.start.containsName(objectName) && pos == 0) return true
                if (model.end.containsName(objectName) && pos == max) return true
                return false
            }

            if (objectName == tag || model.pcs.containsName(objectName)) return true
        }

        return false
    }

    private fun renderRailStatic2(tileEntity: TileEntityLargeRailCore, x: Float, y: Float, z: Float) {
        if (originalFile == OriginalScriptFile.BRERAILS01) return
        if (isSwitchRail(tileEntity)) return

        val position = tileEntity.railPositions.first()
        val railMap = tileEntity.getRailMap(null)
        val max = MathHelper.floor(railMap.length * 2.0F)
        val originPos = railMap.getRailPos(max, 0)
        val originHeight = railMap.getRailHeight(max, 0).toFloat()
        val originCantHeight = 1.5F * MathHelper.abs(MathHelper.sin(railMap.getRailRoll(max, 0).toRadians()))

        bindTexture(modelObject.textures.first().material.texture)

        GL11.glPushMatrix()
        GL11.glTranslatef(
            (x + position.posX - position.blockX).toFloat(),
            (y + position.posY - position.blockY - 0.0625F).toFloat(),
            (z + position.posZ - position.blockZ).toFloat()
        )

        for (i in 0..max) {
            val pos = railMap.getRailPos(max, i)

            setBrightness(getBrightness(tileEntity.world, pos[1].toInt(), tileEntity.y, pos[0].toInt()))

            GL11.glPushMatrix()
            GL11.glTranslatef(
                (pos[1] - originPos[1]).toFloat(),
                railMap.getRailHeight(max, i).toFloat() - originHeight + originCantHeight,
                (pos[0] - originPos[0]).toFloat()
            )
            GL11.glRotatef(railMap.getRailYaw(max, i), 0.0F, 1.0F, 0.0F)
            GL11.glRotatef(-railMap.getRailPitch(max, i), 1.0F, 0.0F, 0.0F)
            if (originalFile == OriginalScriptFile.BRERAILS04)
                GL11.glRotatef(railMap.getRailRoll(max, i), 0.0F, 0.0F, 1.0F)

            if (originalFile == OriginalScriptFile.BRERAILS02) {
                val wallIndex = i % 13

                model.wallL.list[wallIndex].render(this)
                model.wallR.list[wallIndex].render(this)
                model.grooveL.render(this)
                model.grooveR.render(this)
                if (i == 0 || i == max) model.wallE.render(this)
            } else if (originalFile == OriginalScriptFile.BRERAILS03) {
                val wallIndex = i % 13

                model.wallL.list[wallIndex].render(this)
                model.wallR.list[wallIndex].render(this)
                model.base.render(this)
                model.grooveL.render(this)
                model.grooveR.render(this)
            } else if (originalFile == OriginalScriptFile.BRERAILS04) {
                val yaw = railMap.getRailYaw(max, i)
                val yaw2 = (if (i == max) yaw else railMap.getRailYaw(max, i + 1)) - yaw

                if (i % 10 == 1) {
                    if (yaw2 <= -0.005F) {
                        model.anchorL.render(this)
                    } else if (yaw2 >= 0.005F) {
                        model.anchorR.render(this)
                    }
                }
            }
            GL11.glPopMatrix()
        }
        GL11.glPopMatrix()
    }

    private fun renderRailStatic2InBRERails5(tileEntity: TileEntityLargeRailCore, x: Float, y: Float, z: Float) {
        if (isSwitchRail(tileEntity)) return

        val position = tileEntity.railPositions.first()
        val railMap = tileEntity.getRailMap(null)
        val railLength = railMap.length
        val originPos = railMap.getRailPos(1, 0)
        val originHeight = railMap.getRailHeight(1, 0)
        val originCantHeight = 1.5F * MathHelper.abs(MathHelper.sin(railMap.getRailRoll(1, 0).toRadians()))
        val numberOfSegment = MathHelper.ceil(railLength / 10.0F)
        val numberOfCrosstieInSegment = MathHelper.floor(railLength / numberOfSegment * 2.5F)
        val numberOfCrosstie = numberOfCrosstieInSegment * numberOfSegment

        bindTexture(modelObject.textures.first().material.texture)

        GL11.glPushMatrix()
        GL11.glTranslatef(
            (x + position.posX - position.blockX).toFloat(),
            (y + position.posY - position.blockY - 0.0625F).toFloat(),
            (z + position.posZ - position.blockZ).toFloat()
        )

        for (i in 0 until numberOfSegment) {
            val startPos = railMap.getRailPos(numberOfSegment, i)
            val endPos = railMap.getRailPos(numberOfSegment, i + 1)
            val segmentLengthX = (endPos[1] - startPos[1]).toFloat()
            val segmentLengthZ = (endPos[0] - startPos[0]).toFloat()

            for (j in 0..numberOfCrosstieInSegment) {
                val crosstieOffset = i * numberOfCrosstieInSegment

                setBrightness(getBrightness(tileEntity.world, endPos[1].toInt(), tileEntity.y, endPos[0].toInt()))

                GL11.glPushMatrix()
                GL11.glTranslatef(
                    (startPos[1].toFloat() + (j / numberOfSegment.toFloat()) * segmentLengthX - originPos[1]).toFloat(),
                    (railMap.getRailHeight(numberOfCrosstie, crosstieOffset + j) - originHeight + originCantHeight).toFloat(),
                    (startPos[0] + (j / numberOfCrosstieInSegment) * segmentLengthZ - originPos[0]).toFloat()
                )
                GL11.glRotatef((atan(segmentLengthX / segmentLengthZ) * 180.0F / PI).toFloat(), 0.0F, 1.0F, 0.0F)
                GL11.glRotatef(-railMap.getRailPitch(numberOfCrosstie, crosstieOffset + j), 1.0F, 0.0F, 0.0F)
                GL11.glRotatef(railMap.getRailRoll(numberOfCrosstie, crosstieOffset + j), 0.0F, 0.0F, 1.0F)

                model.pcs.render(this)

                GL11.glPopMatrix()
            }
        }
        GL11.glPopMatrix()
    }

    private fun renderPoint(tileEntity: TileEntityLargeRailSwitchCore, point: Point) {
        if (point.branchDir == RailDir.NONE) {
            val railMap = point.rmMain
            val max = MathHelper.floor(railMap.length * 2.0F)
            val halfMax = MathHelper.floor((max * 4) / 5.0F)

            renderRailMapStatic(
                tileEntity,
                railMap,
                max,
                if (point.mainDirIsPositive) 0 else halfMax,
                if (point.mainDirIsPositive) halfMax else max,
                model.leftParts,
                model.rightParts,
                model.fixtureL,
                model.fixtureR
            )
        } else {
            val tongIndex = MathHelper.floor(point.rmMain.length * 2.0F * TONG_POS)

            renderRailMapDynamic(
                tileEntity,
                point.rmMain,
                point.branchDir,
                point.mainDirIsPositive,
                point.movement * TONG_MOVE,
                tongIndex
            )

            renderRailMapDynamic(
                tileEntity,
                point.rmBranch,
                point.branchDir.invert(),
                point.branchDirIsPositive,
                (1.0F - point.movement) * TONG_MOVE,
                tongIndex
            )
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
        val startPos = tileEntity.startPoint
        val revXZ = RailPosition.REVISION[tileEntity.railPositions.first().direction.toInt()]
        val moveX = -startPos[0] + 0.5F + revXZ[0]
        val moveZ = -startPos[2] + 0.5F + revXZ[1]
        val directionFixture =
            if (isPositive && direction == RailDir.LEFT || !isPositive && direction == RailDir.RIGHT) -1.0F else 1.0F

        if (originalFile == OriginalScriptFile.BRERAILS02 || originalFile == OriginalScriptFile.BRERAILS03) {
            for (i in 0 until max) {
                val wallIndex = i % 13
                val pos = railMap.getRailPos(max, i)

                setBrightness(getBrightness(tileEntity.world, pos[1].toInt(), tileEntity.y, pos[0].toInt()))

                GL11.glPushMatrix()
                GL11.glTranslatef(
                    (moveX + pos[1]).toFloat(),
                    0.0F,
                    (moveZ + pos[0]).toFloat()
                )
                GL11.glRotatef(railMap.getRailYaw(max, i), 0.0F, 1.0F, 0.0F)

                if (originalFile == OriginalScriptFile.BRERAILS03 && i == 0) model.joint.render(this)
                if (directionFixture == 1.0F) {
                    model.wallL.list[wallIndex].render(this)
                    model.grooveL.render(this)
                } else {
                    model.wallR.list[wallIndex].render(this)
                    model.grooveR.render(this)
                }

                GL11.glPopMatrix()
            }
        }

        for (i in (if (isPositive) 0 else halfMax) until (if (isPositive) halfMax else max)) {
            val pos = railMap.getRailPos(max, i)
            val separationRate =
                (1.0F - Util.sigmoid((if (isPositive) i else max - i) / halfMax.toFloat())) *
                        movement * directionFixture
            val halfGaugeMovement = directionFixture * HALF_GAUGE

            setBrightness(getBrightness(tileEntity.world, pos[1].toInt(), tileEntity.y, pos[0].toInt()))

            GL11.glPushMatrix()
            GL11.glTranslatef(
                (moveX + pos[1]).toFloat(),
                0.0F,
                (moveZ + pos[0]).toFloat()
            )
            GL11.glRotatef(railMap.getRailYaw(max, i), 0.0F, 1.0F, 0.0F)

            if (directionFixture == 1.0F) {
                model.leftParts.render(this)
                model.fixtureL.render(this)
            } else {
                model.rightParts.render(this)
                model.fixtureL.render(this)
            }

            GL11.glTranslatef(separationRate - halfGaugeMovement, 0.0F, 0.0F)
            GL11.glRotatef(
                separationRate * YAW_RATE / railLength.toFloat() * if (isPositive) -1.0F else 1.0F,
                0.0F,
                1.0F,
                0.0F
            )
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

    private enum class OriginalScriptFile {
        BRERAILS01,
        BRERAILS02,
        BRERAILS03,
        BRERAILS04,
        BRERAILS05;
        companion object {
            fun fromModelName(name: String): OriginalScriptFile {
                with(RenderData.OriginalScriptIdentifier) {
                    if (renderBRERails01js.matches(name)) return BRERAILS01
                    if (renderBRERails02js.matches(name)) return BRERAILS02
                    if (renderBRERails03js.matches(name)) return BRERAILS03
                    if (renderBRERails04js.matches(name)) return BRERAILS04
                    if (renderBRERails05js.matches(name)) return BRERAILS05
                }

                throw IllegalStateException()
            }
        }
    }

    companion object {
        private const val TONG_MOVE = 0.35F
        private const val TONG_POS = 1.0F / 7.0F
        private const val HALF_GAUGE = 0.5647F
        private const val YAW_RATE = 450.0F
    }
}
