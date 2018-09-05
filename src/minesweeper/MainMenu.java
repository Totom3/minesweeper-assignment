/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Frankie
 */
public class MainMenu extends JFrame implements IMainMenu{
    int height;
    int width;
    
    JLabel gamesWon = new JLabel(); //will display how many games are won
    JLabel gamesLost = new JLabel();
    
    JLabel gameInProgress = new JLabel(); //display if a game is in progress or not.
    JButton startGame = new JButton("Start Game");
    
    MainMenu (int width, int height) {
        setSize(width, height);
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //create new game
            }
        });
    }
    public static void main(String[] args) {
        MainMenu window = new MainMenu(640, 480);
        window.setVisible(true);
    }
    
    @Override
    public void onGameEnd(boolean winOrLose) {
        if (winOrLose){
            //display victory message/sound
        }
        else {
            //initiate death sound
        }
    }
    
}
