package de.eugenbernwald.fretboardtrainer.model

import java.util.*

enum class Tuning constructor(internal var guitarStrings: List<GuitarString>) {

    STANDARD_E(Arrays.asList<GuitarString>(
            GuitarString(Tone(Note.E, Shift.NATURAL, 4)),
            GuitarString(Tone(Note.B, Shift.NATURAL, 4)),
            GuitarString(Tone(Note.G, Shift.NATURAL, 3)),
            GuitarString(Tone(Note.D, Shift.NATURAL, 3)),
            GuitarString(Tone(Note.A, Shift.NATURAL, 3)),
            GuitarString(Tone(Note.E, Shift.NATURAL, 2)))),

    DROP_D(Arrays.asList<GuitarString>(
            GuitarString(Tone(Note.E, Shift.NATURAL, 4)),
            GuitarString(Tone(Note.B, Shift.NATURAL, 4)),
            GuitarString(Tone(Note.G, Shift.NATURAL, 3)),
            GuitarString(Tone(Note.D, Shift.NATURAL, 3)),
            GuitarString(Tone(Note.A, Shift.NATURAL, 3)),
            GuitarString(Tone(Note.D, Shift.NATURAL, 2))));

    fun getString(i: Int): GuitarString {
        return this.guitarStrings[i]
    }

    fun numStrings(): Int {
        return guitarStrings.size
    }
}
