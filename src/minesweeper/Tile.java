package minesweeper;

/**
 * Represents a board tile. Each tile has two properties (except for their
 * coordinates):
 * <ul>
 * <li>A status: COVERED, UNCOVERED, or FLAGGED. Represents the state of the
 * tile and how it should be displayed. The status of a tile can change
 * throughout the game.</li>
 * <li>A rank: the number of nearby bombs, or -1 if the tile is a bomb itself.
 * The rank of a tile cannot change throughout a game.</li>
 * </ul>
 *
 *
 * @author Frankie
 * @author Tomer Moran
 */
public class Tile {

	private TileStatus status;
	private final int rank;
	private final int x, y;

	/**
	 * Instantiates this tile with status {@link TileStatus#COVERED} and the
	 * given coordinates and rank.
	 *
	 * @param x the x coordinate of this tile. Must be between 0 (inclusive) and
	 * {@link IGameLogic#BOARD_SIZE} (exclusive).
	 * @param y the y coordinate of this tile. Must be between 0 (inclusive) and
	 * {@link IGameLogic#BOARD_SIZE} (exclusive).
	 * @param rank the rank of this tile.
	 */
	public Tile(int x, int y, int rank) {
		this(x, y, rank, TileStatus.COVERED);
	}

	/**
	 * Instantiates this tile with the given status, rank and coordinates.
	 *
	 * @param x the x coordinate of this tile. Must be between 0 (inclusive) and
	 * {@link IGameLogic#BOARD_SIZE} (exclusive).
	 * @param y the y coordinate of this tile. Must be between 0 (inclusive) and
	 * {@link IGameLogic#BOARD_SIZE} (exclusive).
	 * @param rank the rank of this tile.
	 * @param status the status of this tile.
	 */
	public Tile(int x, int y, int rank, TileStatus status) {
		this.status = status;
		this.rank = rank;
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x coordinate of this tile.
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y coordinate of this tile.
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the status of this tile. This can change as the game progresses.
	 */
	public TileStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status of this tile.
	 *
	 * @param status the new status to set. Must not be {@code null}.
	 */
	public void setStatus(TileStatus status) {
		this.status = status;
	}

	/**
	 * Toggles the flag status of this tile between {@link TileStatus#COVERED}
	 * and {@link TileStatus#FLAGGED}. If the status is
	 * {@link TileStatus#UNCOVERED}, {@link IllegalStateException} is thrown.
	 *
	 * @return the new status.
	 */
	public TileStatus toggleFlag() {
		switch (status) {
			case COVERED:
				this.status = TileStatus.FLAGGED;
				break;
			case FLAGGED:
				this.status = TileStatus.COVERED;
				break;
			case UNCOVERED:
				throw new IllegalStateException();
			default:
				throw new AssertionError("unexpected status " + status);
		}

		return status;
	}

	/**
	 * Returns the rank of this tile. If zero or more, this represents the
	 * number of adjacent mines. If negative, this tile hides a mine. The rank
	 * of a tile does not change over time.
	 *
	 * @return the rank of this tile. This remains constant throughout a game.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Returns whether or not this tile hides a mine. This is equivalent to the
	 * expression {@code rank < 0}.
	 *
	 * @return {@code true} if this tile is a mine, {@code false} otherwise.
	 */
	public boolean isMine() {
		return rank < 0;
	}
}
