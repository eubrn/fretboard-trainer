package de.eugenbernwald.fretboardtrainer.model

enum class Shift(val value: Int) {

    FLAT(-1),
    NATURAL(0),
    SHARP(1);

    val symbol: String
        get() {
            return when (this) {
                FLAT -> "♭"
                NATURAL -> ""
                SHARP -> "♯"
            }
        }


}
