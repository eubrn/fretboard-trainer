package de.eugenbernwald.fretboardtrainer

import de.eugenbernwald.fretboardtrainer.ui.MainViewController
import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.WindowEvent

class FretboardTrainerApp : Application() {

    override fun start(stage: Stage) {
        val loader = FXMLLoader(javaClass.classLoader.getResource("view/notetrainer.fxml"))
        val root = loader.load<Parent>()

        val scene = Scene(root, 800.0, 600.0)
        val controller = loader.getController<MainViewController>()

        stage.title = "Fretboard Trainer"
        stage.scene = scene
        stage.isResizable = false
        stage.onCloseRequest = EventHandler<WindowEvent> { controller.onClose() }
        stage.show()
    }
}
