package de.eugenbernwald.fretboardtrainer;

import de.eugenbernwald.fretboardtrainer.ui.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FretboardTrainerApp extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/notetrainer.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600);
        MainViewController controller = loader.getController();

        stage.setTitle("Fretboard Trainer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(controller::onClose);
        stage.show();
    }
}
