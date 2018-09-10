package minesweeper;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The game board GUI class. Creates, initializes, and maintains the game board.
 *
 * @author Frankie
 * @author Tomer Moran
 */
public class GameBoard extends JFrame {

	private static final Font BUTTON_FONT = new Font("monospace", Font.BOLD, 30);

	/**
	 * Reference to the main menu. Used to notify it when the game ends.
	 */
	private final MainMenu mainMenu;

	/**
	 * The brains of the game. Provides logic for board generation and click
	 * handling.
	 */
	private final GameLogic gameLogic;

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
	public GameBoard(MainMenu mainMenu) {
		this.mainMenu = mainMenu;
		this.gameLogic = new GameLogic();
		this.boardButtons = new JButton[GameLogic.BOARD_SIZE][GameLogic.BOARD_SIZE];

		initGUI();
	}

	/**
	 * Constructs the game board GUI.
	 */
	private void initGUI() {
		final int screenSize = 1000;
		final int buttonSize = screenSize / GameLogic.BOARD_SIZE;

		JPanel panel = new JPanel(new GridLayout(GameLogic.BOARD_SIZE, GameLogic.BOARD_SIZE));
		setContentPane(panel);
		setSize(screenSize, screenSize);
		setLocationRelativeTo(null);
		setTitle("Minesweeper Game");

		// Initialize grid
		for (int i = 0; i < GameLogic.BOARD_SIZE; ++i) {
			for (int j = 0; j < GameLogic.BOARD_SIZE; ++j) {
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

	/**
	 * Opens the board game window and allows the game to start.
	 */
	public void onStart() {
		this.setVisible(true);
	}

	/**
	 * Generates a click listener for the tile button at the given coordinates.
	 *
	 * @param x the x coordinate of the tile.
	 * @param y the y coordinate of the tile.
	 * @return a click handler for the button.
	 */
	private ActionListener listener(int x, int y) {
		return (ae) -> {
			// Check if board has been generated
			if (!gameLogic.isBoardGenerated()) {
				// Start game
				gameLogic.generateBoard(x, y);

				// Flag all mines if in debug mode
				if (MainMenu.DEBUG_MODE) {
					for (int i = 0; i < GameLogic.BOARD_SIZE; ++i) {
						for (int j = 0; j < GameLogic.BOARD_SIZE; ++j) {
							// Flag if mine
							Tile tile = gameLogic.getBoard()[i][j];
							if (tile.isMine()) {
								tile.setStatus(TileStatus.FLAGGED);
								boardButtons[i][j].setText("M");
							}
						}
					}
				}
			} else if (isCtrlPressed) {
				// Flag the tile
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

			if (gameLogic.getBoard()[x][y].getStatus() == TileStatus.FLAGGED) {
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

	/**
	 * Uncovers the given set of tiles and updates the buttons accordingly.
	 *
	 * @param tiles the tiles to uncover.
	 */
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

	/**
	 * Generates a {@link GridBagConstraints} instance for the given
	 * coordinates.
	 *
	 * @param x the x coordinate.
	 * @param y the y coordinate.
	 * @return constraints to be applied to a component in a
	 * {@link GridBagLayout}.
	 */
	private static GridBagConstraints constr(int x, int y) {
		GridBagConstraints constr = new GridBagConstraints();
		constr.gridx = x;
		constr.gridy = y;
		constr.weightx = 1;
		constr.weighty = 1;
		constr.fill = GridBagConstraints.BOTH;
		return constr;
	}
}
