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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;

public class KlondikeSolitaireController {
    //Card Slots
    @FXML private StackPane deckSlot;
    @FXML private StackPane wasteSlot;
    @FXML private StackPane clubsFoundation;
    @FXML private StackPane diamondsFoundation;
    @FXML private StackPane heartsFoundation;
    @FXML private StackPane spadesFoundation;
    @FXML private StackPane tableSlot1;
    @FXML private StackPane tableSlot2;
    @FXML private StackPane tableSlot3;
    @FXML private StackPane tableSlot4;
    @FXML private StackPane tableSlot5;
    @FXML private StackPane tableSlot6;
    @FXML private StackPane tableSlot7;
    @FXML private AnchorPane containerAnchor;
    //+52 Dynamically created ImageViews, as superclass of CardViews.(Cardviews extend ImageView)
    //Each CardView has a "Card"(model class) field to have info on updating its image.

    //Main game program, Maps to map view with model, mouse X|Y for drag and drop info, an array for loop convenience
    private KlondikeSolitaireProgram theGame;
    private final Map<Card, CardView> cardsMap;
    private final Map<BoardCardsSlot, StackPane> boardSlotsMap = new HashMap<>();
    private final Map<StackPane, Bounds> boundryOfLastImageViewInStacks = new HashMap<>();
    private double mouseX, mouseY;
    StackPane[] tablesArray = {tableSlot1, tableSlot2, tableSlot3, tableSlot4, tableSlot5, tableSlot6, tableSlot7};

    public KlondikeSolitaireController() {
        //Init the gameProgram, get cardDeck, removing the jokers cards, populate cardMaps.
        theGame = new KlondikeSolitaireProgram();
        Deck deck = theGame.getDeck();
        deck.removeTheJokers();
        cardsMap = new HashMap<>();
        for (Card c : deck.getCards()) {
            cardsMap.put(c, new CardView(c));
        }
    }

    public void initialize() {
        //populate boardSlotsMap,adding mouse listeners for drag and drop operations,
        //adding a Bountry to be intercepted at each StackPane using the last childs(CardView) Bountry of it
        populateTheBoardSlotMap();
        addListenersToCardViews();
        Platform.runLater(this::updateStackPaneBounds);
    }

    private void populateTheBoardSlotMap(){
        //sadly couldn't think of a shortcut or a loop due to many differences
        boardSlotsMap.put(theGame.getDeckSlot(), deckSlot);
        boardSlotsMap.put(theGame.getWasteSlot(), wasteSlot);
        boardSlotsMap.put(theGame.getClubsFoundationSlot(), clubsFoundation);
        boardSlotsMap.put(theGame.getDiamondsFoundationSlot(), diamondsFoundation);
        boardSlotsMap.put(theGame.getHeartsFoundationSlot(), heartsFoundation);
        boardSlotsMap.put(theGame.getSpadesFoundationSlot(), spadesFoundation);
        for (int i = 0; i < tablesArray.length; i++) {
            //tableSlots are also an array in model from 1 to 7
            boardSlotsMap.put(theGame.getATableSlot(i), tablesArray[i]);
        }
    }

    private void addListenersToCardViews(){
        for(Map.Entry<Card,CardView> c:cardsMap.entrySet()){
            CardView cV=c.getValue();
            addMouseListenersForDragAndDrop(cV);
        }
    }

    private void addMouseListenersForDragAndDrop(CardView cV){
        addSetOnMouseClickListener(cV);
        addSetOnMouseDraggedListener(cV);
        addSetOnMouseReleased(cV);
    }

    //Click on card...
    private void addSetOnMouseClickListener(CardView cV){
        cV.setOnMousePressed(event -> {
            //getMouseLocation of click to set the starting point of drag(used later for translate x,y)
            setMouseCurrentLocation(event);
            //changing the Z-order of Vbox (Anchor>Vbox>StackPane>CardView)  so the potential drag item(ImageView)
            // won't be hidden by other stackpane. Used anchor so they won't affect view(absolute position)
            cardViewParentOfParentInFront(event);
        });
    }

    private void setMouseCurrentLocation(MouseEvent e){
        mouseX = e.getSceneX();
        mouseY = e.getSceneY();
    }

    private void cardViewParentOfParentInFront(MouseEvent e){
        ImageView theDragger = (ImageView) e.getSource();
        VBox theDraggerParentsParent = (VBox) theDragger.getParent().getParent();
        theDraggerParentsParent.toFront();
    }

    //...start moving mouse while holding click (drag)
    private void addSetOnMouseDraggedListener(CardView cV){
        cV.setOnMouseDragged(event -> {
            double offsetX = event.getSceneX() - mouseX;
            double offsetY = event.getSceneY() - mouseY;

            cV.setTranslateX(cV.getTranslateX() + offsetX);
            cV.setTranslateY(cV.getTranslateY() + offsetY);

            // Update stored mouse coordinates for the next drag event
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            event.consume();
        });
    }

    //...release the mouse
    private void addSetOnMouseReleased(ImageView cV){
        cV.setOnMouseReleased(event -> {
            ImageView theDragger = (ImageView) event.getSource();
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
                if (bounds.intersects(imageViewBoundsInScene) && stackPane != parentOfMoveable) {
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

    private void setMarginOnStackPaneChildrens(StackPane stackPane) {
        ObservableList<Node> stacksPanelChildren = stackPane.getChildren();
        if (stacksPanelChildren.isEmpty()) {
            return;
        }
        ImageView lastImageViewOfStack = (ImageView) stacksPanelChildren.getLast();
        double actualHeightOfTheLastCard = lastImageViewOfStack.getBoundsInLocal().getHeight();
        double marginPerCard = actualHeightOfTheLastCard * 0.25;
        for (int i = 0; i < stacksPanelChildren.size(); i++) {
            Node currentChild = stacksPanelChildren.get(i);
            if (currentChild instanceof ImageView) {
                // static method to apply margin!!! ok javaFX... I didn't see that coming...
                StackPane.setMargin(currentChild, new Insets(i * marginPerCard, 0, 0, 0));
            }
        }

    }

    private void updateCardRevealsStatus() {
        for (Map.Entry<Card, CardView> e : cardsMap.entrySet()) {
            CardView cardView = e.getValue();
            cardView.updateImage();
        }
    }


}

