package minesweeper;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Frankie
 */
public class MainMenu extends JFrame {

	public static final boolean DEBUG_MODE = true;
	private static final Font TITLE_FONT = new Font("Courier New", Font.BOLD, 48);
	private static final Font DEFAULT_FONT = new Font("Courier New", Font.ITALIC, 24);

	private final JButton startGame;
	private final JMenuBar menuBar;
	private final JMenu menu;
	private final JMenuItem itemDebug;
	private final JLabel titleLabel;
	private final JLabel gamesWonLabel;
	private final JLabel gamesLostLabel;
	private final JLabel gameInProgressLabel;

	private int winCounter;
	private int loseCounter;

	public MainMenu() {
		// Initialize components
		this.startGame = new JButton("Start Game");
		this.titleLabel = new JLabel("MINESWEEPER");
		this.gamesWonLabel = new JLabel("Games won: " + winCounter);
		this.gamesLostLabel = new JLabel("Games lost: " + loseCounter);
		this.gameInProgressLabel = new JLabel("^ Start a new game ^");
		this.menuBar = new JMenuBar();
		this.menu = new JMenu("Options");
		this.itemDebug = new JMenuItem("Enable Debug Mode");
		menuBar.add(menu);
		menu.add(itemDebug);
		this.setJMenuBar(menuBar);
		initGUI();
	}

	private void initGUI() {
		titleLabel.setFont(TITLE_FONT);
		gamesWonLabel.setFont(DEFAULT_FONT);
		gamesLostLabel.setFont(DEFAULT_FONT);
		gameInProgressLabel.setFont(DEFAULT_FONT);

		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gamesWonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gamesLostLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gameInProgressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		
		//when gameInProgress = true, display: "Game in Progress", AND disable the start button
		//when it's false, display "Start a new game"
		startGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				GameBoard newGame = new GameBoard(MainMenu.this);
				
				// Disable Start Game button until game is ended (check onGameEnd method)
				startGame.setEnabled(false); 
				gameInProgressLabel.setText("Game is in progress!");
				gameInProgressLabel.setHorizontalAlignment(SwingConstants.CENTER);
				
				// Start new game
				newGame.onStart();
				newGame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent we) {
						onGameEnd(false);
					}
				});
			}
		});
		
		itemDebug.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
			//change debug value
        }
    });
		JPanel pane = new JPanel(new GridLayout(5, 1));
		pane.add(titleLabel);
		pane.add(startGame);
		pane.add(gamesWonLabel);
		pane.add(gamesLostLabel);
		pane.add(gameInProgressLabel);
		setContentPane(pane);

		setSize(640, 480);
		setLocationRelativeTo(null);
		setTitle("Minesweeper Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		MainMenu window = new MainMenu();
		window.setVisible(true);
	}

	/**
	 * Triggers when game is finished. When boolean variable winOrLose is true,
	 * the player has won, when it's false, the player has lost.
	 *
	 * @param winOrLose
	 */
	public void onGameEnd(boolean winOrLose) {

		startGame.setEnabled(true); //re-enables the "start game" button when the game ends

		if (winOrLose) {
			winCounter++;
			gamesWonLabel.setText("Games won: " + winCounter);
		} else {
			loseCounter++;
			gamesLostLabel.setText("Games lost: " + loseCounter);
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
