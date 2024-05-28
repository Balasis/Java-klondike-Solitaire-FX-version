package gr.athtech.balas.klondikesolitaireminigame;

import gr.athtech.balas.klondikesolitaireminigame.board.TableSlot;

import java.util.Scanner;

public class LetsDoSomeTesting {

    static Scanner myScanObj=new Scanner(System.in);

    public static void main(String[] args) {
        KlondikeSolitaireProgram theGame=new KlondikeSolitaireProgram();
        theGame.setUpTheGame();
        TableSlot[] tablesSlots=theGame.getTableSlots();

        for (int i = 0; i < tablesSlots.length; i++) {
            System.out.println(tablesSlots[i].getCards().getLast());
        }
        if(theGame.moveCards(tablesSlots[myScanObj.nextInt()-1] ,tablesSlots[myScanObj.nextInt()-1] ,2)){
            System.out.println("done");
        }else{
            System.out.println("not Done");
        }


        for (int i = 0; i < tablesSlots.length; i++) {
            System.out.println(tablesSlots[i]);
        }
    }
}
