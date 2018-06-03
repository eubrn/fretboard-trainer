package de.eugenbernwald.fretboardtrainer.model

import java.util.*

class Tone(private val note: Note, private val shift: Shift, octave: Int) {

    val frequency: Float

    val lowerLimit: Float

    val upperLimit: Float

    val key: Int

    init {
        this.key = octave * 12 + (note.semitones + shift.value)

        this.frequency = Tone.key2Frequency(this.key)
        this.lowerLimit = (this.frequency + key2Frequency(this.key - 1)) / 2.0f
        this.upperLimit = (this.frequency + key2Frequency(this.key + 1)) / 2.0f
    }

    override fun toString(): String {
        return String.format("%s%s", this.note, this.shift.symbol)
    }

    companion object {

        private val random = Random()

        fun key2Frequency(key: Int): Float {
            return 440.0f * Math.pow(2.0, ((key - 60) / 12.0f).toDouble()).toFloat()
        }
    }


}
