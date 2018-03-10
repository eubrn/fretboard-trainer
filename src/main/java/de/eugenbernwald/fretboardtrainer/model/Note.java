package de.eugenbernwald.fretboardtrainer.model;

public enum Note {

    E(7),
    F(8),
    G(10),
    A(0),
    B(2),
    C(3),
    D(5);

    int semitones;

    Note(int semitones) {
        this.semitones = semitones;
    }

    public int getSemitones() {
        return this.semitones;
    }
}
