package de.eugenbernwald.fretboardtrainer.ui;

import be.tarsos.dsp.pitch.PitchProcessor;
import de.eugenbernwald.fretboardtrainer.model.*;
import de.eugenbernwald.fretboardtrainer.service.ToneEvaluator;
import de.eugenbernwald.fretboardtrainer.service.PitchEmitter;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainViewController {

    private Random random;

    @FXML
    private ChoiceBox<Tuning> tuningChoiceBox;

    @FXML
    private ChoiceBox<Mixer.Info> deviceChoiceBox;

    @FXML
    private ChoiceBox<PitchProcessor.PitchEstimationAlgorithm> algorithmChoiceBox;

    @FXML
    private Label toneLabel;

    @FXML
    private Label stringNumberLabel;

    @FXML
    private Pane colorAnimationPane;

    @FXML
    private BorderPane windowPane;

    @FXML
    private Label scoreLabel;

    private PitchEmitter pitchEmitter;

    private ToneEvaluator toneEvaluator;

    private Color currentColor = Colors.getNextColor(null);

    private Mixer.Info initialMixerInfo;

    private int stringIndex;

    private int score;

    public MainViewController() {
        this.random = new Random();

        Tuning tuning = Tuning.STANDARD_E;

        this.stringIndex = this.random.nextInt(tuning.numStrings());
        GuitarString guitarString = tuning.getString(stringIndex);
        Tone tone = guitarString.getRandomToneOnFret(random.nextInt(12));
        this.toneEvaluator = new ToneEvaluator(tone, this::replaceTone);

        this.initialMixerInfo = Stream.of(AudioSystem.getMixerInfo())
                .map(AudioSystem::getMixer)
                .filter(m -> m.isLineSupported(new DataLine.Info(DataLine.class, Constants.AUDIO_FORMAT)))
                .map(Mixer::getMixerInfo)
                .findAny().orElse(null);

        this.pitchEmitter = new PitchEmitter(initialMixerInfo, this.toneEvaluator);
    }



    public void initialize(){

        List<Mixer.Info> supportedMixers =  Stream.of(AudioSystem.getMixerInfo())
                .map(AudioSystem::getMixer)
                .filter(m -> m.isLineSupported(new DataLine.Info(DataLine.class, Constants.AUDIO_FORMAT)))
                .map(Mixer::getMixerInfo)
                .collect(Collectors.toList());

        ObservableList<Mixer.Info> infos = FXCollections.observableList(supportedMixers);

        this.windowPane.setBackground(Colors.toBackground(this.currentColor));

        this.tuningChoiceBox.setItems(FXCollections.observableArrayList(Tuning.values()));
        this.tuningChoiceBox.setValue(Tuning.STANDARD_E);
        this.tuningChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::onTuningChanged);

        this.deviceChoiceBox.setItems(infos);
        this.deviceChoiceBox.setValue(this.initialMixerInfo);
        this.deviceChoiceBox.setConverter(new MixerInfoStringConverter());
        this.deviceChoiceBox.getSelectionModel().selectedItemProperty().addListener(this.pitchEmitter::onDeviceChange);

        this.algorithmChoiceBox.setItems(FXCollections.observableList(Arrays.asList(PitchProcessor.PitchEstimationAlgorithm.values())));
        this.algorithmChoiceBox.setValue(PitchProcessor.PitchEstimationAlgorithm.YIN);
        this.algorithmChoiceBox.getSelectionModel().selectedItemProperty().addListener(this.pitchEmitter::onAlgorithmChange);

        this.toneLabel.setText(this.toneEvaluator.getTone().toString());
        this.stringNumberLabel.setText(String.format("on String %d", this.stringIndex + 1));

        this.colorAnimationPane.setBackground(Colors.toBackground(Colors.getNextColor(this.currentColor)));
        this.colorAnimationPane.prefWidthProperty().bind(this.toneEvaluator.getProgress().multiply(800));

        this.toneEvaluator.start();
    }



    private void onTuningChanged(ObservableValue<? extends Tuning> observable, Tuning oldValue, Tuning newValue) {
        this.replaceTone();
    }



    public void onClose(WindowEvent event) {
        this.pitchEmitter.stop();
    }



    private void replaceTone(){

        playSuccessSound();
        Tuning tuning = this.tuningChoiceBox.getValue();

        this.score++;
        this.scoreLabel.setText(String.format("Score: %d", this.score));

        this.stringIndex = this.random.nextInt(tuning.numStrings());
        GuitarString guitarString = tuning.getString(this.stringIndex);
        Tone newTone = guitarString.getRandomToneOnFret(this.random.nextInt(12));
        this.toneEvaluator.setTone(newTone);

        this.toneLabel.setText(newTone.toString());
        this.stringNumberLabel.setText(String.format("on String %d", this.stringIndex + 1));

        this.windowPane.setBackground(this.colorAnimationPane.getBackground());
        this.currentColor = Colors.getNextColor(this.currentColor);
        this.colorAnimationPane.setBackground(Colors.toBackground(this.currentColor));
    }


    private void playSuccessSound(){
        Optional.ofNullable(getClass().getClassLoader().getResource("sounds/bell.wav"))
                .map(Object::toString)
                .map(Media::new)
                .map(MediaPlayer::new)
                .ifPresent(MediaPlayer::play);
    }
}
