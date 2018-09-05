/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Frankie
 */

public class GameBoard  extends JFrame implements IGameBoard{

    int width;
    int height;
    /**
     * These buttons will represent each tile, display the status of the tile,  and handle onClick events
    */
    JButton[][] boardTiles = new JButton[8][8];
    
    //to refer to board length, don't use 8, use BOARD_SIZE
    /**
     * creates game board object
     */
    GameBoard(int width, int height) { 
        setSize(width, height);
    }
         
    
    @Override
    public void onStart() { 
        //create new game of GameBoard
    }
    
    /**
     * Main method will deal with the instantiation of the game, setting of window size, etc.
     * @param args
     */
    public static void main(String[] args) {
        GameBoard game = new GameBoard(1000, 1000);
        game.setVisible(true);
    }
    
}
