/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Frankie
 */
public class MainMenu extends JFrame implements IMainMenu {

    int height;
    int width;

    JLabel gamesWon = new JLabel(); //will display how many games are won
    JLabel gamesLost = new JLabel();

    JLabel gameInProgress = new JLabel(); //display if a game is in progress or not.
    JButton startGame = new JButton("Start Game");

    MainMenu(int width, int height) {
        setSize(width, height);
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                GameBoard newGame = new GameBoard(1000,1000);
                //create new game
            }
        });
    }

    public static void main(String[] args) {
        MainMenu window = new MainMenu(640, 480);
        window.setVisible(true);
    }

    /**
     * Triggers when game is finished. When boolean variable winOrLose is true,
     * the player has won, when it's false, the player has lost.
     *
     * @param winOrLose
     */
    @Override
    public void onGameEnd(boolean winOrLose) {
        try {
            File winSound = new File(winOrLose ? "Win.wav" : "Lose.wav"); //display victory/loss sound
            AudioInputStream aisWin = AudioSystem.getAudioInputStream(winSound);
            Clip winClip = AudioSystem.getClip();
            winClip.open(aisWin);
            winClip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("An error has occured, make sure that this file is in the .wav format, and that exists on your PC.");
        }
    }

}
