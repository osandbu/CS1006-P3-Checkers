package checkers.wrapper;

import java.awt.Graphics;

import checkers.Board;
import checkers.CheckersApplet;
import checkers.Constants;

/**
 * Wrapper used to store information about a piece on the board. This includes
 * the player two owns it, it's location on the board and whether or not it is a
 * king.
 * 
 * @author 090010514
 */
public class Piece extends Circle {
	public static final int RADIUS = 12;
	private final Player player;
	private int row;
	private int col;
	private boolean king;
	private CheckersApplet applet;

	/**
	 * Create a new Piece object with a given player, row, column and applet.
	 * 
	 * @param player
	 *            The player owning this piece.
	 * @param row
	 *            The row number of the piece.
	 * @param col
	 *            The column number of the piece.
	 * @param applet
	 *            The CheckersApplet in which it exists.
	 */
	public Piece(Player player, int row, int col, CheckersApplet applet) {
		super(getColX(col), getRowY(row), RADIUS);
		this.player = player;
		this.row = row;
		this.col = col;
		king = false;
		this.applet = applet;
	}

	public Player getPlayer() {
		return player;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	/**
	 * Get the cell location of the Piece.
	 * 
	 * @return The cell location of the Piece.
	 */
	public Cell getCell() {
		return new Cell(row, col);
	}

	/**
	 * Draws this piece onto a graphics context. Its color will be specific for
	 * the player who owns it.
	 * 
	 * @param g
	 *            A graphics context.
	 */
	public void draw(Graphics g) {
		g.setColor(player.getColor());
		super.draw(g);
		if (isKing()) {
			g.setColor(player.opposite().getColor());
			int leftX = getX();
			int midX = leftX + RADIUS;
			int topY = getY();
			int midY = topY + RADIUS;
			g.fillRect(midX - 1, topY, 3, RADIUS * 2);
			g.fillRect(leftX, midY - 1, RADIUS * 2, 3);
		}
	}

	/**
	 * Move this piece to another cell, then repaint.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 */
	public void move(int row, int col) {
		this.row = row;
		this.col = col;
		int x = getColX(col);
		int y = getRowY(row);
		setX(x);
		setY(y);
	}

	/**
	 * Animate the movement of a piece to a cell of a given row and column.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 */
	public void animateMove(int row, int col) {
		this.row = row;
		this.col = col;
		int destX = getColX(col);
		int destY = getRowY(row);
		int dx = destX - getX();
		int dy = destY - getY();
		if (dx == 0 || dy == 0)
			new Exception().printStackTrace();
		// absolute value of dx and dy is always the same
		int adx = Math.abs(dx);
		dy = 2 * dy / adx;
		dx = 2 * dx / adx;
		while (destX - getX() > dx || destY - getY() > dy) {
			setX(getX() + dx);
			setY(getY() + dy);
			applet.repaint(this);
			try {
				Thread.sleep(Constants.ANIMATION_SLEEP);
			} catch (InterruptedException e) {
			}
		}
		setX(destX);
		setY(destY);
		applet.repaint();
	}

	/**
	 * Get the x-coordinate of the left side of a column.
	 * 
	 * @param col
	 *            A column number.
	 * @return The x-coordinate of the left side of the column.
	 */
	public static int getColX(int col) {
		return Board.DEFAULT_CELL_WIDTH * col + Board.DEFAULT_CELL_WIDTH / 2
				- RADIUS;
	}

	/**
	 * Get the y-coordinate of the top of a row.
	 * 
	 * @param row
	 *            A row number.
	 * @return The y-coordinate of the row.
	 */
	public static int getRowY(int row) {
		return Board.DEFAULT_CELL_HEIGHT * row + Board.DEFAULT_CELL_HEIGHT / 2
				- RADIUS;
	}

	/**
	 * Make this piece a king.
	 */
	public void makeKing() {
		king = true;
	}

	/**
	 * Returns whether or not this piece is a king.
	 * 
	 * @return true if this piece is a king, false otherwise.
	 */
	public boolean isKing() {
		return king;
	}

	/**
	 * Determines if this piece should be a king given the position it is in and
	 * the player who owns it. i.e. if the player starting at the top owns the
	 * piece and the piece is in the last row of the board, it should be a king,
	 * and the same is true for the opposite.
	 * 
	 * @return true if this piece should be a king, false otherwise.
	 */
	public boolean shouldBeKing() {
		return !king
				&& ((player == Player.BELOW && row == 0) || (player == Player.ABOVE && row == 7));
	}

	/**
	 * Return a String stating the player owning the piece and whether or not
	 * the piece is a king. i.e. if the player starting below owns it and it is
	 * not a king, the string will be "B". Similarly, if the player starting
	 * above owns it and it is a king, the string will be "AK".
	 */
	public String toString() {
		String str;
		if (player == Player.ABOVE)
			str = "A";
		else
			str = "B";
		if (isKing())
			str += "K";
		return str;
	}

	@Override
	public Piece clone() {
		Piece p = new Piece(player, row, col, applet);
		p.king = king;
		return p;
	}
}
