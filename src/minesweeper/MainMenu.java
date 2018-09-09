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
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Frankie
 */
public class MainMenu extends JFrame {

	public static boolean DEBUG_MODE = false;
	private static final Font TITLE_FONT = new Font("Courier New", Font.BOLD, 48);
	private static final Font DEFAULT_FONT = new Font("Courier New", Font.ITALIC, 24);

	private final JButton startGame;
	private final JMenuBar menuBar;
	private final JMenu menu;
	private final JCheckBoxMenuItem itemDebug;
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
		this.itemDebug = new JCheckBoxMenuItem("Enable Debug Mode");
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
		
		
		
		//When gameInProgress = true, display: "Game in Progress", AND disable the start button.
		//When it's false, display "Start a new game".
		startGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				GameBoard newGame = new GameBoard(MainMenu.this);
				
				
				startGame.setEnabled(false); // Disable Start Game button until game is ended (check onGameEnd method)
				itemDebug.setEnabled(false); // Disable menu item when game is in progress
				gameInProgressLabel.setText("Game is in progress!"); //This will change the text on the bottom (from its original: "^ Start a new game ^")
				gameInProgressLabel.setHorizontalAlignment(SwingConstants.CENTER);
				
				// Start new game
				newGame.onStart();
				newGame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent we) {
						onGameEnd(false); //changes winOrLose variable to false (indicating a lost game) when window is X-ed out						
					}
				});
			}
		});
		
		itemDebug.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				DEBUG_MODE = !DEBUG_MODE;
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
		setLocationRelativeTo(null); //centers the window on the computer screen
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
		itemDebug.setEnabled(true); //re enables button when game is closed
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
