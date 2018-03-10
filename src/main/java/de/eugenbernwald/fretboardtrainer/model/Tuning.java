package de.eugenbernwald.fretboardtrainer.model;

import java.util.Arrays;
import java.util.List;

public enum Tuning {

    STANDARD_E(Arrays.asList(
            new GuitarString(new Tone(Note.E, Shift.NATURAL,4)),
            new GuitarString(new Tone(Note.B, Shift.NATURAL,4)),
            new GuitarString(new Tone(Note.G, Shift.NATURAL,3)),
            new GuitarString(new Tone(Note.D, Shift.NATURAL,3)),
            new GuitarString(new Tone(Note.A, Shift.NATURAL,3)),
            new GuitarString(new Tone(Note.E, Shift.NATURAL,2)))),

    DROP_D(Arrays.asList(
            new GuitarString(new Tone(Note.E, Shift.NATURAL,4)),
            new GuitarString(new Tone(Note.B, Shift.NATURAL,4)),
            new GuitarString(new Tone(Note.G, Shift.NATURAL,3)),
            new GuitarString(new Tone(Note.D, Shift.NATURAL,3)),
            new GuitarString(new Tone(Note.A, Shift.NATURAL,3)),
            new GuitarString(new Tone(Note.D, Shift.NATURAL,2))));

    List<GuitarString> guitarStrings;

    Tuning(List<GuitarString> guitarStrings) {
        this.guitarStrings = guitarStrings;
    }

    public GuitarString getString(int i){
        return this.guitarStrings.get(i);
    }

    public int numStrings(){
        return guitarStrings.size();
    }
}
