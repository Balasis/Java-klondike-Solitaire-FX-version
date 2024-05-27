package gr.athtech.balas.klondikesolitaireminigame;

import gr.athtech.balas.klondikesolitaireminigame.board.*;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.InvalidTakeCardsException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.IncorrNumOfCardsRemovalException;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Deck;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Suit;

import java.util.ArrayList;

import static gr.athtech.balas.klondikesolitaireminigame.thedeck.Suit.*;

public class KlondikeSolitaireProgram {
    private Deck deck;
    private DeckSlot deckSlot;
    private WasteSlot wasteSlot;
    private ArrayList<FoundationSlot> foundationSlots;
    private TableSlot[] tableSlots;
    private MoveCardsSlotTypeValidator moveCardsSlotTypeValidator;

    public KlondikeSolitaireProgram(){
        deck=new Deck();
        deckSlot=new DeckSlot();
        wasteSlot=new WasteSlot();
        moveCardsSlotTypeValidator=new MoveCardsSlotTypeValidator();
        createFoundationSlotPerSuit();
        createTableSlots();
    }

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

    public void setUpTheGame(){
        if (deck.isEmpty()){
            return;
        }
        deck.removeTheJokers();
        deck.shuffle();
        setUpTableSlots();
        addRemainingCardsIntoDeckslot();
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

    public void addCardFromDeckToWaste(int numberOfCards){
        try {
            ArrayList<Card> cardsFromDeck=deckSlot.takeCards(numberOfCards);
            for(Card c:cardsFromDeck){
                c.setIsFaceUp(true);
            }
            wasteSlot.addCards(cardsFromDeck);
        } catch (IncorrNumOfCardsRemovalException e) {
            throw new RuntimeException(e);
        }
    }

    public void moveCardsFromWasteToDeckSlot(){
        deckSlot.addCardsNoRestrictions(wasteSlot.getCards());
        wasteSlot.getCards().clear();
    }

    public void turnToHiddenAllCardsDeckSlot(){
        for (Card s:deckSlot.getCards()){
            s.setIsFaceUp(false);
        }
    }

    public boolean isTheGameWon(){
        for (int i = 0; i < foundationSlots.size(); i++) {
            if (!foundationSlots.get(i).isFoundationSuitSetComplete()){
                return false;
            }
        }
        return true;
    }

    public void revealLastCardOfACardSlot(BoardCardsSlot b){
        b.revealLastCard();
    }

    private boolean isTransferAmongSlotsTypesAllowed(BoardCardsSlot from,BoardCardsSlot to){
        return moveCardsSlotTypeValidator.isMoveToCardSlotValid(from,to);
    }

}