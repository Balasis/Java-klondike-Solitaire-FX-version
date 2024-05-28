package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.FoundationAdditionCriteriaException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.IncorrNumOfCardsAdditionException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.addcardsexceptions.NoCardsToAddException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.IncorrNumOfCardsRemovalException;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.takecardsexceptions.NoCardsToTakeException;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Rank;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FoundationSlot extends CardsSlot implements BoardCardsSlot {
    private final SlotType slotType;
    private final Suit foundationSuit;

    public FoundationSlot(Suit s){
        foundationSuit=s;
        slotType=SlotType.FOUNDATION;
    }

    // API
    public boolean isFoundationSuitSetComplete(){
        return getCards().size()==13;
    }

    @Override
    public SlotType getSlotType() {
        return slotType;
    }

    @Override
    public void addCards(ArrayList<Card> cards) throws IncorrNumOfCardsAdditionException, NoCardsToAddException, FoundationAdditionCriteriaException {
        if (cards.isEmpty()){
            throw new NoCardsToAddException("No Cards in the list to be added");
        }
        if (cards.size()==1){
            throw new IncorrNumOfCardsAdditionException("Foundation slot can have only 1 card added per time");
        }
        checkingFoundationCritiria(cards);//throws a critiria exception with the according message depending on violation
        getCards().add(cards.getFirst());
    }

    @Override
    public boolean isAddCardsValid(ArrayList<Card> cards) {
            return (cards.size()==1) && (isAddCardAcceptable(cards.getFirst()) );
    }

    @Override
    public ArrayList<Card> takeCards(int numberOfCards) throws IncorrNumOfCardsRemovalException, NoCardsToTakeException {
        if(getCards().size()<numberOfCards){
            throw new NoCardsToTakeException("there aren't so many cards to pick from");
        }
        if(numberOfCards>1){
            throw new IncorrNumOfCardsRemovalException("Specific Card Slot doesnt support multi-removal");
        }
        return new ArrayList<Card>(Collections.singletonList(getCards().getFirst()));
    }

    @Override
    public boolean isTakeCardsValid(int numberOfCards) {
        return numberOfCards==1 && !getCards().isEmpty();
    }

    // Privates
    private boolean isAddCardAcceptable(Card card) {
        if (!isItTheRightSuit(card)){
            return false;
        }else if (getCards().isEmpty()){
            return isFirstCardAnAceIfEmpty(card);
        }else{
            return isTheRankLowerThanLast(card);
        }
    }

    private void checkingFoundationCritiria(ArrayList<Card> cards) throws FoundationAdditionCriteriaException {
        String theErrorMessage="";
        Boolean wasThereAThrow=false;
        if (!isItTheRightSuit(cards.getFirst())){
            wasThereAThrow=true;
            theErrorMessage="Incorrect suit for specific foundation Slot";
        }else if (getCards().isEmpty()){
            wasThereAThrow= isFirstCardAnAceIfEmpty(cards.getFirst());
            theErrorMessage="First Card needs to be an Ace";
        }else{
            wasThereAThrow= isTheRankLowerThanLast(cards.getFirst());
            theErrorMessage="Rank not in the right order";
        }
        if(wasThereAThrow){
            throw new FoundationAdditionCriteriaException(theErrorMessage);
        }
    }

    private boolean isItTheRightSuit(Card card){
        return card.getSuit()==foundationSuit;
    }

    private boolean isFirstCardAnAceIfEmpty(Card card){
        return !getCards().isEmpty() || card.getRank() == Rank.ACE;
    }

    private boolean isTheRankLowerThanLast(Card card){
        return getCards().getLast().getRank().getValue() < card.getRank().getValue();
    }

    // Getters
    public Suit getFoundationSuit() {
        return foundationSuit;
    }

    @Override
    public String toString() {
        return "FoundationSlot{" +
                "slotType=" + slotType +
                ", foundationSuit=" + foundationSuit +
                '}';
    }
}