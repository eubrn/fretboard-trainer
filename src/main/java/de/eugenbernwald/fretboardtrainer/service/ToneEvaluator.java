package de.eugenbernwald.fretboardtrainer.service;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import de.eugenbernwald.fretboardtrainer.model.Tone;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ToneEvaluator extends AnimationTimer implements PitchDetectionHandler {

    private float currentPitch;

    private Tone tone;
    private DoubleProperty progress;
    private CorrectToneListener  correctToneListener;

    public ToneEvaluator(Tone tone, CorrectToneListener listener) {
        this.currentPitch = 0.0f;
        this.tone = tone;
        this.progress = new SimpleDoubleProperty(0.0);
        this.correctToneListener = listener;
    }

    public DoubleProperty getProgress() {
        return this.progress;
    }

    public Tone getTone() {
        return this.tone;
    }

    public void setTone(Tone tone) {
        this.tone = tone;
    }

    @Override
    public void handle(long now) {
        if(this.currentPitch < tone.getUpperLimit() && this.currentPitch > tone.getLowerLimit()){
            hit();
        }
        else {
            miss();
        }
    }

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        this.currentPitch = pitchDetectionResult.getPitch();
    }

    private void hit(){
        progress.setValue(progress.get() + (1.0/50));

        if(progress.get() >= 1.0){
            progress.setValue(0);
            correctToneListener.onCorrectTone();
        }

    }

    private void miss() {

        if(progress.get() > 0){
            progress.setValue(progress.get() - (1.0/50));
        }
        else {
            progress.setValue(0);
        }
    }
}
