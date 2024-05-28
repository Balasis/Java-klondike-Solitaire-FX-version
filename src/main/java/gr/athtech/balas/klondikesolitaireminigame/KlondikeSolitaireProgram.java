package gr.athtech.balas.klondikesolitaireminigame;

import gr.athtech.balas.klondikesolitaireminigame.board.*;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.InvalidCardSlotTypeException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.InvalidAddCardsException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.InvalidTakeCardsException;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Deck;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Suit;

import java.util.ArrayList;
import java.util.Collections;

import static gr.athtech.balas.klondikesolitaireminigame.thedeck.Suit.*;

public class KlondikeSolitaireProgram {
    private final MoveCardsSlotTypeValidator moveCardsSlotTypeValidator;
    private final Deck deck;
    private final DeckSlot deckSlot;
    private final WasteSlot wasteSlot;
    private ArrayList<FoundationSlot> foundationSlots;
    private TableSlot[] tableSlots;

    public KlondikeSolitaireProgram(){
        deck=new Deck();
        deckSlot=new DeckSlot();
        wasteSlot=new WasteSlot();
        moveCardsSlotTypeValidator=new MoveCardsSlotTypeValidator();
        createFoundationSlotPerSuit();
        createTableSlots();
    }

    //Api
    public void setUpTheGame(){
        if (deck.isEmpty()){
            return;
        }
        deck.removeTheJokers();
        deck.shuffle();
        setUpTableSlots();
        addRemainingCardsIntoDeckslot();
    }

    public boolean moveCards(BoardCardsSlot from, BoardCardsSlot to, int numberOfCards){

        ArrayList<Card> cardsToBeMoved=new ArrayList<>();
        try {
            if(!isTransferAmongSlotsTypesAllowed(from,to)){
                throw new InvalidCardSlotTypeException("Can't Transfer Among Slots");
            }
            cardsToBeMoved=from.takeCards(numberOfCards);
            to.addCards(cardsToBeMoved);
            if ( !(from instanceof DeckSlot)){
                from.revealLastCard();
            }
        } catch (InvalidCardSlotTypeException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (InvalidTakeCardsException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (InvalidAddCardsException e) {
            from.addCardsNoRestrictions(cardsToBeMoved);
           System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean isTakeCardsPossible(BoardCardsSlot theBoardCardSlot, int numOfCards){
         return  theBoardCardSlot.isTakeCardsValid(numOfCards);
    }

    public void removeTheJokers(){//in case you need removal before setUp
        deck.removeTheJokers();
    }

    public boolean moveCardFromDeckToWaste(){
       if( moveCards(getDeckSlot(),getWasteSlot(),1) ){
           wasteSlot.revealLastCard();
           return true;
       }
        return false;
    }

    public void returnWasteCardsToDeck(){

        deckSlot.addCardsNoRestrictions(wasteSlot.takeAllCardsNoRestrictions());
        Collections.reverse(deckSlot.getCards());
        for (Card c : getDeckSlot().getCards()) {
            c.setIsFaceUp(false);//hide back cards...
        }
    }

    public boolean isTheGameWon(){
        return foundationSlots.stream().allMatch(FoundationSlot::isFoundationSuitSetComplete);
    }

    public void revealBoardsSlotLastCard(BoardCardsSlot b){
        b.revealLastCard();
    }






    // Privates
    private void createFoundationSlotPerSuit(){
        foundationSlots=new ArrayList<>();
        Suit[] f={CLUBS,DIAMONDS,HEARTS,SPADES};
        for(Suit s:f){
            foundationSlots.add(new FoundationSlot(s));
        }
    }

    private void createTableSlots(){
        tableSlots=new TableSlot[7];
        for (int i = 0; i < 7; i++) {
            tableSlots[i]= new TableSlot();
        }
    }

    private void setUpTableSlots(){
        for (int i = 0; i < tableSlots.length; i++) {
            giveSettingUpCardsToTableSlots(i);
            tableSlots[i].revealLastCard();
        }
    }

    private void giveSettingUpCardsToTableSlots(int i){
        tableSlots[i].addCardsNoRestrictions(deck.takeNumberOfDeckCards(i+1));
    }

    private void addRemainingCardsIntoDeckslot(){
        deckSlot.addCardsNoRestrictions(deck.takeAllDeckCards());
    }

    private boolean isTransferAmongSlotsTypesAllowed(BoardCardsSlot from,BoardCardsSlot to){
        return moveCardsSlotTypeValidator.isMoveToCardSlotValid(from,to);
    }

    // Getters
    public FoundationSlot getClubsFoundationSlot(){
        FoundationSlot theClubOne=null;
        for(FoundationSlot f:foundationSlots){
            if(f.getFoundationSuit()== CLUBS){
                theClubOne=f;
            }
        }
        return theClubOne;
    }

    public FoundationSlot getDiamondsFoundationSlot(){
        FoundationSlot theDiamondsOne=null;
        for(FoundationSlot f:foundationSlots){
            if(f.getFoundationSuit()== DIAMONDS){
                theDiamondsOne=f;
            }
        }
        return theDiamondsOne;
    }

    public FoundationSlot getHeartsFoundationSlot(){
        FoundationSlot theHeartsOne=null;
        for(FoundationSlot f:foundationSlots){
            if(f.getFoundationSuit()== HEARTS){
                theHeartsOne=f;
            }
        }
        return theHeartsOne;
    }

    public FoundationSlot getSpadesFoundationSlot(){
        FoundationSlot theSpadesOne=null;
        for(FoundationSlot f:foundationSlots){
            if(f.getFoundationSuit()== SPADES){
                theSpadesOne=f;
            }
        }
        return theSpadesOne;
    }

    public Deck getDeck() {
        return deck;
    }

    public ArrayList<Card> getTheDecksCards(){
        return deck.getCards();
    }

    public DeckSlot getDeckSlot() {
        return deckSlot;
    }

    public WasteSlot getWasteSlot() {
        return wasteSlot;
    }

    public ArrayList<FoundationSlot> getFoundationSlots() {
        return foundationSlots;
    }

    public TableSlot[] getTableSlots() {
        return tableSlots;
    }

    public TableSlot getATableSlot(int index) {
        return tableSlots[index];
    }

    public boolean isTheDeckSlotEmptyOfCards(){
        return getDeckSlot().getCards().isEmpty();
    }

}