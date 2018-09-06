package minesweeper;

/**
 *
 * @author Tomer Moran
 */
public interface IGameLogic {

	int BOARD_SIZE = 8;
	int MINES_COUNT = 10;

	/**
	 * Generates the board.
	 *
	 * @param clickX the x coordinate the player clicked.
	 * @param clickY the y coordinate the player clicked.
	 */
	void generateBoard(int clickX, int clickY);

	/**
	 * @return {@code true} if the board has been generated; {@code false}
	 * otherwise.
	 */
	boolean isBoardGenerated();

	/**
	 * @return the game board. This is the actual copy and not a mirror; updates
	 * made to the board reflect in the returned value.
	 */
	Tile[][] getBoard();

	/**
	 * Handles a click on the given tile. The result of the click is stored in
	 * the return value. A click can lead to (1) a loss; (2) a win; (3) the
	 * normal continuation of the game, with the possibility of some tiles being
	 * uncovered.
	 *
	 * @param x the x coordinate the player clicked.
	 * @param y the y coordinate the player clicked.
	 * @return
	 */
	ClickResult onClick(int x, int y);
}
