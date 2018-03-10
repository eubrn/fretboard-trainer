package de.eugenbernwald.fretboardtrainer.model;

import java.util.Random;

public class Tone {

    private static Random random = new Random();

    private Note note;

    private Shift shift;

    private float frequency;

    private float lowerLimit;

    private float upperLimit;

    private int key;

    public Tone(Note note, Shift shift, int octave) {
        this.note = note;
        this.shift = shift;
        this.key = octave * 12 + (note.getSemitones() + shift.getValue());

        this.frequency = Tone.key2Frequency(this.key);
        this.lowerLimit = (this.frequency + key2Frequency(this.key - 1)) / 2.0f;
        this.upperLimit = (this.frequency + key2Frequency(this.key + 1)) / 2.0f;
    }

    public float getFrequency() {
        return frequency;
    }

    public float getLowerLimit() {
        return lowerLimit;
    }

    public float getUpperLimit() {
        return upperLimit;
    }

    public int getKey() {
        return key;
    }

    @Override
    public String toString() {
        return String.format("%s%s", this.note, this.shift.getSymbol());
    }

    public static float key2Frequency(int key){
        return 440.0f * (float) Math.pow(2.0f, (key - 60) / 12.0f);
    }


}
