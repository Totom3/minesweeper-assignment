/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 *
 * @author Frankie
 */
public class MainMenu extends JFrame implements IMainMenu {

    private final Font defaultFont = new Font("Courier New", Font.ITALIC, 24);
    private final Font titleFont = new Font("Courier New", Font.BOLD, 48);
    private final JLabel gamesWon; //will display how many games are won
    private final JLabel gamesLost;

    private final JLabel titleLabel;

    protected int winCounter = 0;
    protected int loseCounter = 0;

    private JLabel gameInProgress; //display if a game is in progress or not.
    private final JButton startGame;

    public MainMenu(int width, int height) {
        //NEED TO ADD A TITLE FOR THE MAIN MENU WINDOW
        titleLabel = new JLabel("MINESWEEPER");
        titleLabel.setFont(titleFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gameInProgress = new JLabel("^ Start a new game ^");
        gameInProgress.setFont(defaultFont);
        gameInProgress.setHorizontalAlignment(SwingConstants.CENTER);

        gamesWon = new JLabel("Games won: " + winCounter); //will display the player's win/loss record (winCounter, and loseCounter respectively)
        gamesWon.setHorizontalAlignment(SwingConstants.CENTER); //centers the text
        gamesWon.setFont(defaultFont);
        //gamesWon.setBorder(new LineBorder(Color.blue));

        gamesLost = new JLabel("Games lost: " + loseCounter);
        gamesLost.setHorizontalAlignment(SwingConstants.CENTER);
        gamesLost.setFont(defaultFont);

        setSize(width, height); //sets size of the main menu window
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startGame = new JButton("Start Game");
        //when gameInProgress = true, display: "Game in Progress", AND disable the start button
        //when it's false, display "Start a new game"

        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                GameBoard newGame = new GameBoard(MainMenu.this);
                startGame.setEnabled(false); //disables Start Game button until game is ended (check onGameEnd method)
                gameInProgress.setText("Game is in progress!");
                gameInProgress.setHorizontalAlignment(SwingConstants.CENTER);
                newGame.onStart();
                newGame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we) {
                        onGameEnd(false);
                    }
                });
            }
        });

        Container mainMenu = getContentPane();
        mainMenu.setLayout(new GridLayout(5, 1));
        mainMenu.add(titleLabel);
        mainMenu.add(startGame);
        mainMenu.add(gamesWon);
        mainMenu.add(gamesLost);
        mainMenu.add(gameInProgress);
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

        startGame.setEnabled(true); //re-enables the "start game" button when the game ends

        if (winOrLose) {
            winCounter++;
            gamesWon.setText("Games won: " + winCounter);
        } else {
            loseCounter++;
            gamesLost.setText("Games lost: " + loseCounter);
        }

        try {
            File winSound = new File(winOrLose ? "Win.wav" : "Lose.wav"); //plays victory/defeat sound depending on boolean value of winOrLose
            AudioInputStream aisWin = AudioSystem.getAudioInputStream(winSound);
            Clip winClip = AudioSystem.getClip();
            winClip.open(aisWin);
            winClip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("An error has occured, make sure that this file is in the .wav format, and that the file actually exists on your PC.");
        }
    }

}
