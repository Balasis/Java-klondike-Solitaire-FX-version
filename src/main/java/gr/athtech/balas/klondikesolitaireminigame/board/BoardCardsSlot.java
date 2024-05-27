package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.exceptions.ColorsBlocksRemoval;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.HiddenCardBlocksRemoval;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.MultipleInvalidRemoval;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.RanksBlocksRemoval;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;

import java.util.ArrayList;

public interface BoardCardsSlot {

    Card getLastCard();
    Card getFirstCard();
    ArrayList<Card> getMultipleCardsFromTop(int numOfCards);
    ArrayList<Card> getMultipleCardsFromBottom(int numberOfCards);
    ArrayList<Card> getCards();

    void addCardsNoRestrictions(ArrayList<Card> cards);
    void revealLastCard();

    void addCards(ArrayList<Card> cards);
    boolean isAddCardsValid(ArrayList<Card> cards);

    ArrayList<Card> takeCards(int numberOfCards) throws MultipleInvalidRemoval, ColorsBlocksRemoval, RanksBlocksRemoval, HiddenCardBlocksRemoval;
   // boolean isTakeCardsValid(int numberOfCards);



    boolean isCardsSlotEmpty();
    SlotType getSlotType();
}
