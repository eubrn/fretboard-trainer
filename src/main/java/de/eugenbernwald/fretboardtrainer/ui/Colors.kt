package de.eugenbernwald.fretboardtrainer.ui

import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import java.util.*
import java.util.stream.Collectors

class Colors {
    companion object {
        private val rnd = Random()
        private val BG_COLORS = Arrays.asList(
                Color.rgb(0x91, 0, 0),
                Color.rgb(0xff, 0xba, 0x08),
                Color.rgb(0, 0x92, 0x46),
                Color.rgb(0x3f, 0x88, 0xc5),
                Color.rgb(0x03, 0x2b, 0x43))

        @JvmStatic
        fun getNextColor(current: Color?): Color {
            val colors = BG_COLORS
                    .stream()
                    .filter { bg -> current == null || current != bg }
                    .collect(Collectors.toList())

            colors.shuffle()

            return colors[rnd.nextInt(colors.size)]
        }

        @JvmStatic
        fun toBackground(color: Color): Background {
            return Background(BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY))
        }
    }
}
