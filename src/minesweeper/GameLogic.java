package minesweeper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Tomer Moran
 */
public class GameLogic implements IGameLogic {

	private boolean generated;
	private int coveredTiles;
	private final Tile[][] board;

	public GameLogic() {
		this.board = new Tile[GameLogic.BOARD_SIZE][GameLogic.BOARD_SIZE];
	}

	@Override
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
						if (!isCoordValid(i) || !isCoordValid(j)) {
							continue;
						}

						// Increment rank (nearby bomb)
						++rank[i + coords.x][j + coords.y];
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

	@Override
	public Tile[][] getBoard() {
		return board;
	}

	@Override
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
		collectTilesToBeUncovered(clickedTile, uncovered);

		// Check for win
		this.coveredTiles -= uncovered.size();
		return new ClickResult(coveredTiles == 0 ? ClickResult.WIN : ClickResult.CONTINUE, uncovered);
	}

	private void collectTilesToBeUncovered(Tile tile, Set<Tile> set) {
		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				// Check that coordinates are valid
				if (!isCoordValid(i) || !isCoordValid(j)) {
					continue;
				}

				// Get tile relative to this
				Tile t = board[i + tile.getX()][j + tile.getY()];

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
		return x >= 0 && x < 8;
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
