package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.MultipleCardsRemovalException;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;

import java.util.ArrayList;

public class WasteSlot extends CardsSlot implements BoardCardsSlot{
    private final SlotType slotType;

    public WasteSlot() {
        this.slotType = SlotType.WASTE;
    }

    @Override
    public SlotType getSlotType() {
        return slotType;
    }

    @Override
    public void addCards(ArrayList<Card> cards) {
        if (isAddCardsValid(cards)){
            getCards().add(cards.get(0));
        }
    }

    @Override
    public boolean isAddCardsValid(ArrayList<Card> cards) {
        return cards.size()==1;
    }

    @Override
    public ArrayList<Card> takeCards(int numberOfCards) throws MultipleCardsRemovalException {
        ArrayList<Card> c=new ArrayList<>();
        if(numberOfCards>1){
            throw new MultipleCardsRemovalException("Specific Card Slot doesnt support multi-removal");
        }
        c.add(getCards().get(0));
        return c;
    }

    @Override
    public boolean isTakeCardsValid(int numberOfCards) {
        return numberOfCards==1;
    }

    @Override
    public String toString() {
        return "WasteSlot{" +
                "slotType=" + slotType +
                '}';
    }
}
