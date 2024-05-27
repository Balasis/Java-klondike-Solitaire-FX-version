package gr.athtech.balas.klondikesolitaireminigame.board;

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
    public void addCards(ArrayList<Card> cards) throws RequiredEmptyListException{
        if (isAddCardsValid(cards)){
            throw new RequiredEmptyListException("DeckSlot needs to be empty to receive cards");
        }
        getCards().addAll(cards);
    }

    @Override
    public boolean isAddCardsValid(ArrayList<Card> cards) {
        return getCards().isEmpty();
    }

    @Override
    public ArrayList<Card> takeCards(int numberOfCards) throws IncorrNumOfCardsRemovalException, NoCardsToTakeException {
        if(getCards().isEmpty()){
            throw new NoCardsToTakeException("No cards to take");
        }
        if(numberOfCards>1){
            throw new IncorrNumOfCardsRemovalException("Specific type of BoardCardSlot doesnt support multiRemoval");
        }
        return new ArrayList<Card>(Collections.singletonList(getCards().getFirst()));
    }

    @Override
    public boolean isTakeCardsValid(int numberOfCards) {
        return numberOfCards==1;
    }

    @Override
    public String toString() {
        return this.slotType+" "+super.toString();
    }
}