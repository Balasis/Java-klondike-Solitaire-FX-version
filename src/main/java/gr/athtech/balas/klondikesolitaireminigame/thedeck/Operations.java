package gr.athtech.balas.klondikesolitaireminigame.thedeck;

public class Operations {
    static int getRandomNumber(int min, int max){
        return (int) (Math.random() * (max - min) ) + min;
    }
}
