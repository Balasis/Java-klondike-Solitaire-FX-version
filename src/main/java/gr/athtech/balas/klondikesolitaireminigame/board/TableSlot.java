package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.exceptions.ColorsBlocksRemoval;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.HiddenCardBlocksRemoval;
import gr.athtech.balas.klondikesolitaireminigame.exceptions.RanksBlocksRemoval;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.CardColor;
import gr.athtech.balas.klondikesolitaireminigame.thedeck.Rank;

import java.util.ArrayList;

public class TableSlot extends CardsSlot implements BoardCardsSlot{
    private final SlotType slotType;

    public TableSlot() {
        this.slotType = SlotType.TABLE;
    }

    @Override
    public SlotType getSlotType() {
        return slotType;
    }

    @Override
    public void addCards(ArrayList<Card> cards){
        if (isAddCardsValid(cards)){
            this.getCards().addAll(cards);
        }
    }

    @Override
    public boolean isAddCardsValid(ArrayList<Card> cards) {
        if (cards.isEmpty()){
            return false;
        }
        if(isCardsSlotEmpty()){
            System.out.println("inhere");
            return isAKingOnEmptySlot(cards);
        }
        return isTheFirstCardColorCorrect(cards) && isTheFirstCardRankCorrect(cards);
    }

    private boolean isAKingOnEmptySlot(ArrayList<Card> cards){
       return  (cards.getFirst().getRank()== Rank.KING);
    }

    private boolean isTheFirstCardColorCorrect(ArrayList<Card> cards){
        String incomingCardColor=cards.getFirst().getCardColor();
        String lastCardOfTheSlotColor=getCards().getLast().getCardColor();

       return !incomingCardColor.equals(lastCardOfTheSlotColor);
    }

    private boolean isTheFirstCardRankCorrect(ArrayList<Card> cards){
        int rankValueOfFirstIncomingCard=cards.getFirst().getRank().getValue();
        int rankValueOfLastCardInSlot=getCards().getLast().getRank().getValue();

        System.out.println(rankValueOfFirstIncomingCard);
        System.out.println(rankValueOfLastCardInSlot);

        return  rankValueOfFirstIncomingCard+1 == rankValueOfLastCardInSlot;
    }

    //Before I remove them from the list I first create a dummy
    //array to check removal capability according to game rules
    @Override
    public ArrayList<Card> takeCards(int numberOfCards) throws ColorsBlocksRemoval, RanksBlocksRemoval, HiddenCardBlocksRemoval {
        ArrayList<Card> listForTakeabilityCheck=formAListToBeChecked(numberOfCards);
        if (!areTheColorsCorrect(listForTakeabilityCheck)){
            throw new ColorsBlocksRemoval("Incorrect combination of colors");
        }
        if(!areTheRanksCorrect(listForTakeabilityCheck)){
            throw new RanksBlocksRemoval("Incorrect combination of Ranks");
        }
        if(!areAllTheCardsRevealed(listForTakeabilityCheck)){
            throw new HiddenCardBlocksRemoval("Hidden Cards block removal");
        }
        ArrayList<Card> cardsToBeReturned=new ArrayList<>();
        populateCardsToBeReturned(cardsToBeReturned,numberOfCards);
        return cardsToBeReturned;
    }

    private void populateCardsToBeReturned(ArrayList<Card> cardsToBeReturnedList, int toTakeCardsFromTableSlot){
        for (int i = 0; i < toTakeCardsFromTableSlot; i++) {
            if(getCards().isEmpty()){
                break;
            }
            cardsToBeReturnedList.addFirst(getCards().removeLast());
        }
    }

    private ArrayList<Card> formAListToBeChecked(int numberOfCards) {
        ArrayList<Card> listToBeChecked=new ArrayList<>();
        for (int i = 0; i < getCards().size(); i++) {
            if(i>=numberOfCards){
                break;
            }
            listToBeChecked.addFirst(getCards().get( (getCards().size()-i) -1 ));
        }
        return listToBeChecked;
    }

    private boolean areTheColorsCorrect(ArrayList<Card> cards){
        CardColor currentColor=null;
        for (int i = 0; i < cards.size(); i++) {
            Card c=cards.get(i);
            CardColor cColor=c.getCardColor();
            if (currentColor!=null){
                if(cColor.equals(currentColor)){
                    return false;
                }
            }
            currentColor=cColor;
        }
        return true;
    }

    private boolean areTheRanksCorrect(ArrayList<Card> cards){
        if (cards.size() <= 1) {
            return true;
        }
        for (int i = 0; i < cards.size() - 1; i++) {
            int curCardRankVal=cards.get(i).getRank().getValue();
            int validRankValueForNextCard=curCardRankVal-1;
            int nextCardRankVal=cards.get(i+1).getRank().getValue() ;

            if (!(validRankValueForNextCard==nextCardRankVal)) {
                return false;
            }
        }

        return true;
    }

    private boolean areAllTheCardsRevealed(ArrayList<Card> cards){
        for (int i = 0; i <cards.size() ; i++) {
            Card c=cards.get(i);
           if(!c.getIsFaceUp()){
               return false;
           }
        }
        return true;
    }

    @Override
    public String toString() {
        return this.slotType+" "+super.toString();
    }
}
