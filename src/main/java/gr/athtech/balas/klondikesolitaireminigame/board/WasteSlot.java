package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.IncorrNumOfCardsAdditionException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.IncorrNumOfCardsRemovalException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.NoCardsToTakeException;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;

import java.util.ArrayList;
import java.util.Collections;

public class WasteSlot extends CardsSlot implements BoardCardsSlot {
    private final SlotType slotType;

    public WasteSlot() {
        this.slotType = SlotType.WASTE;
    }

    // API /Overrides...
    @Override
    public SlotType getSlotType() {
        return slotType;
    }

    @Override
    public void addCards(ArrayList<Card> cards) throws IncorrNumOfCardsAdditionException {
        if (isAddCardsValid(cards)) {
            if (cards.size() != 1) {
                throw new IncorrNumOfCardsAdditionException("waste slot can have only 1 card added per time");
            }
        }
        getCards().addLast(cards.getFirst());
    }

    @Override
    public boolean isAddCardsValid(ArrayList<Card> cards) {
        return cards.size() == 1;
    }

    @Override
    public ArrayList<Card> takeCards(int numberOfCards) throws IncorrNumOfCardsRemovalException, NoCardsToTakeException {
        if (getCards().size() < numberOfCards) {
            throw new NoCardsToTakeException("there aren't so many cards to pick from");
        }
        if (numberOfCards > 1) {
            throw new IncorrNumOfCardsRemovalException("Specific Card Slot doesnt support multi-removal");
        }
        return new ArrayList<Card>(Collections.singletonList(getCards().removeLast()));
    }

    @Override
    public boolean isTakeCardsValid(int numberOfCards) {
        return !getCards().isEmpty() && numberOfCards == 1;
    }

    @Override
    public String toString() {
        return "WasteSlot{" +
                "slotType=" + slotType +
                '}';
    }

}