package de.eugenbernwald.fretboardtrainer.model;

import java.util.*;

public class GuitarString {

    private Random random = new Random();
    private List<HashSet<Tone>> tones;

    GuitarString(Tone startTone) {

        this.tones = new ArrayList<>();

        Set<Note> allNotes = EnumSet.allOf(Note.class);
        Set<Shift> allShifts = EnumSet.allOf(Shift.class);
        int octave = startTone.getKey() / 12;

        for (int i = 0; i < 12; i++) {
            this.tones.add(new HashSet<>());
            for (Note note : allNotes) {
                for (Shift shift : allShifts) {
                    for (int j = -1; j <= 1; j++) {
                        Tone tone = new Tone(note, shift, octave+j);
                        if(Tone.key2Frequency(startTone.getKey() + i) == tone.getFrequency()){
                            this.tones.get(i).add(tone);
                        }
                    }



                }
            }
        }
    }

    public Tone getRandomToneOnFret(int num){
        Set<Tone> possibleTones = this.tones.get(num);
        return new ArrayList<>(possibleTones).get(this.random.nextInt(possibleTones.size()));
    }
}
