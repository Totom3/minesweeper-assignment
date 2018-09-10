package minesweeper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Handles the board generation and the logic behind tile clicks. Communicates
 * the results to {@link GameBoard} so that it can display the board
 * appropriately.
 *
 * @author Frankie
 * @author Tomer Moran
 */
public class GameLogic {

	public static final int BOARD_SIZE = 8;
	public static final int MINES_COUNT = 10;

	private int coveredTiles;
	private boolean generated;
	private final Tile[][] board;

	public GameLogic() {
		this.board = new Tile[GameLogic.BOARD_SIZE][GameLogic.BOARD_SIZE];
	}

	/**
	 * Generates the board.
	 *
	 * @param clickX the x coordinate the player clicked.
	 * @param clickY the y coordinate the player clicked.
	 */
	public void generateBoard(int clickX, int clickY) {
		if (generated) {
			throw new IllegalStateException("board already generated");
		}

		// Initialize variables
		Random rand = new Random();
		int[][] rank = new int[GameLogic.BOARD_SIZE][GameLogic.BOARD_SIZE];
		Set<Coordinates> bombs = new HashSet<>();
		Coordinates clickCoords = new Coordinates(clickX, clickY);

		// Generate bomb locations
		while (bombs.size() < GameLogic.MINES_COUNT) {
			// Generate random location
			Coordinates coords = new Coordinates(rand.nextInt(GameLogic.BOARD_SIZE), rand.nextInt(GameLogic.BOARD_SIZE));

			// If valid, update the rank of nearby tiles
			// Make sure the clicked coordinate is not a bomb and is not adjacent to a bomb
			if (!coords.isAdjacent(clickCoords) && bombs.add(coords)) {
				// Update rank of adjacent tiles
				for (int i = -1; i <= 1; ++i) {
					for (int j = -1; j <= 1; ++j) {
						int x1 = i + coords.x;
						int y1 = j + coords.y;
						if (!isCoordValid(x1) || !isCoordValid(y1)) {
							continue;
						}

						// Check that we are not erasing a bomb
						if (rank[x1][y1] == -1) {
							continue;
						}

						// Increment rank
						++rank[x1][y1];
					}
				}

				// Set rank to -1 to declare a bomb in this tile
				rank[coords.x][coords.y] = -1;
			}
		}

		// Initialize board
		for (int x = 0; x < BOARD_SIZE; ++x) {
			for (int y = 0; y < BOARD_SIZE; ++y) {
				board[x][y] = new Tile(x, y, rank[x][y]);
			}
		}

		this.generated = true;
		this.coveredTiles = (BOARD_SIZE * BOARD_SIZE) - MINES_COUNT;
	}

	/**
	 * @return {@code true} if the board has been generated; {@code false}
	 * otherwise.
	 */
	public boolean isBoardGenerated() {
		return generated;
	}

	/**
	 * @return the game board. This is the actual copy and not a mirror; updates
	 * made to the board reflect in the returned value.
	 */
	public Tile[][] getBoard() {
		return board;
	}

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
	public ClickResult onClick(int x, int y) {
		Tile clickedTile = board[x][y];
		int rank = clickedTile.getRank();

		// If a mine was clicked, player loses
		if (rank < 0) {
			return new ClickResult(ClickResult.LOSE, new HashSet<>());
		}

		// Gather tiles to be uncovered
		Set<Tile> uncovered = new HashSet<>();
		uncovered.add(clickedTile);
		if (clickedTile.getRank() == 0) {
			collectTilesToBeUncovered(clickedTile, uncovered);
		}

		// Check for win
		this.coveredTiles -= uncovered.size();
		return new ClickResult(coveredTiles == 0 ? ClickResult.WIN : ClickResult.CONTINUE, uncovered);
	}

	private void collectTilesToBeUncovered(Tile tile, Set<Tile> set) {
		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				// Check that coordinates are valid
				int x1 = i + tile.getX();
				int y1 = j + tile.getY();
				if (!isCoordValid(x1) || !isCoordValid(y1)) {
					continue;
				}

				// Get tile relative to this
				Tile t = board[x1][y1];

				// Do not proceed if mine or already uncovered
				if (t.getRank() < 0 || t.getStatus() != TileStatus.COVERED) {
					continue;
				}

				if (!set.add(t)) {
					continue;
				}

				// Recurvsive check
				if (t.getRank() == 0) {
					collectTilesToBeUncovered(t, set);
				}
			}
		}
	}

	private static boolean isCoordValid(int x) {
		return x >= 0 && x < GameLogic.BOARD_SIZE;
	}

	private static final class Coordinates {

		final int x, y;

		Coordinates(int x, int y) {
			this.x = x;
			this.y = y;
		}

		boolean isAdjacent(Coordinates other) {
			return Math.abs(this.x - other.x) <= 1
					&& Math.abs(this.y - other.y) <= 1;
		}

		@Override
		public int hashCode() {
			int hash = 5;
			hash = 53 * hash + this.x;
			hash = 53 * hash + this.y;
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Coordinates)) {
				return false;
			}

			Coordinates other = (Coordinates) obj;
			return this.x == other.x && this.y == other.y;
		}

	}
}
