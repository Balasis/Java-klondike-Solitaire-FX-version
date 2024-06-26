package gr.athtech.balas.klondikesolitaireminigame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class KlondikeSolitaireApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(KlondikeSolitaireApp.class.getResource("klondikeSolitaireView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 800);
        stage.setTitle("Klondike Solitaire by John Balasis!");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/forIco.png"))));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}