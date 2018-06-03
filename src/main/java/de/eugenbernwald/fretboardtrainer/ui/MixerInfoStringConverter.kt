package de.eugenbernwald.fretboardtrainer.ui

import javafx.util.StringConverter

import javax.sound.sampled.Mixer

class MixerInfoStringConverter : StringConverter<Mixer.Info>() {

    override fun toString(info: Mixer.Info): String {
        return info.name
    }

    override fun fromString(string: String): Mixer.Info? {
        return null
    }
}
