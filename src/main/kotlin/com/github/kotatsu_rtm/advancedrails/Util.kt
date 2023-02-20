package com.github.kotatsu_rtm.advancedrails

import net.minecraft.util.math.MathHelper
import kotlin.math.PI

fun Float.toRadians() = this * (PI / 180).toFloat()

object Util {
    fun sigmoid(x: Float): Float {
        val a = 3.5F
        return a / MathHelper.sqrt(1.0F + a * a) * 0.75F + 0.25F
    }
}
