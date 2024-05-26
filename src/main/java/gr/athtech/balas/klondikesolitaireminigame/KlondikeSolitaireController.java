package gr.athtech.balas.klondikesolitaireminigame;

import gr.athtech.balas.klondikesolitaireminigame.board.BoardCardsSlot;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Deck;
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


    private KlondikeSolitaireProgram theGame;
    //Map Cards to the CardViews(class which extend ImageView) that represents them.
    private Map<Card,CardView> cardsMap;
    //Map the BoardCardSlots to a StackPane
    private Map<BoardCardsSlot, StackPane> boardSlotsMap= new HashMap<>();
    //Holds the Bountry of the last Card of each StackPane(used in order to check intercept->Drag And Drop->Find desired Drop Stack)
    private final Map<StackPane, Bounds> boundryOfLastImageViewInStacks = new HashMap<>();
    //Location of the mouse related to scene, used in fake dragging process /change of translate x,y
    private double mouseX, mouseY;


    public KlondikeSolitaireController(){
        //Starting a new Game
        theGame=new KlondikeSolitaireProgram();
        //get The deck(deck is no longer in use after setting up the game;All cards get to the board)
        Deck deck=theGame.getDeck();
        //Removing the jokers from the deck since we don't need them.
        deck.removeTheJokers();
        //Map a CardView (extends ImageView) for each card of the game left(no Jokers).
        cardsMap=new HashMap<>();
        for(Card c:deck.getCards()){
        //Card view class holds only image info depending on card given, also a method to update it(hidden or revealed)
            cardsMap.put(c,new CardView(c));
        }
    }

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
            for (Map.Entry<StackPane, Bounds> entry : boundryOfLastImageViewInStacks.entrySet()) {
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
        boundryOfLastImageViewInStacks.clear();
        for (Node vboxNode : containerAnchor.getChildren()) {
            if (vboxNode instanceof VBox) {
                for (Node stackPaneNode : ((VBox) vboxNode).getChildren()) {
                    if (stackPaneNode instanceof StackPane) {
                        StackPane stackPane = (StackPane) stackPaneNode;
                        if (!stackPane.getChildren().isEmpty()) {
                            Node lastImageView = stackPane.getChildren().getLast();
                            Bounds bounds = lastImageView.localToScene(lastImageView.getBoundsInLocal());
                            boundryOfLastImageViewInStacks.put(stackPane, bounds);
                        } else {
                            Bounds bounds = stackPane.localToScene(stackPane.getBoundsInLocal());

                            boundryOfLastImageViewInStacks.put(stackPane, bounds);
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
        double marginPerCard = actualHeightOfTheLastCard * 0.25;
        for (int i = 0; i < stacksPanelChildren.size(); i++) {
            Node currentChild=stacksPanelChildren.get(i);
          if(currentChild instanceof  ImageView){
             // static method to apply margin!!! ok javaFX... I didn't see that coming...
                StackPane.setMargin(currentChild,new Insets(i * marginPerCard,0,0,0));
          }
        }

    }

}