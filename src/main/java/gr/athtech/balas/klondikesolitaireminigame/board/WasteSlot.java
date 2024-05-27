package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.IncorrNumOfCardsAdditionException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.IncorrNumOfCardsRemovalException;
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
    public void addCards(ArrayList<Card> cards) throws IncorrNumOfCardsAdditionException {
        if (isAddCardsValid(cards)){
            if (cards.size()==1){
                throw new IncorrNumOfCardsAdditionException("Foundation slot can have only 1 card added per time");
            }
        }
        getCards().add(cards.getFirst());
    }

    @Override
    public boolean isAddCardsValid(ArrayList<Card> cards) {
        return cards.size()==1;
    }

    @Override
    public ArrayList<Card> takeCards(int numberOfCards) throws IncorrNumOfCardsRemovalException {
        ArrayList<Card> c=new ArrayList<>();
        if(numberOfCards>1){
            throw new IncorrNumOfCardsRemovalException("Specific Card Slot doesnt support multi-removal");
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
