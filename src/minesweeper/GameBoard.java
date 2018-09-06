/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Frankie
 */
public class GameBoard extends JFrame implements IGameBoard {

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
	private final JButton[][] boardButtons = new JButton[IGameLogic.BOARD_SIZE][IGameLogic.BOARD_SIZE];

	/**
	 * Creates a game board object.
	 *
	 * @param mainMenu the main menu reference
	 */
	public GameBoard(IMainMenu mainMenu) {
		this.mainMenu = mainMenu;
		this.gameLogic = new GameLogic();
		
		//initGUI();
	}
	
	

	@Override
	public void onStart() {

	}

}
