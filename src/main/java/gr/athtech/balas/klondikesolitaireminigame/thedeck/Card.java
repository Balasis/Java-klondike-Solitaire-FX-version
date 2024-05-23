package gr.athtech.balas.klondikesolitaireminigame.thedeck;

public class Card{
    private Rank rank;
    private final Suit suit;
    private Boolean isRevealed;
    private String color;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.isRevealed =false;
        if(this.suit==Suit.HEARTS || this.suit==Suit.DIAMONDS){
            this.color="red";
        }else{
            this.color="black";
        }
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public String getColor(){
        return color;
    }

    public Boolean getIsRevealed() {
        return isRevealed;
    }

    public void reveal() {
        this.isRevealed = true;
    }

    public void hide() {
        this.isRevealed = false;
    }

    //Do not remove the setRank; used at setting joker cards rank later on.
    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String toString() {
        return " Rank: " + this.rank + ", Suit: " + this.suit
                + (isRevealed ? " Revealed" : " Hidden");
    }
}
