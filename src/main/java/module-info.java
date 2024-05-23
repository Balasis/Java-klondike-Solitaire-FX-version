module gr.athtech.balas.klondikesolitaireminigame {
    requires javafx.controls;
    requires javafx.fxml;


    opens gr.athtech.balas.klondikesolitaireminigame to javafx.fxml;
    exports gr.athtech.balas.klondikesolitaireminigame;
}