package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class StartMenuController {
    @FXML
    private Button startBtn;
    @FXML
    private Button quitBtn;

    @FXML
    public void initialize(){

    }

    @FXML
    private void startGameClick(ActionEvent event) throws IOException {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("sample.fxml")), 1920, 1080));
            currentStage.setFullScreen(true);
    }
    @FXML
    private void quitGameClick(ActionEvent event) throws IOException {
        Platform.exit();
    }
}
