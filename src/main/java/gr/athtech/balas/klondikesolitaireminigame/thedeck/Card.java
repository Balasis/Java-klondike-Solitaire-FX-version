package gr.athtech.balas.klondikesolitaireminigame.thedeck;

public class Card {
    private Rank rank;
    private final Suit suit;
    private Boolean isFaceUp;
    private final CardColor cardColor;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.isFaceUp = false;
        this.cardColor = (this.suit == Suit.HEARTS || this.suit == Suit.DIAMONDS) ? CardColor.RED : CardColor.BLACK;
    }

    // Getters
    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Boolean getIsFaceUp() {
        return isFaceUp;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    // Setters
    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setIsFaceUp(Boolean b) {
        this.isFaceUp = b;
    }

    //Overrides
    @Override
    public String toString() {
        return " Rank: " + this.rank + ", Suit: " + this.suit
                + (isFaceUp ? " Revealed " : " Hidden" + " Card color: ")
                + this.cardColor;
    }

}