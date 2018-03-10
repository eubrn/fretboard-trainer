package de.eugenbernwald.fretboardtrainer.ui;

import javafx.util.StringConverter;

import javax.sound.sampled.Mixer;

public class MixerInfoStringConverter extends StringConverter<Mixer.Info> {

    @Override
    public String toString(Mixer.Info info) {
        return info.getName();
    }

    @Override
    public Mixer.Info fromString(String string) {
        return null;
    }
}
