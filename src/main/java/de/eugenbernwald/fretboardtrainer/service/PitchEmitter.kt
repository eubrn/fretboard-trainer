package de.eugenbernwald.fretboardtrainer.service

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.jvm.JVMAudioInputStream
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import de.eugenbernwald.fretboardtrainer.model.Constants

import javax.sound.sampled.*

class PitchEmitter(private var currentMixerInfo: Mixer.Info?, private val pitchDetectionHandler: PitchDetectionHandler) {

    private var dispatcher: AudioDispatcher

    private val audioFormat: AudioFormat

    private var currentAlgorithm: PitchProcessor.PitchEstimationAlgorithm

    init {
        currentAlgorithm = PitchProcessor.PitchEstimationAlgorithm.YIN
        audioFormat = Constants.AUDIO_FORMAT
        dispatcher = recreateDispatcher()
    }

    private fun recreateDispatcher(): AudioDispatcher {

        val mixer = AudioSystem.getMixer(currentMixerInfo)
        val dataLineInfo = DataLine.Info(TargetDataLine::class.java, audioFormat)

        try {

            val line = mixer.getLine(dataLineInfo) as TargetDataLine
            line.open(audioFormat, Constants.AUDIO_BUFFER_SIZE)
            line.start()
            val audioInputStream = AudioInputStream(line)
            val audioStream = JVMAudioInputStream(audioInputStream)
            dispatcher = AudioDispatcher(audioStream, Constants.AUDIO_BUFFER_SIZE, 0)

        } catch (e: LineUnavailableException) {
            e.printStackTrace()
        }

        val processor = PitchProcessor(
                currentAlgorithm,
                Constants.SAMPLE_RATE,
                Constants.AUDIO_BUFFER_SIZE,
                pitchDetectionHandler)

        dispatcher.addAudioProcessor(processor)

        Thread(dispatcher).start()
        return dispatcher
    }

    fun stop() {
        dispatcher.stop()
    }

    fun onDeviceChange(newValue: Mixer.Info) {
        dispatcher.stop()
        currentMixerInfo = newValue
        dispatcher = recreateDispatcher()
    }

    fun onAlgorithmChange(newValue: PitchProcessor.PitchEstimationAlgorithm) {

        currentAlgorithm = newValue
        dispatcher.stop()
        dispatcher = recreateDispatcher()
    }

}
