package checkers;

import java.awt.Color;

/**
 * This class defines constants which are used elsewhere.
 * 
 * @author 090010514
 */
public class Constants {
	/**
	 * Sleep time between each frame update in animated moves.
	 */
	public static final long ANIMATION_SLEEP = 20;
	// the title of the game window
	public static final String GAME_NAME = "Checkers";
	public static final Color BOARD_COLOR_1 = Color.WHITE;
	public static final Color BOARD_COLOR_2 = new Color(100, 60, 0);
	// number of rows on the board
	public static final int ROWS = 8;
	// number of columns on the board
	public static final int COLS = 8;

	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
}
