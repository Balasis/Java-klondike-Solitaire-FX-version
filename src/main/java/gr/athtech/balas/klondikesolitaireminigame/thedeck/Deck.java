package gr.athtech.balas.klondikesolitaireminigame.thedeck;

import java.util.ArrayList;
import java.util.Iterator;

public class Deck {
    private final ArrayList<Card> cards;
    private int jokersCounter=0;

    public Deck(){
        cards =new ArrayList<>();
        populateDeck();
    }

    private void populateDeck(){
        //for each suit put according number of cards and values into deck.
        for(Suit suit:Suit.values()){
            for(Rank rank:Rank.values()){
                createDeckCard(rank,suit);
            }
        }
    }

    private void createDeckCard(Rank rank, Suit suit){
        if(suit==Suit.JOKER){
            jokersCounter++;
            if (areAllJokerCardsLoadedIntoDeck()){
                return;
            }
            cards.add(new Card(null,suit));
        }else{
            cards.add(new Card(rank,suit));
        }
    }

    private boolean areAllJokerCardsLoadedIntoDeck(){
        int jokersCapacity = 2;
        return jokersCounter > jokersCapacity;
    }


    public void shuffle(int numberOfShuffles){
        if (cards.size()<2){
            return;
        }//we grab each card of the deck(remove it from deck),and reEnter into it in a random index
        for (int i = 0; i < numberOfShuffles; i++) {//we also repeat this for numberOfShuffles Given
            for (int k = 0; k < cards.size(); k++) {
                Card grabbedCard= cards.remove( Operations.getRandomNumber( 0 , cards.size() ) );
                cards.add(Operations.getRandomNumber( 0 , cards.size() ) , grabbedCard);
            }
        }
    }

    public void removeTheJokers() {
        Iterator<Card> iterator = cards.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();
            if (card.getSuit() == Suit.JOKER) {
                iterator.remove();
            }
        }
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }

    public boolean isEmpty(){
       return cards.isEmpty();
    }

    public ArrayList<Card> takeAllCurrentCards(){
        ArrayList<Card> cardsToBeReturned=new ArrayList<>(cards);
        cards.clear();
        return cardsToBeReturned;
    }

    public ArrayList<Card> takeCards(int numberOfCards){
        ArrayList<Card> cardsToBeGiven=new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (i>=numberOfCards){//counters starts from 0...
                break;
            }
            cardsToBeGiven.addFirst(cards.remove( (cards.size()-i)-1) );
        }
        return cardsToBeGiven;
    }

    public String toString(){
        StringBuilder s= new StringBuilder();
        if (!cards.isEmpty()) {
            for (Card c : cards) {
                s.append(cards.indexOf(c)).append(")Rank: ").append(c.getRank()).append(" Suit: ").append(c.getSuit()).append("\n");
            }
        }else{
            s.append("Deck is empty");
        }
        return s.toString();
    }
}