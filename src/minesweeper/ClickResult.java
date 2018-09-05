package minesweeper;

import java.util.Set;

/**
 *
 * @author Tomer Moran
 */
public class ClickResult {

	public static final int WIN = 1;
	public static final int CONTINUE = 0;
	public static final int LOSE = -1;

	private final int result;
	private final Set<Tile> uncoveredTiles;

	public ClickResult(int result, Set<Tile> uncoveredTiles) {
		this.result = result;
		this.uncoveredTiles = uncoveredTiles;
	}

	/**
	 *
	 * @return the result of this click. Compare it to the constants defined in
	 * this class.
	 */
	public int getResult() {
		return result;
	}

	/**
	 * @return a set containing the tiles to be uncovered as a result of this
	 * click.
	 */
	public Set<Tile> getUncoveredTiles() {
		return uncoveredTiles;
	}

}
