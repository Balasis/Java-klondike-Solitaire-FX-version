package gr.athtech.balas.klondikesolitaireminigame;

import gr.athtech.balas.klondikesolitaireminigame.board.BoardCardsSlot;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;
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
    //+52 Dynamically created ImageViews, as superclasses of CardViews.( Cardview extends ImageView)

    //Each CardView has a "Card"(model class) field to have info on updating its image.

    //Main game program, Maps to for info from model to view, mouse X|Y for drag and drop info, an array for loop convenience.
    private KlondikeSolitaireProgram theGame;
    private final Map<Card, CardView> cardsToCardViewsMap;
    private final Map<StackPane,BoardCardsSlot > stackPaneToBoardCardsSlotMap;
    private final Map<StackPane, Bounds> dropBountryOfEachStackPanelMap;

    private double mouseX, mouseY;
    private ArrayList<StackPane> tableStacks;
    int numberOfCardsWeTryToPick = 0;
    StackPane stackPaneChosenAsSource;



    public KlondikeSolitaireController() {
        //Init the gameProgram, get cards from a deck, removing the jokers cards, populate "cardMaps".
        theGame = new KlondikeSolitaireProgram();
        theGame.removeTheJokers();
        cardsToCardViewsMap = new HashMap<>();
        stackPaneToBoardCardsSlotMap = new HashMap<>();
        dropBountryOfEachStackPanelMap = new HashMap<>();
    }

    public void initialize() {
        tableStacks = new ArrayList<>(Arrays.asList(tableSlot1, tableSlot2, tableSlot3, tableSlot4, tableSlot5, tableSlot6, tableSlot7));
        populateMaps();
        theGame.setUpTheGame();
        theViewSetUpTheGame();
        Platform.runLater(this::updateStackPaneBounds);//creates bountry for each Stackpane(droppable areas up to interception)DragAndDrop
    }

    private void theViewSetUpTheGame(){
        reloadCardViewsForAllStackPane();
        addListenersToCardViews();
        updateMarginsOfCardsViews();
        updateCardRevealsStatus();
    }

    private void populateMaps(){
        populateCardsToCardViewsMap();
        populateTheBoardSlotMap();
    }

    private void populateCardsToCardViewsMap(){
        for (Card c : theGame.getTheDecksCards() ) {
            cardsToCardViewsMap.put(c, createCardView(c));
        }
    }

    private void populateTheBoardSlotMap(){
        //(sadly couldn't think of a shortcut or a loop due to many differences)
        //population of "boardSlotMap". Maps BoardCardsSlot to StackPane elements.
        stackPaneToBoardCardsSlotMap.put(deckSlot,theGame.getDeckSlot() );
        stackPaneToBoardCardsSlotMap.put(wasteSlot,theGame.getWasteSlot() );
        stackPaneToBoardCardsSlotMap.put(clubsFoundation,theGame.getClubsFoundationSlot() );
        stackPaneToBoardCardsSlotMap.put(diamondsFoundation,theGame.getDiamondsFoundationSlot());
        stackPaneToBoardCardsSlotMap.put( heartsFoundation,theGame.getHeartsFoundationSlot());
        stackPaneToBoardCardsSlotMap.put(spadesFoundation,theGame.getSpadesFoundationSlot());
        for (int i = 0; i < tableStacks.size(); i++) {
            stackPaneToBoardCardsSlotMap.put(tableStacks.get(i) ,theGame.getATableSlot(i));
        }
    }

    private CardView createCardView(Card c){
        CardView cV=new CardView(c);
        cV.setFitWidth(95);
        cV.setFitHeight(150);
        cV.setPreserveRatio(false);
        return cV;
    }

    private void updateMarginsOfCardsViews(){
        for(StackPane s:tableStacks){
            setMarginOnStackPaneChildrens(s);
        }
    }

    private void addListenersToCardViews(){
        for(Map.Entry<Card,CardView> c: cardsToCardViewsMap.entrySet()){
                addMouseListenersForDragAndDrop(c.getValue());
        }
    }

    private void addListenerToDeckSlotOverlay(){
//        cV.setOnMousePressed(event -> {
//            StackPane parentOfcV=(StackPane) cV.getParent();
//            theGame.addCardFromDeckToWaste(1);
//            parentOfcV.getChildren().remove(cV);
//            wasteSlot.getChildren().add(cV);
//            updateCardRevealsStatus();
//            event.consume();
//        });
    }

    private void addMouseListenersForDragAndDrop(CardView cV){
        addSetOnMouseClickListener(cV);
        addSetOnMouseDraggedListener(cV);
        addSetOnMouseReleased(cV);
    }

    //gets x|y mouse location ,change z-order at Parent of parent so the upcoming
    //"dragged" element won't get hidden by z-order issues.
    // (Required AnchorPane use because z-order would change the position of it)
    //(AnchorPane->Vbox->StackPane->CardView)
    private void addSetOnMouseClickListener(CardView cV){
        cV.setOnMousePressed(event -> {
            numberOfCardsWeTryToPick =getNumberOfCardViewsUnderIt(cV);
            stackPaneChosenAsSource =(StackPane) cV.getParent();
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

            for (Map.Entry<StackPane, Bounds> entry : dropBountryOfEachStackPanelMap.entrySet()) {
                StackPane stackPane = entry.getKey();
                Bounds bounds = entry.getValue();

                // Check for intersection using the actual scene coordinates
                if (bounds.intersects(imageViewBoundsInScene) && stackPane != parentOfMoveable) {
                    if(!theGame.moveCards( stackPaneToBoardCardsSlotMap.get(parentOfMoveable),stackPaneToBoardCardsSlotMap.get(stackPane),1 )){
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

    private void updateBoardProperties(){
        updateStackPaneBounds();
        updateCardRevealsStatus();
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

        theDragger.setTranslateX(0);
        theDragger.setTranslateY(0);
    }



    //record the bounds of the last card of each stack and reforms a full Map of them. (used for interception comparison)
    private void updateStackPaneBounds() {

        dropBountryOfEachStackPanelMap.clear();
        for (Node vboxNode : containerAnchor.getChildren()) {
            if (vboxNode instanceof VBox) {
                for (Node stackPaneNode : ((VBox) vboxNode).getChildren()) {

                    if (stackPaneNode instanceof StackPane) {

                        StackPane stackPane = (StackPane) stackPaneNode;
                        if (!stackPane.getChildren().isEmpty()) {
                            Node lastImageView = stackPane.getChildren().getLast();
                            Bounds bounds = lastImageView.localToScene(lastImageView.getBoundsInLocal());
                            dropBountryOfEachStackPanelMap.put(stackPane, bounds);
                        } else {
//                            System.out.println("The" +stackPane.getId()+" is empty !!!");
                            Bounds bounds = stackPane.localToScene(stackPane.getBoundsInLocal());
                            dropBountryOfEachStackPanelMap.put(stackPane, bounds);
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
        for (Map.Entry<Card, CardView> e : cardsToCardViewsMap.entrySet()) {
            CardView cardView = e.getValue();
            cardView.updateImage();
        }
    }

    private void reloadCardViewsForAllStackPane(){
        clearAllStackPane();
        populateAllStackPane();
    }

    private void clearAllStackPane(){
        for (Map.Entry<StackPane,BoardCardsSlot> r : stackPaneToBoardCardsSlotMap.entrySet()) {
            StackPane s= r.getKey();
            s.getChildren().clear();
        }
    }

    private void populateAllStackPane(){
        for (Map.Entry<StackPane,BoardCardsSlot> e : stackPaneToBoardCardsSlotMap.entrySet()) {
            StackPane stackPane =e.getKey();
            BoardCardsSlot boardCardsSlot= e.getValue();
            for (Card c:boardCardsSlot.getCards()){
                stackPane.getChildren().add( cardsToCardViewsMap.get(c) );
            }
        }
    }


}

