package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;

import java.util.ArrayList;

public class CardsSlot {
    private final ArrayList<Card> cards;

    public CardsSlot(){
        cards=new ArrayList<>();
    }

    public Card getLastCard() {
        return cards.getLast();
    }


    public Card getFirstCard() {
        return cards.getFirst();
    }

    //first card of the Top is index card.size() - 1
    //we to the first always because we pick from the last
    //otherwise the order will be reversed
    public ArrayList<Card> getMultipleCardsFromTop(int numberOfCards) {
        ArrayList<Card> cardsToBeGiven=new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (i>=numberOfCards){
                break;
            }
            cardsToBeGiven.addFirst(cards.get( (cards.size()-i)-1) );
        }
        return cardsToBeGiven;
    }

    //first card of the bottom is the index 0
    public ArrayList<Card> getMultipleCardsFromBottom(int numberOfCards) {
        ArrayList<Card> cardsToBeGiven=new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (i>=numberOfCards){
                break;
            }
            cardsToBeGiven.add(cards.get(i));
        }
        return cardsToBeGiven;
    }

    public void addCardsNoRestrictions(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }

    public ArrayList<Card> takeAllCardsNoRestrictions(){
        ArrayList<Card> c=new ArrayList<>(cards);
        cards.clear();
        return c;
    }

    public boolean isCardsSlotEmpty(){
        return cards.isEmpty();
    }

    public void revealLastCard(){
        if (!cards.isEmpty()){
            cards.getLast().setIsFaceUp(true);
        }

    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "CardsSlot{" +
                "cards=" + cards +
                '}';
    }
}