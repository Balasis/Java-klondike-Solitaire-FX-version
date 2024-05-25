package gr.athtech.balas.klondikesolitaireminigame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class KlondikeSolitaireController {

    @FXML
    private ImageView imageView;

    @FXML
    private StackPane sourceStackPane;

    @FXML
    private StackPane targetStackPane;

    @FXML
    private HBox containerHBox;

    private Map<StackPane, Bounds> stackPaneBoundsMap = new HashMap<>();
    private double mouseX, mouseY; // Store initial mouse coordinates
    private boolean isThereADragOperation;

    public void initialize() {
        // Populate the map with the initial bounds after the layout pass
        Platform.runLater(this::updateStackPaneBounds);

        imageView.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        // Event handler for mouse dragged (during drag)
        imageView.setOnMouseDragged(event -> {
            isThereADragOperation=true;
            double offsetX = event.getSceneX() - mouseX;
            double offsetY = event.getSceneY() - mouseY;

            imageView.setTranslateX(imageView.getTranslateX()+offsetX);
            imageView.setTranslateY(imageView.getTranslateY()+offsetY);

            // Update stored mouse coordinates for the next drag event
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            event.consume();
        });

        imageView.setOnMouseReleased(event -> {
            // Get the bounds of imageView in the scene coordinate space
            Bounds imageViewBoundsInScene = imageView.localToScene(imageView.getBoundsInLocal());
            StackPane parentOfMoveable = (StackPane) imageView.getParent();
            boolean intersected = false;
            for (Map.Entry<StackPane, Bounds> entry : stackPaneBoundsMap.entrySet()) {
                StackPane stackPane = entry.getKey();
                Bounds bounds = entry.getValue();

                // Check for intersection using the actual scene coordinates
                if (bounds.intersects(imageViewBoundsInScene) && stackPane!=parentOfMoveable ) {
                    // Move imageView to the target stackPane
                    sourceStackPane.getChildren().remove(imageView);
                    stackPane.getChildren().add(imageView);

                    // Reset translation to (0, 0) since it is now a child of the new StackPane
                    imageView.setTranslateX(0);
                    imageView.setTranslateY(0);

                    // Update the bounds after moving the imageView
                    Platform.runLater(this::updateStackPaneBounds);
                    intersected = true;
                    break;
                }
            }

            if (!intersected) {
                // Reset translation if not intersecting any target StackPane
                imageView.setTranslateX(0);
                imageView.setTranslateY(0);
            }

            mouseX = 0;
            mouseY = 0;
            event.consume();
        });
    }


    private void updateStackPaneBounds() {
        stackPaneBoundsMap.clear();

        for (Node vboxNode : containerHBox.getChildren()) {
            if (vboxNode instanceof VBox) {
                for (Node stackPaneNode : ((VBox) vboxNode).getChildren()) {
                    if (stackPaneNode instanceof StackPane) {
                        StackPane stackPane = (StackPane) stackPaneNode;

                        if (!stackPane.getChildren().isEmpty()) {
                            Node lastImageView = stackPane.getChildren().get(stackPane.getChildren().size() - 1);
                            Bounds bounds = lastImageView.localToScene(lastImageView.getBoundsInLocal());
                            stackPaneBoundsMap.put(stackPane, bounds);
                            System.out.println(bounds);
                        } else {
                            Bounds bounds = stackPane.localToScene(stackPane.getBoundsInLocal());
                            System.out.println(bounds);
                            stackPaneBoundsMap.put(stackPane, bounds);
                        }
                    }
                }
            }
        }

    }


}