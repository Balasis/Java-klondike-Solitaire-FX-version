package gr.athtech.balas.klondikesolitaireminigame;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

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
    private AnchorPane containerAnchor;

    private Map<StackPane, Bounds> boundryOflastImageViewInStacks = new HashMap<>();
    private double mouseX, mouseY; // Store initial mouse coordinates

    public void initialize() {
        // Populate the map with the initial bounds after the layout pass

        Platform.runLater(this::updateStackPaneBounds);


        imageView.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            ImageView theDragger =(ImageView) event.getSource();
            VBox theDraggerParentsParent=(VBox) theDragger.getParent().getParent();
            theDraggerParentsParent.toFront();
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

        imageView.setOnMouseReleased(event -> {
            ImageView theDragger=(ImageView) event.getSource();
            // Get the bounds of imageView in the scene coordinate space
            Bounds imageViewBoundsInScene = theDragger.localToScene(theDragger.getBoundsInLocal());
            StackPane parentOfMoveable = (StackPane) theDragger.getParent();
            boolean intersected = false;
            //ok...this for each was recommended was taken by the web... I read a bit about the Map.Entry and entrySet()
            //but I am not that familiar with it... logic seems simple enough so let it be.
            for (Map.Entry<StackPane, Bounds> entry : boundryOflastImageViewInStacks.entrySet()) {
                StackPane stackPane = entry.getKey();
                Bounds bounds = entry.getValue();

                // Check for intersection using the actual scene coordinates
                if (bounds.intersects(imageViewBoundsInScene) && stackPane!=parentOfMoveable ) {
                    // Move imageView to the target stackPane
                    parentOfMoveable.getChildren().remove(theDragger);
                    stackPane.getChildren().add(theDragger);

                    // Reset translation to (0, 0) since it is now a child of the new StackPane
                    theDragger.setTranslateX(0);
                    theDragger.setTranslateY(0);


                    setMarginOnStackPaneChildrens(parentOfMoveable);
                    setMarginOnStackPaneChildrens(stackPane);

                    // Update the bounds after moving the imageView
                    Platform.runLater(this::updateStackPaneBounds);
                    intersected = true;
                    break;
                }
            }

            if (!intersected) {
                // Reset translation if not intersecting any target StackPane
                theDragger.setTranslateX(0);
                theDragger.setTranslateY(0);
            }

            mouseX = 0;
            mouseY = 0;
            event.consume();
        });
    }



    //record the bounds of the last card of each stack and reforms a full Map of them. (used for interception comparison)
    private void updateStackPaneBounds() {
        boundryOflastImageViewInStacks.clear();
        for (Node vboxNode : containerAnchor.getChildren()) {
            if (vboxNode instanceof VBox) {
                for (Node stackPaneNode : ((VBox) vboxNode).getChildren()) {
                    if (stackPaneNode instanceof StackPane) {
                        StackPane stackPane = (StackPane) stackPaneNode;
                        if (!stackPane.getChildren().isEmpty()) {
                            Node lastImageView = stackPane.getChildren().getLast();
                            Bounds bounds = lastImageView.localToScene(lastImageView.getBoundsInLocal());
                            boundryOflastImageViewInStacks.put(stackPane, bounds);
                        } else {
                            Bounds bounds = stackPane.localToScene(stackPane.getBoundsInLocal());

                            boundryOflastImageViewInStacks.put(stackPane, bounds);
                        }
                    }
                }
            }
        }

    }

    private void setMarginOnStackPaneChildrens(StackPane stackPane){
        ObservableList<Node> stacksPanelChildren=stackPane.getChildren();
        if(stacksPanelChildren.isEmpty()){
            return;
        }
        ImageView lastImageViewOfStack = (ImageView) stacksPanelChildren.getLast();
        double actualHeightOfTheLastCard = lastImageViewOfStack.getBoundsInLocal().getHeight();
        double marginPerCard = actualHeightOfTheLastCard * 0.30;
        for (int i = 0; i < stacksPanelChildren.size(); i++) {
            Node currentChild=stacksPanelChildren.get(i);
          if(currentChild instanceof  ImageView){
             // static method to apply margin!!! ok javaFX... I didn't see that coming...
                StackPane.setMargin(currentChild,new Insets(i * marginPerCard,0,0,0));
          }
        }

    }

}