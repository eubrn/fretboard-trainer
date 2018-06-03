package de.eugenbernwald.fretboardtrainer.model

import java.util.*

class GuitarString internal constructor(startTone: Tone) {

    private val random = Random()
    private val tones: MutableList<HashSet<Tone>>

    init {

        this.tones = ArrayList()

        val allNotes = EnumSet.allOf(Note::class.java)
        val allShifts = EnumSet.allOf(Shift::class.java)
        val octave = startTone.key / 12

        for (i in 0..11) {
            this.tones.add(HashSet())
            for (note in allNotes) {
                for (shift in allShifts) {
                    for (j in -1..1) {
                        val tone = Tone(note, shift, octave + j)
                        if (Tone.key2Frequency(startTone.key + i) == tone.frequency) {
                            this.tones[i].add(tone)
                        }
                    }


                }
            }
        }
    }

    fun getRandomToneOnFret(num: Int): Tone {
        val possibleTones = this.tones[num]
        return ArrayList(possibleTones)[this.random.nextInt(possibleTones.size)]
    }
}
