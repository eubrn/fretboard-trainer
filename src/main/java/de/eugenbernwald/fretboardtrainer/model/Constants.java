package de.eugenbernwald.fretboardtrainer.model;

import javax.sound.sampled.AudioFormat;

public class Constants {

    public static final AudioFormat AUDIO_FORMAT = new AudioFormat(
            48000.0f,
            16,
            1,
            true,
            true);

    public static final int AUDIO_BUFFER_SIZE = 2048;
    public static final float SAMPLE_RATE = 48000.0f;
}
