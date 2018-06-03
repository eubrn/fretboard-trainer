package de.eugenbernwald.fretboardtrainer.service

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import de.eugenbernwald.fretboardtrainer.model.Tone
import javafx.animation.AnimationTimer
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty

class ToneEvaluator(var tone: Tone, private val correctToneListener: () -> Unit  ) : AnimationTimer(), PitchDetectionHandler {

    private var currentPitch: Float = 0.0f
    val progress: DoubleProperty

    init {
        this.currentPitch = 0.0f
        this.progress = SimpleDoubleProperty(0.0)
    }

    override fun handle(now: Long) {
        if (this.currentPitch < tone.upperLimit && this.currentPitch > tone.lowerLimit) {
            hit()
        } else {
            miss()
        }
    }

    override fun handlePitch(pitchDetectionResult: PitchDetectionResult, audioEvent: AudioEvent) {
        this.currentPitch = pitchDetectionResult.pitch
    }

    private fun hit() {
        progress.value = progress.get() + 1.0 / 50

        if (progress.get() >= 1.0) {
            progress.setValue(0)
            correctToneListener()
        }

    }

    private fun miss() {

        if (progress.get() > 0) {
            progress.value = progress.get() - 1.0 / 50
        } else {
            progress.setValue(0)
        }
    }
}
