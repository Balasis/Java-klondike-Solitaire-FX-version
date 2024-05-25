package gr.athtech.balas.klondikesolitaireminigame;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class KlondikeSolitaireController {

    @FXML
    private ImageView imageView;

    @FXML
    private StackPane sourceStackPane;

    @FXML
    private StackPane targetStackPane;

    private double mouseX, mouseY; // Store initial mouse coordinates


    public void initialize() {

        // Set up drag gesture detection on the source image view
//        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent event) {
//
//                mouseX = event.getSceneX();
//                mouseY = event.getSceneY();
//                System.out.println(mouseX);
//                System.out.println(mouseY);
//                //sneaky,we get only the reference here not the creation... or actually both
//                //drag and drop actually creates the dragboard...which we here
//                Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
//
//                //so heres the thing...if there's no clipboard then there's nothing to drop
//                //later in order to trigger the drop method...and then ask for the getGestureSource
//                //which started the drag event ;...dourios ipos diladi... kai to view part ; isos
//                //in case of image...
//                ClipboardContent content = new ClipboardContent();
//                content.putString("why ... why they have to make it so hard?");
//
//                db.setContent(content);
//                dragFirst=true;
//            }
//        });
//
//        // Set up drag event handling on the target stack pane
//        targetStackPane.setOnDragOver(new EventHandler<DragEvent>() {
//            public void handle(DragEvent event) {
//               if (event.getGestureSource() != targetStackPane) {
//                    event.acceptTransferModes(TransferMode.MOVE);//enables in the target node to accept TransferMode
//               }
//                event.consume();
//            }
//        });
//
//        // Handle the drop event on the target stack pane
//        targetStackPane.setOnDragDropped(new EventHandler<DragEvent>() {
//            public void handle(DragEvent event) {
//                ImageView droppedImageView = (ImageView) event.getGestureSource();//you target the one that started the drag
//                targetStackPane.getChildren().add(droppedImageView);
////                droppedImageView.setLayoutX(event.getX());
////                droppedImageView.setLayoutY(event.getY());
//                event.setDropCompleted(true);
//                event.consume();
//            }
//        });


        imageView.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        // Event handler for mouse dragged (during drag)
        imageView.setOnMouseDragged(event -> {
            double offsetX = event.getSceneX() - mouseX;
            double offsetY = event.getSceneY() - mouseY;

            imageView.setTranslateX(imageView.getTranslateX()+offsetX);
            imageView.setTranslateY(imageView.getTranslateY()+offsetY);

            // Update stored mouse coordinates for the next drag event
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            event.consume();
        });
//a non level
//        imageView.setOnMouseReleased(event -> {
//
//
//            if (imageView.getBoundsInParent().intersects(targetStackPane.getBoundsInParent())) {
//
//
//                // Move the imageView to the targetStackPane
//                sourceStackPane.getChildren().remove(imageView);
//                targetStackPane.getChildren().add(imageView);
//
//                // Reset translation to (0, 0) since it is now a child of targetStackPane
//                imageView.setTranslateX(0);
//                imageView.setTranslateY(0);
//            } else {
//                // Reset translation to (0, 0) if not intersecting (optional)
//                imageView.setTranslateX(0);
//                imageView.setTranslateY(0);
//            }
//            // Reset stored mouse coordinates
//            mouseX = 0;
//            mouseY = 0;
//            event.consume();
//        });
        //a level over...
        imageView.setOnMouseReleased(event -> {
            // Calculate the final position of imageView after dragging based on translation
            Bounds targetBoundsRelativeToVBox = targetStackPane.getBoundsInParent();
            double targetXRelativeToVBox = targetBoundsRelativeToVBox.getMinX() + targetStackPane.getParent().getLayoutX();
            double targetYRelativeToVBox = targetBoundsRelativeToVBox.getMinY() + targetStackPane.getParent().getLayoutY();

            if (imageView.getBoundsInParent().intersects(targetXRelativeToVBox, targetYRelativeToVBox, targetBoundsRelativeToVBox.getWidth(), targetBoundsRelativeToVBox.getHeight())) {
                // Remove imageView from sourceStackPane and add it to targetStackPane
                sourceStackPane.getChildren().remove(imageView);
                targetStackPane.getChildren().add(imageView);

                // Reset translation to (0, 0) since it is now a child of targetStackPane
                imageView.setTranslateX(0);
                imageView.setTranslateY(0);
            } else {
                // Reset translation to (0, 0) if not intersecting (optional)
                imageView.setTranslateX(0);
                imageView.setTranslateY(0);
            }
        });




    }


}