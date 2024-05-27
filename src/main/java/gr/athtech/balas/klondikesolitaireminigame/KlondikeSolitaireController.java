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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KlondikeSolitaireController {
    //Card Slots
    @FXML
    private StackPane deckSlot;
    @FXML
    private StackPane wasteSlot;
    @FXML
    private StackPane clubsFoundation;
    @FXML
    private StackPane diamondsFoundation;
    @FXML
    private StackPane heartsFoundation;
    @FXML
    private StackPane spadesFoundation;
    @FXML
    private StackPane tableSlot1;
    @FXML
    private StackPane tableSlot2;
    @FXML
    private StackPane tableSlot3;
    @FXML
    private StackPane tableSlot4;
    @FXML
    private StackPane tableSlot5;
    @FXML
    private StackPane tableSlot6;
    @FXML
    private StackPane tableSlot7;
    @FXML
    private AnchorPane containerAnchor;
    //+52 Dynamically created ImageViews, as superclasses of CardViews.( Cardview extends ImageView)

    //Each CardView has a "Card"(model class) field to have info on updating its image.

    //Main game program, Maps to for info from model to view, mouse X|Y for drag and drop info, an array for loop convenience.
    private KlondikeSolitaireProgram theGame;
    private final Map<Card, CardView> cardsMap;
    private final Map<StackPane,BoardCardsSlot > boardSlotsMap = new HashMap<>();
    private final Map<StackPane, Bounds> boundryOfLastImageViewInStacks = new HashMap<>();
    private double mouseX, mouseY;
    private ArrayList<StackPane> tableStacks;
    int numberOfCardsWeTryToPick=0;
    StackPane stackPaneChosenAsSource;
    boolean isThereADraggingGoingOn;


    public KlondikeSolitaireController() {
        //Init the gameProgram, get cards from a deck, removing the jokers cards, populate "cardMaps".
        theGame = new KlondikeSolitaireProgram();
        Deck deck = theGame.getDeck();
        deck.removeTheJokers();
        cardsMap = new HashMap<>();
        for (Card c : deck.getCards()) {
            CardView cV=new CardView(c);
            cV.setFitWidth(95);
            cV.setFitHeight(150);
            cV.setPreserveRatio(false);
            cardsMap.put(c, new CardView(c));
        }
    }

    public void initialize() {
        tableStacks = new ArrayList<>(Arrays.asList(tableSlot1, tableSlot2, tableSlot3, tableSlot4, tableSlot5, tableSlot6, tableSlot7));
        //populate "boardSlotsMap", adding mouse listeners for drag and drop operations,
        //adding a Bountry to be intercepted at each StackPane using the last childs(CardView) Bountry of it
        //in order to get the destination(drop) StackPane
        populateTheBoardSlotMap();

        theGame.setUpTheGame();


        updateStackPaneStatus();
        addListenersToCardViews();
        setMarginToAllTables();
        updateCardRevealsStatus();
        Platform.runLater(this::updateStackPaneBounds);
    }

    private void setMarginToAllTables(){
        for(StackPane s:tableStacks){
            setMarginOnStackPaneChildrens(s);
        }

    }

    private void populateTheBoardSlotMap(){
        //(sadly couldn't think of a shortcut or a loop due to many differences)
        //population of "boardSlotMap". Maps BoardCardsSlot to StackPane elements.
        boardSlotsMap.put(deckSlot,theGame.getDeckSlot() );
        boardSlotsMap.put(wasteSlot,theGame.getWasteSlot() );
        boardSlotsMap.put(clubsFoundation,theGame.getClubsFoundationSlot() );
        boardSlotsMap.put(diamondsFoundation,theGame.getDiamondsFoundationSlot());
        boardSlotsMap.put( heartsFoundation,theGame.getHeartsFoundationSlot());
        boardSlotsMap.put(spadesFoundation,theGame.getSpadesFoundationSlot());
        for (int i = 0; i < tableStacks.size(); i++) {
            boardSlotsMap.put(tableStacks.get(i) ,theGame.getATableSlot(i));
        }
    }

    private void addListenersToCardViews(){
        for(Map.Entry<Card,CardView> c:cardsMap.entrySet()){
            CardView cV=c.getValue();
            if(cV.getParent().getId().equals("deckSlot")){
                addMouseListenerForDeckToWaste(cV);
            }else{
                addMouseListenersForDragAndDrop(cV);
            }

        }
    }

    private void addMouseListenerForDeckToWaste(CardView cV){
        cV.setOnMousePressed(event -> {
            StackPane parentOfcV=(StackPane) cV.getParent();
            theGame.addCardFromDeckToWaste(1);
            parentOfcV.getChildren().remove(cV);
            wasteSlot.getChildren().add(cV);
            cV.updateImage();
            updateCardRevealsStatus();
            event.consume();
        });
    }

    private void addMouseListenersForDragAndDrop(CardView cV){
        addSetOnMouseClickListener(cV);
        addSetOnMouseDraggedListener(cV);
        addSetOnMouseReleased(cV);//checks inter
    }

    //gets x|y mouse location ,change z-order at Parent of parent so the upcoming
    //"dragged" element won't get hidden by z-order issues.
    // (Required AnchorPane use because z-order would change the position of it)
    //(AnchorPane->Vbox->StackPane->CardView)
    private void addSetOnMouseClickListener(CardView cV){
        cV.setOnMousePressed(event -> {
            numberOfCardsWeTryToPick =getNumberOfCardViewsUnderIt(cV);
            stackPaneChosenAsSource =(StackPane) cV.getParent();
            isThereADraggingGoingOn=false;
            setMouseCurrentLocation(event);
            cardViewParentOfParentInFront(event);
            event.consume();
        });
    }

    private int getNumberOfCardViewsUnderIt(CardView cV){
        StackPane theParent=(StackPane) cV.getParent();
        return  theParent.getChildren().size()-theParent.getChildren().indexOf(cV);
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

    //update and sum to x|y mouse loc, changes translate x|y (fake drag)
    private void addSetOnMouseDraggedListener(CardView cV){
        cV.setOnMouseDragged(event -> {
            if (stackPaneChosenAsSource==null || numberOfCardsWeTryToPick==0){
                System.out.println("no card dragged");
                event.consume();
                return;
            }
            if (!isThereADraggingGoingOn) {
                System.out.println("no dragging going on");
                theGame.dragCardsFromBoardCardSlot(boardSlotsMap.get(stackPaneChosenAsSource), numberOfCardsWeTryToPick);
            }
            isThereADraggingGoingOn=true;
                if (theGame.isThereDragCards()){
                    updateTranslateXYtoCardView(event);
                    // Update stored mouse coordinates for the next drag event
                    setMouseCurrentLocation(event);
                }
            event.consume();
        });
    }

    private void updateTranslateXYtoCardView(MouseEvent e){
        CardView cV=(CardView) e.getSource();
        double offsetX = e.getSceneX() - mouseX;
        double offsetY = e.getSceneY() - mouseY;
        cV.setTranslateX(cV.getTranslateX() + offsetX);
        cV.setTranslateY(cV.getTranslateY() + offsetY);
    }

    //...release the mouse
    private void addSetOnMouseReleased(CardView cV){
        cV.setOnMouseReleased(event -> {
            updateStackPaneBounds();
            CardView theDragger = (CardView) event.getSource();
            Bounds imageViewBoundsInScene = theDragger.localToScene(theDragger.getBoundsInLocal());
            StackPane parentOfMoveable = (StackPane) theDragger.getParent();

            for (Map.Entry<StackPane, Bounds> entry : boundryOfLastImageViewInStacks.entrySet()) {
                StackPane stackPane = entry.getKey();
                Bounds bounds = entry.getValue();

                // Check for intersection using the actual scene coordinates
                if (bounds.intersects(imageViewBoundsInScene) && stackPane != parentOfMoveable) {
                    if(!theGame.addCardsToBoardSlot( boardSlotsMap.get(stackPane) )){
                        System.out.println("wasn't Approved");
                        break;
                    }
                    moveImageViewsBetweenPanels(parentOfMoveable,stackPane,theDragger);
                    setMarginsOnAffectedPanels(parentOfMoveable,stackPane);
                    break;
                }
            }
            resetDraggingProperties(theDragger);
            updateBoardProperties();
            event.consume();
        });
    }

    private void moveImageViewsBetweenPanels(StackPane source,StackPane target,ImageView imageView){
        source.getChildren().remove(imageView);
        target.getChildren().add(imageView);
    }

    private void setMarginsOnAffectedPanels(StackPane source,StackPane target){
        setMarginOnStackPaneChildrens(source);
        setMarginOnStackPaneChildrens(target);
    }

    private void resetDraggingProperties(CardView theDragger){
        mouseX = 0;
        mouseY = 0;
        numberOfCardsWeTryToPick=0;
        stackPaneChosenAsSource=null;
        isThereADraggingGoingOn =true;
        theDragger.setTranslateX(0);
        theDragger.setTranslateY(0);
    }

    private void updateBoardProperties(){
        updateStackPaneBounds();
        updateCardRevealsStatus();
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
//                            System.out.println("The" +stackPane.getId()+" is empty !!!");
                            Bounds bounds = stackPane.localToScene(stackPane.getBoundsInLocal());
                            boundryOfLastImageViewInStacks.put(stackPane, bounds);
                        }
                    }
                }
            }
        }
    }

    private void setMarginOnStackPaneChildrens(StackPane stackPane) {
        if (!tableStacks.contains(stackPane)){
            ObservableList<Node> stacksPanelChildren = stackPane.getChildren();
            for (Node child : stacksPanelChildren) {
                StackPane.setMargin(child, new Insets(0, 0, 0, 0));
            }
        }else{
            ObservableList<Node> stacksPanelChildren = stackPane.getChildren();
            if (stacksPanelChildren.isEmpty()) {
                return;
            }
            ImageView lastImageViewOfStack = (ImageView) stacksPanelChildren.getLast();
            double actualHeightOfTheLastCard = lastImageViewOfStack.getBoundsInLocal().getHeight();
            double marginPerCard = actualHeightOfTheLastCard * 0.23;

            for (int i = 0; i < stacksPanelChildren.size(); i++) {
                Node currentChild = stacksPanelChildren.get(i);
                if (currentChild instanceof ImageView) {
                    // static method to apply margin!!! ok javaFX... I didn't see that coming...
                    StackPane.setMargin(currentChild, new Insets(i * marginPerCard, 0, 0, 0));
                }
            }
        }



    }



    private void updateCardRevealsStatus() {
        for (Map.Entry<Card, CardView> e : cardsMap.entrySet()) {
            CardView cardView = e.getValue();
            cardView.updateImage();
        }
    }

    private void updateStackPaneStatus(){
        for (Map.Entry<StackPane,BoardCardsSlot> e : boardSlotsMap.entrySet()) {
            StackPane stackPane =e.getKey();
            BoardCardsSlot boardCardsSlot= e.getValue();
            for (Card c:boardCardsSlot.getCards()){
                stackPane.getChildren().add( cardsMap.get(c) );
            }

        }
    }


}

