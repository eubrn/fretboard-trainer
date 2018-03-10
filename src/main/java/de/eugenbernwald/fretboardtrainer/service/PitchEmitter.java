package de.eugenbernwald.fretboardtrainer.service;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;
import de.eugenbernwald.fretboardtrainer.model.Constants;
import javafx.beans.value.ObservableValue;

import javax.sound.sampled.*;

public class PitchEmitter {

    private Mixer.Info currentMixerInfo;

    private AudioDispatcher dispatcher;

    private AudioFormat audioFormat;
    private PitchDetectionHandler pitchDetectionHandler;

    private PitchProcessor.PitchEstimationAlgorithm currentAlgorithm;

    public PitchEmitter(Mixer.Info mixerInfo, PitchDetectionHandler handler) {
        this.currentAlgorithm = PitchProcessor.PitchEstimationAlgorithm.YIN;
        this.audioFormat = Constants.AUDIO_FORMAT;
        this.currentMixerInfo = mixerInfo;
        this.pitchDetectionHandler = handler;
        this.dispatcher = recreateDispatcher();
    }

    private AudioDispatcher recreateDispatcher() {

        Mixer mixer = AudioSystem.getMixer(currentMixerInfo);
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

        try {

            TargetDataLine line = (TargetDataLine) mixer.getLine(dataLineInfo);
            line.open(audioFormat, Constants.AUDIO_BUFFER_SIZE);
            line.start();
            AudioInputStream audioInputStream = new AudioInputStream(line);
            JVMAudioInputStream audioStream = new JVMAudioInputStream(audioInputStream);
            this.dispatcher = new AudioDispatcher(audioStream, Constants.AUDIO_BUFFER_SIZE, 0);

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        PitchProcessor processor = new PitchProcessor(
                this.currentAlgorithm,
                Constants.SAMPLE_RATE,
                Constants.AUDIO_BUFFER_SIZE,
                this.pitchDetectionHandler);

        this.dispatcher.addAudioProcessor(processor);

        new Thread(this.dispatcher).start();
        return  this.dispatcher;
    }

    public void stop(){
        this.dispatcher.stop();
    }

    public void onDeviceChange(ObservableValue<? extends Mixer.Info> observable, Mixer.Info oldValue, Mixer.Info newValue) {
        if (this.dispatcher != null) {
            this.dispatcher.stop();
        }
        this.currentMixerInfo = newValue;
        dispatcher = recreateDispatcher();
    }

    public void onAlgorithmChange(
            ObservableValue<? extends PitchProcessor.PitchEstimationAlgorithm> observable,
            PitchProcessor.PitchEstimationAlgorithm oldValue,
            PitchProcessor.PitchEstimationAlgorithm newValue) {

        this.currentAlgorithm = newValue;
        if(this.dispatcher != null){
            this.dispatcher.stop();
        }
        this.dispatcher = recreateDispatcher();
    }

}
