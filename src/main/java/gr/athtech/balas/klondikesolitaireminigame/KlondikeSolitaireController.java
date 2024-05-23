package gr.athtech.balas.klondikesolitaireminigame;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KlondikeSolitaireController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}