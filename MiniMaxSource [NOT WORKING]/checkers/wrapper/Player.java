package checkers.wrapper;

import java.awt.Color;

/**
 * Enumeration type representing the players of the game: The one starting at
 * the bottom of the board, namely BELOW, and the other starting at the top of
 * the board, namely ABOVE. These players may also be called "Black" and "Red".
 * 
 * @author 090010514
 */
public enum Player {
	ABOVE, BELOW;
	private static final String BLACK = "Black";
	private static final String RED = "Red";
	private static final Color BLACK_COLOR = Color.BLACK;
	private static final Color RED_COLOR = Color.RED;

	public Color getColor() {
		if (this == ABOVE) {
			return RED_COLOR;
		} else
			return BLACK_COLOR;
	}

	public String toString() {
		if (this == ABOVE)
			return RED;
		else
			return BLACK;
	}

	/**
	 * Create
	 * 
	 * @param str
	 * @return
	 */
	public static Player fromString(String str) {
		if (str.equals(RED))
			return ABOVE;
		else if (str.equals(BLACK))
			return BELOW;
		else
			return null;

	}

	/**
	 * Return the player opposite to this player, i.e. if this method is called
	 * on red, it will return black and if it is called on black it will return
	 * red.
	 * 
	 * @return The player opposite to this player.
	 */
	public Player opposite() {
		if (this == ABOVE) {
			return BELOW;
		} else {
			return ABOVE;
		}
	}
}