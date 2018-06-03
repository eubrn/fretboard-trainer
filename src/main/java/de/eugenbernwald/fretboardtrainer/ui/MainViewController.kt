package de.eugenbernwald.fretboardtrainer.ui

import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm
import de.eugenbernwald.fretboardtrainer.model.Constants
import de.eugenbernwald.fretboardtrainer.model.Tuning
import de.eugenbernwald.fretboardtrainer.service.PitchEmitter
import de.eugenbernwald.fretboardtrainer.service.ToneEvaluator
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import java.net.URL
import java.util.*
import java.util.stream.Stream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.Mixer.Info
import kotlin.streams.toList

class MainViewController : Initializable {

    private val random: Random = Random()

    @FXML
    private var tuningChoiceBox: ChoiceBox<Tuning>? = null

    @FXML
    private var deviceChoiceBox: ChoiceBox<Info>? = null

    @FXML
    private var algorithmChoiceBox: ChoiceBox<PitchEstimationAlgorithm>? = null

    @FXML
    private var toneLabel: Label? = null

    @FXML
    private var stringNumberLabel: Label? = null

    @FXML
    private var colorAnimationPane: Pane? = null

    @FXML
    private var windowPane: BorderPane? = null

    @FXML
    private var scoreLabel: Label? = null

    private val pitchEmitter: PitchEmitter

    private val toneEvaluator: ToneEvaluator

    private var currentColor: Color? = null

    private val initialMixerInfo: Info

    private var stringIndex: Int = 0

    private var score: Int = 0
    
    init {

        val tuning = Tuning.STANDARD_E

        currentColor = Colors.getNextColor(null)

        stringIndex = random.nextInt(tuning.numStrings())
        val guitarString = tuning.getString(stringIndex)
        val tone = guitarString.getRandomToneOnFret(random.nextInt(12))
        toneEvaluator = ToneEvaluator(tone, { replaceTone() })

        initialMixerInfo = Stream.of<Info>(*AudioSystem.getMixerInfo())
                .map { AudioSystem.getMixer(it) }
                .filter { m -> m.isLineSupported(DataLine.Info(DataLine::class.java, Constants.AUDIO_FORMAT)) }
                .map { it.mixerInfo }
                .findAny().orElse(null)

        pitchEmitter = PitchEmitter(initialMixerInfo, toneEvaluator)
    }


    override fun initialize(location: URL?, resources: ResourceBundle?) {

        val supportedMixers = Stream.of<Info>(*AudioSystem.getMixerInfo())
                .map { AudioSystem.getMixer(it) }
                .filter { m -> m.isLineSupported(DataLine.Info(DataLine::class.java, Constants.AUDIO_FORMAT)) }
                .map { it.mixerInfo }
                .toList()

        val infos = FXCollections.observableList<Info>(supportedMixers)

        windowPane?.background = Colors.toBackground(currentColor!!)
        toneLabel?.text = toneEvaluator.tone.toString()
        stringNumberLabel?.text = String.format("on String %d", stringIndex + 1)

        colorAnimationPane?.let {
            it.background = Colors.toBackground(Colors.getNextColor(currentColor))
            it.prefWidthProperty().bind(toneEvaluator.progress.multiply(800))
        }

        tuningChoiceBox?.let {
            it.items = FXCollections.observableArrayList(*Tuning.values())
            it.value = Tuning.STANDARD_E
            it.selectionModel.selectedItemProperty().addListener({ _, _, _ -> onTuningChanged() })
        }

        deviceChoiceBox?.let {
            it.items = infos
            it.value = initialMixerInfo
            it.converter = MixerInfoStringConverter()
            it.selectionModel.selectedItemProperty().addListener { _, _, newValue -> pitchEmitter.onDeviceChange(newValue) }
        }

        algorithmChoiceBox?.let {
            it.items = FXCollections.observableList(Arrays.asList(*PitchEstimationAlgorithm.values()))
            it.value = PitchEstimationAlgorithm.YIN
            it.selectionModel.selectedItemProperty().addListener({ _, _, newValue -> pitchEmitter.onAlgorithmChange(newValue) })
        }

        toneEvaluator.start()
    }

    private fun onTuningChanged() {
        replaceTone()
    }

    fun onClose() {
        pitchEmitter.stop()
    }

    private fun replaceTone() {

        playSuccessSound()
        val tuning = tuningChoiceBox?.value!!

        score++
        scoreLabel?.text = "Score: $score"

        stringIndex = random.nextInt(tuning.numStrings())
        val guitarString = tuning.getString(stringIndex)
        val newTone = guitarString.getRandomToneOnFret(random.nextInt(12))
        toneEvaluator.tone = newTone

        toneLabel?.text = newTone.toString()
        stringNumberLabel?.text = "on String ${stringIndex+1}"

        windowPane?.background = colorAnimationPane?.background
        currentColor = Colors.getNextColor(currentColor)
        colorAnimationPane?.background = Colors.toBackground(currentColor!!)
    }

    private fun playSuccessSound() {
        Optional.ofNullable(javaClass.classLoader.getResource("sounds/bell.wav"))
                .map { it.toString() }
                .map { Media(it) }
                .map { MediaPlayer(it) }
                .ifPresent({it.play()})
    }
}
