package gr.athtech.balas.klondikesolitaireminigame;

import gr.athtech.balas.klondikesolitaireminigame.board.BoardCardsSlot;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

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
    //Maps to help updating the view according to model
    private final KlondikeSolitaireProgram theGame;
    private final Map<Card, CardView> cardsToCardViewsMap;
    private final Map<StackPane,BoardCardsSlot > stackPaneToBoardCardsSlotMap;
    private final Map<StackPane, Bounds> dropBountryOfEachStackPanelMap;
    private ArrayList<StackPane> tableStacks;
    boolean isReloadMethodRunning=false;
    //Drag and drop properties...(faking it with translate x|Y
    private StackPane stackPaneChosenAsSource;
    private double mouseX, mouseY;
    private boolean isDragAvailable=false;
    private int numberOfCardsToDrag = 0;
    private ObservableList<Node> draggedNodes;

    //Init the gameProgram, get cards from a deck, removing the jokers cards, populate "cardMaps".
    public KlondikeSolitaireController() {
        theGame = new KlondikeSolitaireProgram();
        theGame.removeTheJokers();
        cardsToCardViewsMap = new HashMap<>();
        stackPaneToBoardCardsSlotMap = new HashMap<>();
        dropBountryOfEachStackPanelMap = new HashMap<>();
    }
    //Setting up the Map lists, Setting up the game at front and back, Creating the first bountries(droppable areas)
    public void initialize() {
        tableStacks = new ArrayList<>(Arrays.asList(tableSlot1, tableSlot2, tableSlot3, tableSlot4, tableSlot5, tableSlot6, tableSlot7));
        draggedNodes= FXCollections.observableArrayList();
        populateMaps();
        theGame.setUpTheGame();
        theViewSetUpTheGame();
        Platform.runLater(this::updateStackPaneBounds);//creates bountry for each Stackpane(droppable areas up to interception)DragAndDrop
    }

    @FXML //Listener for the deckSlot Transparent Layer(above deck slot to avoid having to swap listeners on cards)
    public void deckToWasteInteraction(){
        if(!theGame.isTheDeckSlotEmptyOfCards()){
            if(theGame.moveCardFromDeckToWaste()){
                moveAVcFromDeckToWaste();
            }
        }else{//if empty, return cards to deck from waste using Unrestricted addition method(no exceptions applied)
            theGame.returnWasteCardsToDeck();
            moveAllCvFromWasteToDeck();
        }
    }
    //Adding drag and drop listeners to cardViews
    private void addListenersToCardViews(){
        for(Map.Entry<Card,CardView> c: cardsToCardViewsMap.entrySet()){
            addMouseListenersForDragAndDrop(c.getValue());
        }
    }

    private void addMouseListenersForDragAndDrop(CardView cV){
        addSetOnMouseClickListener(cV);
        addSetOnMouseDraggedListener(cV);
        addSetOnMouseReleased(cV);
    }
    //
    private void addSetOnMouseClickListener(CardView cV){
        cV.setOnMousePressed(event -> {
            StackPane sp=(StackPane) cV.getParent();
            if (!isTheSetUnderCardViewDragable(cV)){
                resetDraggingProperties(cV);
                return;
            }//to not get a blocked view by other StackPane, AnchorPane used for no visible reorder.
            cardViewParentOfParentInFront(event);
            isDragAvailable=true;
            for (int i = stackPaneChosenAsSource.getChildren().size() - numberOfCardsToDrag; i < stackPaneChosenAsSource.getChildren().size() ; i++) {
                draggedNodes.add(stackPaneChosenAsSource.getChildren().get(i));
            }
            for (Node n:draggedNodes){
                if (n instanceof CardView){
                    CardView cardView=(CardView) n;
                    System.out.println(cardView.getCard());
                }
            }
            setMouseCurrentLocation(event);
            event.consume();
        });
    }

    //update and sum to x|y mouse loc, changes translate x|y (fake drag)
    private void addSetOnMouseDraggedListener(CardView cV){
        cV.setOnMouseDragged(event -> {
            if(!isDragAvailable){
                return;
            }
            updateTranslateXYtoCardViewSet(event);
            event.consume();
        });
    }

    private void updateTranslateXYtoCardViewSet(MouseEvent e){
        double offsetX = e.getSceneX() - mouseX;
        double offsetY = e.getSceneY() - mouseY;
        for (Node n:draggedNodes){
            n.setTranslateX(offsetX);
            n.setTranslateY(offsetY);
        }

    }

    // drop(fakedrop) : Updating the drop areas, check map to find intercepted StackPane,
    //                  move cards on model,if success move corresponding CardViews,
    //                  rest dragAndDrop properties, check if you won the game.
    private void addSetOnMouseReleased(CardView cV){
        cV.setOnMouseReleased(event -> {
            updateStackPaneBounds();
            //getting info about the drager cardview,its parent Stackpane(container) and its boundary
            CardView theDragger = (CardView) event.getSource();
            Bounds imageViewBoundsInScene = theDragger.localToScene(theDragger.getBoundsInLocal());
            StackPane parentOfMoveable = (StackPane) theDragger.getParent();
            //loop through a map of potential boundaries to find where it dropped(dragger intercepted with stackPane)
            //the boundary is set on the last cardView of each StackPane related to localScene;
            loopThroughMapToFindInterceptedArea(imageViewBoundsInScene,parentOfMoveable);
            //restore drag and drop properties and check if the game is over.
            resetDraggingProperties(theDragger);
            updateBoardProperties();
            if (theGame.isTheGameWon()){
                showCongratulatoryPopup();
            }
            event.consume();
        });
    }

    private void loopThroughMapToFindInterceptedArea(Bounds imageViewBoundsInScene,StackPane parentOfMoveable){
        for (Map.Entry<StackPane, Bounds> entry : dropBountryOfEachStackPanelMap.entrySet()) {
            StackPane stackPane = entry.getKey();
            Bounds bounds = entry.getValue();

            if (bounds.intersects(imageViewBoundsInScene) && stackPane != parentOfMoveable) {
                modifyAffectedStackPane(parentOfMoveable,stackPane);
                break;
            }
        }
    }


    private void modifyAffectedStackPane(StackPane parentOfDragger, StackPane droppedAtStackPane){
       if( theGame.moveCards( stackPaneToBoardCardsSlotMap.get(parentOfDragger),stackPaneToBoardCardsSlotMap.get(droppedAtStackPane),draggedNodes.size() ) ){
           moveImageViewsBetweenPanels(droppedAtStackPane);
           setMarginsOnAffectedPanels(parentOfDragger,droppedAtStackPane);
       }

    }

    private void moveAVcFromDeckToWaste(){
        wasteSlot.getChildren().add(deckSlot.getChildren().getLast());
        CardView lastCvInWaste=(CardView) wasteSlot.getChildren().getLast();
        lastCvInWaste.updateImage();
        System.out.println(lastCvInWaste.getCard());
    }

    private void moveAllCvFromWasteToDeck() {
        ObservableList<Node> childrenCopy=createAReversedCopy();
        wasteSlot.getChildren().clear();
        deckSlot.getChildren().addAll(childrenCopy);
        updateCardRevealsStatus();
    }

    private ObservableList<Node> createAReversedCopy(){
        ObservableList<Node> childrenCopy = FXCollections.observableArrayList(wasteSlot.getChildren());
        Collections.reverse(childrenCopy);
        return childrenCopy;
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

    private void resetDraggingProperties(CardView cV){
        resetOnMouseClickProperties();
        resetOnMouseDragProperties();
    }

    private void resetOnMouseClickProperties(){
        mouseX = 0;
        mouseY = 0;
        numberOfCardsToDrag =0;
        stackPaneChosenAsSource=null;
        isDragAvailable=false;
    }

    private void resetOnMouseDragProperties(){
        for (Node n:draggedNodes){
            if(n instanceof CardView){
                n.setTranslateX(0);
                n.setTranslateY(0);
            }
        }
        draggedNodes.clear();
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

    private void updateBoardProperties(){
        updateStackPaneBounds();
        updateCardRevealsStatus();
    }

    private void moveImageViewsBetweenPanels(StackPane stackPane){
        stackPaneChosenAsSource.getChildren().removeAll(draggedNodes);
        for(Node n : draggedNodes){
            if (n instanceof CardView){
                System.out.println(((CardView) n).getCard());
                stackPane.getChildren().add(n);
            }
        }

    }

    private void setMarginsOnAffectedPanels(StackPane source,StackPane target){
        setMarginOnStackPaneChildrens(source);
        setMarginOnStackPaneChildrens(target);
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

    private void reloadCardViewsForAllStackPane(){
        if (isReloadMethodRunning){
            return;
        }
        isReloadMethodRunning=true;
        clearAllStackPane();
        populateAllStackPane();
        updateCardRevealsStatus();
        isReloadMethodRunning=false;//reset reload
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

    private void updateCardRevealsStatus() {
        for (Map.Entry<Card, CardView> e : cardsToCardViewsMap.entrySet()) {
            e.getValue().updateImage();
        }
    }

    private void showCongratulatoryPopup() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Congratulations!");
            alert.setHeaderText(null);
            alert.setContentText("Congratulations! You have won the game!");
            Button restartButton = new Button("Restart");
            restartButton.setOnAction(e -> restartGame());
            alert.getDialogPane().setContent(restartButton);
            alert.showAndWait();
        });
    }

    private void restartGame() {
        Platform.runLater(() -> {
            Stage stage = (Stage) containerAnchor.getScene().getWindow();
            stage.close();
            KlondikeSolitaireApp newApp = new KlondikeSolitaireApp();
            try {
                newApp.start(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isTheSetUnderCardViewDragable(CardView cV){
        numberOfCardsToDrag =getNumberOfCardViewsUnderIt(cV);
        stackPaneChosenAsSource =(StackPane) cV.getParent();
        BoardCardsSlot bOfcardView=stackPaneToBoardCardsSlotMap.get(stackPaneChosenAsSource);
        return theGame.isTakeCardsPossible(bOfcardView, numberOfCardsToDrag);
    }

}

