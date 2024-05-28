package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.InvalidAddCardsException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.RequiredEmptyListException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.IncorrNumOfCardsRemovalException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.NoCardsToTakeException;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;

import java.util.ArrayList;
import java.util.Collections;

public class DeckSlot extends CardsSlot implements BoardCardsSlot{
    private final SlotType slotType;

    public DeckSlot() {
        this.slotType = SlotType.DECK;
    }

    @Override
    public SlotType getSlotType() {
        return slotType;
    }

    @Override
    public void addCards(ArrayList<Card> cards) throws InvalidAddCardsException {
        if (isAddCardsValid(cards)){
            throw new InvalidAddCardsException("DeckSlot accepts only No Restricted additions");
        }
    }

    @Override
    public boolean isAddCardsValid(ArrayList<Card> cards) {
        return false;
    }

    @Override
    public ArrayList<Card> takeCards(int numberOfCards) throws IncorrNumOfCardsRemovalException, NoCardsToTakeException {
        if(getCards().size()<numberOfCards){
            throw new NoCardsToTakeException("there aren't so many cards to pick from");
        }
        if(numberOfCards>1){
            throw new IncorrNumOfCardsRemovalException("Specific type of BoardCardSlot doesnt support multiRemoval");
        }
        return new ArrayList<Card>(Collections.singletonList(getCards().removeLast()));
    }

    @Override
    public boolean isTakeCardsValid(int numberOfCards) {
        return numberOfCards==1 && !getCards().isEmpty();
    }

    @Override
    public String toString() {
        return this.slotType+" "+super.toString();
    }
}