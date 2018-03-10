package de.eugenbernwald.fretboardtrainer.model;

public enum Shift {

    FLAT(-1),
    NATURAL(0),
    SHARP(1);

    int value;

    Shift(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getSymbol(){

        switch (this){
            case FLAT: return "♭";
            case NATURAL: return "";
            case SHARP: return "♯";
            default: return "INVALID";

        }
    }
}
