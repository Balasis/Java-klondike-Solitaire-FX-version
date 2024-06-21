package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.NoCardsToAddException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.NoKingOnEmptyTSException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.WrongColorOrRankAddException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.ColorsOutOfOrderException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.HiddenCardRemovalException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.NoCardsToTakeException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.RanksOutOfOrderException;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.CardColor;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Rank;

import java.util.ArrayList;

public class TableSlot extends CardsSlot implements BoardCardsSlot {
    private final SlotType slotType;

    public TableSlot() {
        this.slotType = SlotType.TABLE;
    }

    @Override
    public SlotType getSlotType() {
        return slotType;
    }

    @Override
    public void addCards(ArrayList<Card> cards) throws NoCardsToAddException, NoKingOnEmptyTSException, WrongColorOrRankAddException {
        if (cards.isEmpty()) {
            throw new NoCardsToAddException("No cards to take");
        }
        if (isCardsSlotEmpty()) {
            if (!isAKingOnEmptySlot(cards)) {
                throw new NoKingOnEmptyTSException("Empty table slots take only Kings");
            }
        } else if (!isTheFirstCardColorCorrect(cards) || !isTheFirstCardRankCorrect(cards)) {
            throw new WrongColorOrRankAddException("Incorrect Color or Rank");
        }
        this.getCards().addAll(cards);
    }

    @Override
    public boolean isAddCardsValid(ArrayList<Card> cards) {
        if (cards.isEmpty()) {
            return false;
        }
        if (isCardsSlotEmpty()) {
            return isAKingOnEmptySlot(cards);
        }
        return isTheFirstCardColorCorrect(cards) && isTheFirstCardRankCorrect(cards);
    }

    @Override
    public ArrayList<Card> takeCards(int numberOfCards) throws ColorsOutOfOrderException,
            RanksOutOfOrderException, HiddenCardRemovalException, NoCardsToTakeException {

        ArrayList<Card> listForTakeabilityCheck = formAListToBeChecked(numberOfCards);
        ArrayList<Card> cardsToBeReturned = new ArrayList<>();
        if (getCards().size() < numberOfCards) {
            throw new NoCardsToTakeException("there aren't so many cards to pick from");
        }
        if (!areAllTheCardsRevealed(listForTakeabilityCheck)) {
            throw new HiddenCardRemovalException("Hidden Cards block removal");
        }
        if (!areTheColorsCorrect(listForTakeabilityCheck)) {
            throw new ColorsOutOfOrderException("Incorrect combination of colors");
        }
        if (!areTheRanksCorrect(listForTakeabilityCheck)) {
            throw new RanksOutOfOrderException("Incorrect combination of Ranks");
        }

        populateCardsToBeReturned(cardsToBeReturned, numberOfCards);
        return cardsToBeReturned;
    }

    @Override
    public boolean isTakeCardsValid(int numberOfCards) {
        ArrayList<Card> listForTakeabilityCheck = formAListToBeChecked(numberOfCards);

        return !getCards().isEmpty() && getCards().size() >= numberOfCards &&
                areTheColorsCorrect(listForTakeabilityCheck) &&
                areTheRanksCorrect(listForTakeabilityCheck) &&
                areAllTheCardsRevealed(listForTakeabilityCheck);
    }

    private boolean isAKingOnEmptySlot(ArrayList<Card> cards) {
        return (cards.getFirst().getRank() == Rank.KING);
    }

    private boolean isTheFirstCardColorCorrect(ArrayList<Card> cards) {
        CardColor incomingCardColor = cards.getFirst().getCardColor();
        CardColor lastCardOfTheSlotColor = getCards().getLast().getCardColor();

        return !incomingCardColor.equals(lastCardOfTheSlotColor);
    }

    private boolean isTheFirstCardRankCorrect(ArrayList<Card> cards) {
        int rankValueOfFirstIncomingCard = cards.getFirst().getRank().getValue();
        int rankValueOfLastCardInSlot = getCards().getLast().getRank().getValue();
        return rankValueOfFirstIncomingCard + 1 == rankValueOfLastCardInSlot;
    }

    private void populateCardsToBeReturned(ArrayList<Card> cardsToBeReturnedList, int toTakeCardsFromTableSlot) {
        for (int i = 0; i < toTakeCardsFromTableSlot; i++) {
            if (getCards().isEmpty()) {
                break;
            }
            cardsToBeReturnedList.addFirst(getCards().removeLast());
        }
    }

    private ArrayList<Card> formAListToBeChecked(int numberOfCards) {
        ArrayList<Card> listToBeChecked = new ArrayList<>();
        for (int i = 0; i < getCards().size(); i++) {
            if (i >= numberOfCards) {
                break;
            }
            listToBeChecked.addLast(getCards().get((getCards().size() - i) - 1));
        }
        return listToBeChecked;
    }

    private boolean areTheColorsCorrect(ArrayList<Card> cards) {
        CardColor currentColor = null;
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            CardColor cColor = c.getCardColor();
            if (currentColor != null) {
                if (cColor.equals(currentColor)) {
                    return false;
                }
            }
            currentColor = cColor;
        }
        return true;
    }

    private boolean areTheRanksCorrect(ArrayList<Card> cards) {
        if (cards.size() <= 1) {
            return true;
        }
        for (int i = 0; i < cards.size() - 1; i++) {
            int curCardRankVal = cards.get(i).getRank().getValue();
            int nextCardRankVal = cards.get(i + 1).getRank().getValue();

            // Check if the current card's rank is exactly one greater than the next card's rank
            if (curCardRankVal == nextCardRankVal + 1) {
                return false;
            }
        }

        return true;
    }

    private boolean areAllTheCardsRevealed(ArrayList<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            if (!c.getIsFaceUp()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return this.slotType + " " + super.toString();
    }

}