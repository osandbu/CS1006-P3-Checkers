package checkers.wrapper;

/**
 * Wrapper class used to represent a move (i.e. the piece which is to be moved
 * and the destination cell).
 * 
 * @author 090010514
 */
public class Move {
	private final Piece piece;
	private final Cell destination;

	/**
	 * Create a new Move object with a given piece and destination.
	 * 
	 * @param piece
	 *            A piece.
	 * @param destination
	 *            A destination cell.
	 */
	public Move(Piece piece, Cell destination) {
		this.piece = piece;
		this.destination = destination;
	}

	public Piece getPiece() {
		return piece;
	}

	public Cell getDestination() {
		return destination;
	}

	/**
	 * Returns a String representation of this move, by stating the cell number
	 * of the starting position a dash and the cell number of the destination
	 * cell. For example if a piece is moving from cell number 22 to 18 the
	 * string representation would be "22-18".
	 */
	public String toString() {
		Cell from = piece.getCell();
		return from.getCellNumber() + "-" + destination.getCellNumber();
	}

	@Override
	public Move clone() {
		return new Move(piece.clone(), destination);
	}
}
