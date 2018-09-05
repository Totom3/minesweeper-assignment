package minesweeper;

/**
 *
 * @author Tomer Moran
 */
public enum TileStatus {
	/**
	 * Represents a tile that is uncovered. Either the player has clicked on it,
	 * or it has been automatically uncovered because of a nearby empty
	 * uncovered tile.
	 */
	UNCOVERED,
	/**
	 * Represents a tile that is covered. The player does not know if it
	 * contains a mine or not.
	 */
	COVERED,
	/**
	 * Represents a covered tile that has been flagged as a mine by the player.
	 * If they click on it, nothing will happen.
	 */
	FLAGGED;
}
