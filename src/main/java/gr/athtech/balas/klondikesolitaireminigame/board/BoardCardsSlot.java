package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.InvalidAddCardsException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.InvalidTakeCardsException;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;

import java.util.ArrayList;

public interface BoardCardsSlot {

 // On CardSlot superclass(shared cored)
    Card getLastCard();
    Card getFirstCard();
//    ArrayList<Card> getMultipleCardsFromTop(int numOfCards);
//    ArrayList<Card> getMultipleCardsFromBottom(int numberOfCards);
    ArrayList<Card> getCards();
    boolean isCardInSlot(Card card);
    boolean isCardsSlotEmpty();

 // On subclasses (different implementation)

    void addCards(ArrayList<Card> cards) throws InvalidAddCardsException;
    boolean isAddCardsValid(ArrayList<Card> cards);

    ArrayList<Card> takeCards(int numberOfCards) throws InvalidTakeCardsException;
    boolean isTakeCardsValid(int numberOfCards);

    void revealLastCard();
    void addCardsNoRestrictions(ArrayList<Card> cards);
    SlotType getSlotType();

}