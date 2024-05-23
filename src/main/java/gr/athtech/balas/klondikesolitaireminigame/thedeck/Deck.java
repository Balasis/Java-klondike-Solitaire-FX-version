package gr.athtech.balas.klondikesolitaireminigame.thedeck;

import java.util.ArrayList;
import java.util.Iterator;

public class Deck {
    private final ArrayList<Card> theDeck;
    private int jokersCounter=0;

    public Deck(){
        theDeck=new ArrayList<>();
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
            theDeck.add(new Card(null,suit));
        }else{
            theDeck.add(new Card(rank,suit));
        }
    }

    private boolean areAllJokerCardsLoadedIntoDeck(){
        int jokersCapacity = 2;
        return jokersCounter > jokersCapacity;
    }


    public void shuffle(int numberOfShuffles){
        if (theDeck.size()<2){
            return;
        }//we grab each card of the deck(remove it from deck),and reEnter into it in a random index
        for (int i = 0; i < numberOfShuffles; i++) {//we also repeat this for numberOfShuffles Given
            for (int k = 0; k < theDeck.size(); k++) {
                Card grabbedCard=theDeck.remove( Operations.getRandomNumber( 0 , theDeck.size() ) );
                theDeck.add(Operations.getRandomNumber( 0 , theDeck.size() ) , grabbedCard);
            }
        }
    }

    public void removeTheJokers() {
        Iterator<Card> iterator = theDeck.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();
            if (card.getSuit() == Suit.JOKER) {
                iterator.remove();
            }
        }
    }

    public ArrayList<Card> getTheDeck(){
        return this.theDeck;
    }

    public boolean isEmpty(){
       return theDeck.isEmpty();
    }

    public ArrayList<Card> takeAllCurrentCards(){
        ArrayList<Card> cardsToBeReturned=new ArrayList<>(theDeck);
        theDeck.clear();
        return cardsToBeReturned;
    }

    public ArrayList<Card> takeCards(int numberOfCards){
        ArrayList<Card> cardsToBeGiven=new ArrayList<>();
        for (int i = 0; i < theDeck.size(); i++) {
            if (i>=numberOfCards){//counters starts from 0...
                break;
            }
            cardsToBeGiven.addFirst(theDeck.remove( (theDeck.size()-i)-1) );
        }
        return cardsToBeGiven;
    }

    public String toString(){
        StringBuilder s= new StringBuilder();
        if (!theDeck.isEmpty()) {
            for (Card c : theDeck) {
                s.append(theDeck.indexOf(c)).append(")Rank: ").append(c.getRank()).append(" Suit: ").append(c.getSuit()).append("\n");
            }
        }else{
            s.append("Deck is empty");
        }
        return s.toString();
    }
}