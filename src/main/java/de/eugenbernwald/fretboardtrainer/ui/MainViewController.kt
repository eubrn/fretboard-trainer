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
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.Mixer.Info

class MainViewController : Initializable {

    private val random: Random = Random()

    @FXML
    private lateinit var tuningChoiceBox: ChoiceBox<Tuning>

    @FXML
    private lateinit var deviceChoiceBox: ChoiceBox<Info>

    @FXML
    private lateinit var algorithmChoiceBox: ChoiceBox<PitchEstimationAlgorithm>

    @FXML
    private lateinit var toneLabel: Label

    @FXML
    private lateinit var stringNumberLabel: Label

    @FXML
    private lateinit var colorAnimationPane: Pane

    @FXML
    private lateinit var windowPane: BorderPane

    @FXML
    private lateinit var scoreLabel: Label

    private val pitchEmitter: PitchEmitter

    private val toneEvaluator: ToneEvaluator

    private var currentColor: Color

    private val initialMixerInfo: Info

    private var stringIndex: Int = 0

    private var score: Int = 0
    
    init {

        val tuning = Tuning.STANDARD_E

        currentColor = Colors.getNextColor(null)

        stringIndex = random.nextInt(tuning.numStrings())
        val guitarString = tuning.getString(stringIndex)
        val tone = guitarString.getRandomToneOnFret(random.nextInt(12))
        toneEvaluator = ToneEvaluator(tone, { win() })

        initialMixerInfo =
                AudioSystem.getMixerInfo()
                .map { AudioSystem.getMixer(it) }
                .filter { m -> m.isLineSupported(DataLine.Info(DataLine::class.java, Constants.AUDIO_FORMAT)) }
                .map { it.mixerInfo }.first()

        pitchEmitter = PitchEmitter(initialMixerInfo, toneEvaluator)
    }


    override fun initialize(location: URL?, resources: ResourceBundle?) {

        val supportedMixers = AudioSystem.getMixerInfo()
                .map { AudioSystem.getMixer(it) }
                .filter { m -> m.isLineSupported(DataLine.Info(DataLine::class.java, Constants.AUDIO_FORMAT)) }
                .map { it.mixerInfo }
                .toList()

        val infos = FXCollections.observableList<Info>(supportedMixers)

        windowPane.background = Colors.toBackground(currentColor)
        toneLabel.text = toneEvaluator.tone.toString()
        stringNumberLabel.text = String.format("on String %d", stringIndex + 1)

        colorAnimationPane.let {
            it.background = Colors.toBackground(Colors.getNextColor(currentColor))
            it.prefWidthProperty().bind(toneEvaluator.progress.multiply(800))
        }

        tuningChoiceBox.let {
            it.items = FXCollections.observableArrayList(*Tuning.values())
            it.value = Tuning.STANDARD_E
            it.selectionModel.selectedItemProperty().addListener({ _, _, _ -> onTuningChanged() })
        }

        deviceChoiceBox.let {
            it.items = infos
            it.value = initialMixerInfo
            it.converter = MixerInfoStringConverter()
            it.selectionModel.selectedItemProperty().addListener { _, _, newValue -> pitchEmitter.onDeviceChange(newValue) }
        }

        algorithmChoiceBox.let {
            it.items = FXCollections.observableList(Arrays.asList(*PitchEstimationAlgorithm.values()))
            it.value = PitchEstimationAlgorithm.YIN
            it.selectionModel.selectedItemProperty().addListener({ _, _, newValue -> pitchEmitter.onAlgorithmChange(newValue) })
        }

        toneEvaluator.start()
    }

    private fun onTuningChanged() {
        replaceTone()
    }

    private fun win(){
        score++
        scoreLabel.text = "Score: $score"
        playSuccessSound()
        replaceTone()
    }

    fun onClose() {
        pitchEmitter.stop()
    }

    private fun replaceTone() {


        val tuning = tuningChoiceBox.value

        stringIndex = random.nextInt(tuning.numStrings())
        val guitarString = tuning.getString(stringIndex)
        val newTone = guitarString.getRandomToneOnFret(random.nextInt(12))
        toneEvaluator.tone = newTone

        toneLabel.text = newTone.toString()
        stringNumberLabel.text = "on String ${stringIndex+1}"

        windowPane.background = colorAnimationPane.background
        currentColor = Colors.getNextColor(currentColor)
        colorAnimationPane.background = Colors.toBackground(currentColor)
    }

    private fun playSuccessSound() {
        Optional.ofNullable(javaClass.classLoader.getResource("sounds/bell.wav"))
                .map { it.toString() }
                .map { Media(it) }
                .map { MediaPlayer(it) }
                .ifPresent({it.play()})
    }
}
