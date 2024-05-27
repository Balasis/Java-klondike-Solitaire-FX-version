package gr.athtech.balas.klondikesolitaireminigame.board;

import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;

import java.util.ArrayList;

public class CardsSlot {
    private final ArrayList<Card> cards;

    public CardsSlot(){
        cards=new ArrayList<>();
    }

    //API
    public ArrayList<Card> takeAllCardsNoRestrictions(){
        ArrayList<Card> c=new ArrayList<>(cards);
        cards.clear();
        return c;
    }

    //Overrides
    public Card getLastCard() {
        return cards.getLast();
    }

    public Card getFirstCard() {
        return cards.getFirst();
    }

    public boolean isCardInSlot(Card card){
        return cards.contains(card);
    }

    public void addCardsNoRestrictions(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }

    public boolean isCardsSlotEmpty(){
        return cards.isEmpty();
    }

    public void revealLastCard(){
        if (!cards.isEmpty()){
            cards.getLast().setIsFaceUp(true);
        }
    }

    // Getters
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



/*
    public ArrayList<Card> getMultipleCardsFromTop(int numberOfCards) {
        ArrayList<Card> cardsToBeGiven=new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (i>=numberOfCards){
                break;
            }   //first card of the Top is index card.size() - 1
            cardsToBeGiven.addFirst(cards.get( (cards.size()-i)-1) );
        }
        return cardsToBeGiven;
    }

    public ArrayList<Card> getMultipleCardsFromBottom(int numberOfCards) {
        ArrayList<Card> cardsToBeGiven=new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (i>=numberOfCards){
                break;
            }//first card of the bottom is the index 0
            cardsToBeGiven.add(cards.get(i));
        }
        return cardsToBeGiven;
    }
 */