package gr.athtech.balas.klondikesolitaireminigame;

import gr.athtech.balas.klondikesolitaireminigame.thedeck.Card;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardView extends ImageView {
   private final Image revealedImage;
    private final Image hiddenImage;
    private final Card card;

    public CardView(Card card) {
        this.card = card;
        // Assuming these images are located in the resource folder
        this.revealedImage = new Image(getClass().getResourceAsStream("/images/"+card.getSuit()+card.getRank().getValue()+".png"));
        this.hiddenImage = new Image(getClass().getResourceAsStream("/images/BACK.png"));

        updateImage();
    }

    public void updateImage() {
        if (card.getIsRevealed()) {
            setImage(revealedImage);
        } else {
            setImage(hiddenImage);
        }
    }

}
