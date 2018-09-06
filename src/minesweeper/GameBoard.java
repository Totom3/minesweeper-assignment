/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 *
 * @author Frankie
 */
public class GameBoard extends JFrame implements IGameBoard {

	private static final Font BUTTON_FONT = new Font("monospace", Font.BOLD, 30);

	/**
	 * Reference to the main menu. Used to notify it when the game ends.
	 */
	private final IMainMenu mainMenu;

	/**
	 * The brains of the game. Provides logic for board generation and click
	 * handling.
	 */
	private final IGameLogic gameLogic;

	/**
	 * These buttons will represent each tile, display the status of the tile,
	 * and handle onClick events.
	 */
	private final JButton[][] boardButtons;

	/**
	 * Whether or not control is pressed.
	 */
	private boolean isCtrlPressed;

	/**
	 * Creates a game board object.
	 *
	 * @param mainMenu the main menu reference
	 */
	public GameBoard(IMainMenu mainMenu) {
		this.mainMenu = mainMenu;
		this.gameLogic = new GameLogic();
		this.boardButtons = new JButton[IGameLogic.BOARD_SIZE][IGameLogic.BOARD_SIZE];

		initGUI();
	}

	private void initGUI() {
		final int buttonSize = 100;
		final int screenSize = IGameLogic.BOARD_SIZE * buttonSize;

		JPanel panel = new JPanel(new GridLayout(IGameLogic.BOARD_SIZE, IGameLogic.BOARD_SIZE));
		setContentPane(panel);
		setSize(screenSize, screenSize);
		setLocationRelativeTo(null);
		setTitle("Minesweeper Game");

		// Initialize grid
		for (int i = 0; i < IGameLogic.BOARD_SIZE; ++i) {
			for (int j = 0; j < IGameLogic.BOARD_SIZE; ++j) {
				JButton button = new JButton();
				button.setFont(BUTTON_FONT);
				button.setFocusable(false);
				button.setPreferredSize(new Dimension(buttonSize, buttonSize));
				button.addActionListener(listener(i, j));

				boardButtons[i][j] = button;
				panel.add(button, constr(i, j));
			}
		}

		panel.setFocusable(true);
		panel.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent ke) {
				isCtrlPressed = ke.isControlDown();
			}

			@Override
			public void keyReleased(KeyEvent ke) {
				isCtrlPressed = ke.isControlDown();
			}

			@Override
			public void keyTyped(KeyEvent ke) {
			}

		});
	}

	@Override
	public void onStart() {
		this.setVisible(true);
	}

	private ActionListener listener(int x, int y) {
		return (ae) -> {
			// Check if board has been generated
			if (!gameLogic.isBoardGenerated()) {
				// Start game
				gameLogic.generateBoard(x, y);
			}

			// Flag the button
			if (isCtrlPressed) {
				JButton button = boardButtons[x][y];
				Tile tile = gameLogic.getBoard()[x][y];
				switch (tile.toggleFlag()) {
					case COVERED:
						button.setText("");
						break;
					case FLAGGED:
						button.setText("M");
						break;
				}

				return;
			}

			// Trigger a click
			ClickResult result = gameLogic.onClick(x, y);
			switch (result.getResult()) {
				case ClickResult.WIN:
				case ClickResult.LOSE:
					// End the game
					this.setVisible(false);
					this.mainMenu.onGameEnd(result.getResult() == ClickResult.WIN);
					break;

				case ClickResult.CONTINUE:
					// Uncover tiles
					uncoverTiles(result.getUncoveredTiles());
					break;
				default:
					throw new AssertionError("Unexpected click result " + result.getResult());
			}
		};
	}

	private void uncoverTiles(Set<Tile> tiles) {
		for (Tile tile : tiles) {
			// Update status
			tile.setStatus(TileStatus.UNCOVERED);

			// Update button
			JButton button = boardButtons[tile.getX()][tile.getY()];
			button.setEnabled(false);
			button.setText(tile.getRank() == 0 ? "" : String.valueOf(tile.getRank()));
		}
	}

	private static GridBagConstraints constr(int x, int y) {
		GridBagConstraints constr = new GridBagConstraints();
		constr.gridx = x;
		constr.gridy = y;
		constr.weightx = 1;
		constr.weighty = 1;
		constr.fill = GridBagConstraints.BOTH;
		return constr;
	}

	public static void main(String[] args) {
		MainMenu menu = null;
		GameBoard board = new GameBoard(menu);
		board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board.onStart();
	}
}
