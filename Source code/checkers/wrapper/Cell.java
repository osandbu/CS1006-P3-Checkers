package checkers.wrapper;

/**
 * Wrapper class used to store the row and column of a cell.
 * 
 * @author 090010514
 */
public class Cell {
	private final int row;
	private final int col;

	/**
	 * Create a new Cell.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 */
	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	/**
	 * Returns the cell number of this cell.
	 * 
	 * @return The cell number of this cell.
	 */
	public int getCellNumber() {
		return row * 4 + col / 2 + 1;
	}

	/**
	 * Returns a String representation of the row and column of this Cell in the
	 * format "(row,col)".
	 */
	public String toString() {
		return "(" + row + "," + col + ")";
	}

	/**
	 * Returns the value to a player of the cell with the given coordinates.
	 * 
	 * @return A value in the range 1-4, representing how good this cell
	 *         position is.
	 */
	public int value() {
		return value(getRow(), getCol());
	}

	/**
	 * Returns the value to a player of the cell with the given coordinates.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 * @return A value in the range 1-4, representing how good this cell
	 *         position is.
	 */
	public static int value(int row, int col) {
		if (row == 0 || row == 7 || col == 0 || col == 7)
			return 4;
		else if (row == 1 || row == 6 || col == 1 || col == 6)
			return 3;
		else if (row == 2 || row == 5 || col == 2 || col == 5)
			return 2;
		else
			return 1;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Cell))
			return false;
		Cell other = (Cell) obj;
		return this.row == other.row && this.col == other.col;
	}

	/**
	 * Converts a cell number to a Cell object.
	 * 
	 * @param cellNumber
	 * @return The cell represented by the given cellNumber.
	 */
	public static Cell fromCellNumber(int cellNumber) {
		int row = (cellNumber - 1) / 4;
		int col = 2 * (cellNumber - 4 * row - 1);
		if (row % 2 == 0)
			col++;
		return new Cell(row, col);
	}
}
