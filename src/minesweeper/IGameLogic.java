package minesweeper;

/**
 *
 * @author Tomer Moran
 */
public interface IGameLogic {
	int BOARD_SIZE = 8;
	
	void generateBoard(int x, int y);
	
	Tile[][] getBoard();
	
	ClickResult onClick(int x, int y);
}
