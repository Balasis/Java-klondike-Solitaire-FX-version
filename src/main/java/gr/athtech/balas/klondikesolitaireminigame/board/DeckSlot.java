package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.RequiredEmptyListException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.IncorrNumOfCardsRemovalException;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;

import java.util.ArrayList;

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
    public ArrayList<Card> takeCards(int numberOfCards) throws IncorrNumOfCardsRemovalException {
        ArrayList<Card> c=new ArrayList<>();
        if(numberOfCards>1){
            throw new IncorrNumOfCardsRemovalException("Specific type of BoardCardSlot doesnt support multiRemoval");
        }
        c.add(getCards().get(0));
        return c;
    }

   // @Override
    public boolean isTakeCardsValid(int numberOfCards) {
        return numberOfCards==1;
    }


    @Override
    public String toString() {
        return this.slotType+" "+super.toString();
    }
}
