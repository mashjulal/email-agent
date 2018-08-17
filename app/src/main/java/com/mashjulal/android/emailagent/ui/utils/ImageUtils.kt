package com.mashjulal.android.emailagent.ui.utils

import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator

fun createTextIcon(textInIcon: String, stringForColorGenerate: String): TextDrawable {
    val iconText = if (textInIcon.length > 2)
        textInIcon
            .split(" ", ".", limit = 2)
            .map { it.first() }
            .joinToString("")
    else textInIcon
    return TextDrawable.builder()
            .beginConfig()
            .fontSize(60)
            .toUpperCase()
            .endConfig()
            .buildRound(iconText, ColorGenerator.MATERIAL.getColor(stringForColorGenerate))
}