package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.exceptions.MultipleInvalidRemoval;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Rank;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Suit;

import java.util.ArrayList;

public class FoundationSlot extends CardsSlot implements BoardCardsSlot {
    private final SlotType slotType;
    private final Suit foundationSuit;

    public FoundationSlot(Suit s){
        foundationSuit=s;
        slotType=SlotType.FOUNDATION;
    }

    @Override
    public SlotType getSlotType() {
        return slotType;
    }

    @Override
    public void addCards(ArrayList<Card> cards){
        if (!isAddCardsValid(cards)){
            return;
        }
        getCards().add(cards.getFirst());
    }

    @Override
    public boolean isAddCardsValid(ArrayList<Card> cards) {
            return (cards.size()==1) && (isAddCardAcceptable(cards.getFirst()) );
    }

    @Override
    public ArrayList<Card> takeCards(int numberOfCards) throws MultipleInvalidRemoval {
        ArrayList<Card> c=new ArrayList<>();
        if(numberOfCards>1){
            throw new MultipleInvalidRemoval("Specific Card Slot doesnt support multi-removal");
        }
        c.add(getCards().getFirst());
        return c;
    }

    private boolean isAddCardAcceptable(Card card) {
        if (!isItTheRightSuit(card)){
            return false;
        }else if (getCards().isEmpty()){
            return isFirstCardAnAceIfEmpty(card);
        }else{
            return isTheRankLowerThanLast(card);
        }
    }

    private boolean isFirstCardAnAceIfEmpty(Card card){
        return !getCards().isEmpty() || card.getRank() == Rank.ACE;
    }

    private boolean isItTheRightSuit(Card card){
        return card.getSuit()==foundationSuit;
    }

    private boolean isTheRankLowerThanLast(Card card){
        return getCards().getLast().getRank().getValue() < card.getRank().getValue();
    }

    public boolean isFoundationSuitSetComplete(){
        return getCards().size()==13;
    }

}