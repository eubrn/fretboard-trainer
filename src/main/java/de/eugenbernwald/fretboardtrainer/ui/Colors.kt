package de.eugenbernwald.fretboardtrainer.ui

import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import java.util.*

object Colors {
    private val rnd = Random()
    private val BG_COLORS = listOf(
            Color.rgb(0x91, 0, 0),
            Color.rgb(0xff, 0xba, 0x08),
            Color.rgb(0, 0x92, 0x46),
            Color.rgb(0x3f, 0x88, 0xc5),
            Color.rgb(0x03, 0x2b, 0x43))

    fun getNextColor(current: Color?): Color {
        val colors = BG_COLORS
                .filter { bg -> current == null || current != bg }
                .shuffled()
                .toMutableList()

        return colors[rnd.nextInt(colors.size)]
    }

    fun toBackground(color: Color): Background {
        return Background(BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY))
    }
}
