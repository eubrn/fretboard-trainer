package de.eugenbernwald.fretboardtrainer.model

enum class Shift(value: Int) {

    FLAT(-1),
    NATURAL(0),
    SHARP(1);

    val value: Int

    init {
        this.value = value
    }

    val symbol: String
        get() {
            return when (this) {
                FLAT -> "♭"
                NATURAL -> ""
                SHARP -> "♯"
            }
        }


}
