package de.eugenbernwald.fretboardtrainer.model

enum class Note(semitones: Int) {

    E(7),
    F(8),
    G(10),
    A(0),
    B(2),
    C(3),
    D(5);

    val semitones: Int

    init {
        this.semitones = semitones
    }
}
