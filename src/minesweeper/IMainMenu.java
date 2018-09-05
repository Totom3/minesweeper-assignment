/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

/**
 *
 * @author Frankie
 */
public interface IMainMenu {
    /**
     * will trigger when game ends
     * @param win
     * @param lose 
     */
    void onGameEnd(boolean winOrLose); 
    
}
