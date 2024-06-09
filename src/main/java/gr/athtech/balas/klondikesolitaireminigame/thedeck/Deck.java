package gr.athtech.balas.klondikesolitaireminigame.thedeck;

import java.util.ArrayList;
//import java.util.Iterator;
import java.util.Random;

public class Deck {
    private final Random random;
    private final ArrayList<Card> cards;
    private int jokersCounter=0;

    public Deck(){
        random=new Random();
        cards =new ArrayList<>();
        populateDeck();
    }

    // API s
    public void removeTheJokers() {
//        Iterator<Card> iterator = cards.iterator();
//        while (iterator.hasNext()) {
//            Card card = iterator.next();
//            if (card.getSuit() == Suit.JOKER) {
//                iterator.remove();
//            }
//        }
        for (int i = 0; i < cards.size();) {
            Card currentCard=cards.get(i);
            if (currentCard.getSuit() == Suit.JOKER) {
                cards.remove(currentCard);
            }else{
                i++;
            }
        }
    }

    public void shuffle(){
        if (cards.size() < 2){
            return;
        }// We grab each card of the deck( remove it from deck ), and reEnter into it in a random index
        for (int i = 0; i < 10; i++) {// Increase i<Counter if you want better shuffle
            for (int k = 0; k < cards.size(); k++){
                Card grabbedCard= cards.remove( random.nextInt(cards.size()) );
                cards.add( random.nextInt( cards.size() ) , grabbedCard);
            }
        }
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }

    public boolean isThereAJokerInDeck(){// Ok need to study more about streams...but seems a nice simple way.
        return cards.stream().anyMatch(card -> card.getSuit() == Suit.JOKER);
    }

    public ArrayList<Card> takeAllDeckCards(){
        ArrayList<Card> cardsToBeReturned=new ArrayList<>(cards);
        cards.clear();
        return cardsToBeReturned;
    }

    public ArrayList<Card> takeNumberOfDeckCards(int numberOfCards){
        ArrayList<Card> cardsToBeGiven=new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (i>=numberOfCards){// Counters starts from 0...
                break;
            }
            cardsToBeGiven.addFirst(cards.remove( (cards.size()-i)-1) );
        }
        return cardsToBeGiven;
    }

    public Card takeSpecificCard(Suit s, Rank r){
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getSuit() == s && cards.get(i).getRank() == r){
                return cards.remove(i);
            }
        }
        return null;
    }

    public Card takeAnUnrankedJokerCard(){
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getSuit() == Suit.JOKER && cards.get(i).getRank() == null){
                return cards.remove(i);
            }
        }
        return null;
    }


    // Privates
    private void populateDeck(){
        // For each suit put according number of cards and values into deck.
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

    //  Getters
    public ArrayList<Card> getCards(){
        return this.cards;
    }

    // //Overrides
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