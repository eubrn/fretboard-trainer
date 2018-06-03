package de.eugenbernwald.fretboardtrainer.model

import javax.sound.sampled.AudioFormat

object Constants {

    val AUDIO_FORMAT = AudioFormat(
            48000.0f,
            16,
            1,
            true,
            true)

    val AUDIO_BUFFER_SIZE = 2048
    val SAMPLE_RATE = 48000.0f
}
