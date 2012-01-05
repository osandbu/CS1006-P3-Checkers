package checkers;

import java.awt.Graphics;
import java.util.ArrayList;

import checkers.wrapper.Cell;
import checkers.wrapper.Move;
import checkers.wrapper.Piece;
import checkers.wrapper.Player;

/**
 * GamePosition represents the state a game is in, including all of the pieces,
 * the player whose move it is, whether the current player has a capture
 * available, and the last piece used to capture with.
 * 
 * @author 090010514
 */
public class GamePosition {
	// Array containing all the pieces on the board.
	private Piece[][] pieces;
	// Whether or not a piece was just made king.
	private boolean justMadeKing;
	// The current player
	private Player currentPlayer;
	// Whether or not the game is over.
	private boolean gameOver;
	// Whether or not the current player has a capture available.
	private boolean hasCapture;
	// Piece last used to capture with.
	private Piece lastUsedToCapture;
	// Whether or not to allow multiple captures in one turn.
	private boolean allowMultiCapture;

	/**
	 * Create a new empty game position.
	 */
	public GamePosition() {
		pieces = new Piece[Constants.ROWS][Constants.COLS];
		justMadeKing = false;
		gameOver = true;
		allowMultiCapture = true;
	}

	/**
	 * Create a game position. Used by the clone method.
	 * 
	 * @param pieces
	 *            The pieces.
	 * @param justMadeKing
	 *            Whether or not the piece last moved was made into a king.
	 * @param allowMultiCapture
	 *            Whether or not to allow multiple captures in one move.
	 * @param currentPlayer
	 *            The current player's move.
	 */
	public GamePosition(Piece[][] pieces, boolean justMadeKing,
			boolean allowMultiCapture, Player currentPlayer) {
		this.pieces = pieces;
		this.justMadeKing = justMadeKing;
		this.allowMultiCapture = allowMultiCapture;
		this.currentPlayer = currentPlayer;
	}

	public boolean isGameOver() {
		return gameOver;
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	/**
	 * Get the hasCapture variable.
	 * 
	 * @return Whether or not the current player has a capture available.
	 */
	public boolean getHasCapture() {
		return hasCapture;
	}

	/**
	 * Set the hasCapture variable.
	 * 
	 * @param hasCapture
	 *            Whether or not the current player has a capture available.
	 */
	public void setHasCapture(boolean hasCapture) {
		this.hasCapture = hasCapture;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Set the current player to a given player.
	 * 
	 * @param player
	 *            A player.
	 */
	public void setCurrentPlayer(Player player) {
		this.currentPlayer = player;
	}

	/**
	 * Make the currentPlayer the other player.
	 */
	public void switchPlayer() {
		setCurrentPlayer(currentPlayer.opposite());
	}

	public Piece getLastUsedToCapture() {
		return lastUsedToCapture;
	}

	/**
	 * Start a new game.
	 * 
	 * @param applet
	 *            The CheckersApplet in which the game is played.
	 */
	public void newGame(CheckersApplet applet) {
		setupPieces(applet);
		gameOver = false;
		hasCapture = false;
		setCurrentPlayer(Player.BELOW);
	}

	/**
	 * Set up the pieces in preparation for a new game.
	 * 
	 * @param applet
	 *            The CheckersApplet in which the game is played.
	 */
	public void setupPieces(CheckersApplet applet) {
		for (int row = 0; row < Constants.ROWS; row++) {
			for (int col = 0; col < Constants.COLS; col++) {
				if (Board.isBlackCell(row, col)) {
					if (row < 3)
						pieces[row][col] = new Piece(Player.ABOVE, row, col,
								applet);
					else if (row > Constants.ROWS - 4) {
						pieces[row][col] = new Piece(Player.BELOW, row, col,
								applet);
					} else {
						pieces[row][col] = null;
					}
				}
			}
		}
	}

	/**
	 * @return Whether or not multiple captures are allowed in a single turn.
	 */
	public boolean getAllowMultiCapture() {
		return allowMultiCapture;
	}

	/**
	 * @param allowMultiCapture
	 *            Whether or not multiple captures in a single turn should be
	 *            allowed.
	 */
	public void setAllowMultiCapture(boolean allowMultiCapture) {
		this.allowMultiCapture = allowMultiCapture;
	}

	/**
	 * Get the piece in a given cell.
	 * 
	 * @param cell
	 *            A cell.
	 * @return The piece in the cell.
	 */
	public Piece get(Cell cell) {
		return get(cell.getRow(), cell.getCol());
	}

	/**
	 * Get the piece in the cell of the given row and column.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 * @return The piece in the given row and column.
	 */
	public Piece get(int row, int col) {
		if (row < 0 || row >= Constants.ROWS || col < 0 || col >= Constants.COLS)
			return null;
		return pieces[row][col];
	}

	/**
	 * Set the piece in a given row and column.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 * @param piece
	 *            A piece.
	 */
	public void set(int row, int col, Piece piece) {
		pieces[row][col] = piece;
	}

	/**
	 * Animate a move.
	 * 
	 * @param move
	 *            A String representation of a move.
	 */
	public void animateMove(String move) {
		move(move, true);
	}

	/**
	 * Move the piece in one cell to another cell.
	 * 
	 * @param move
	 *            A String representation of a move.
	 */
	public void move(String move) {
		move(move, false);
	}

	/**
	 * Move the piece in one cell to another cell.
	 * 
	 * @param move
	 *            A String representation of a move.
	 * @param animated
	 *            Whether or not to animate the move of a piece.
	 */
	public void move(String move, boolean animated) {
		String[] cellStr = move.split("-");
		int cellNumber1 = Integer.parseInt(cellStr[0]);
		int cellNumber2 = Integer.parseInt(cellStr[1]);
		Cell cell1 = Cell.fromCellNumber(cellNumber1);
		Cell cell2 = Cell.fromCellNumber(cellNumber2);
		Piece piece = get(cell1);
		int destRow = cell2.getRow();
		int destCol = cell2.getCol();
		move(piece, destRow, destCol, animated);
	}

	/**
	 * Execute a move.
	 * 
	 * @param move
	 *            A move.
	 */
	public void move(Move move) {
		move(move.getPiece(), move.getDestination());
	}

	/**
	 * Move a piece to another cell.
	 * 
	 * @param piece
	 *            A piece.
	 * @param dest
	 *            A destination cell.
	 */
	public void move(Piece piece, Cell dest) {
		move(piece, dest.getRow(), dest.getCol());
	}

	/**
	 * Move a piece to a given row and column.
	 * 
	 * @param piece
	 *            A piece.
	 * @param destRow
	 *            The destination row.
	 * @param destCol
	 *            The destination column.
	 */
	private void move(Piece piece, int destRow, int destCol) {
		move(piece, destRow, destCol, false);
	}

	/**
	 * Move a piece from one cell to another.
	 * 
	 * @param piece
	 *            A piece.
	 * @param destRow
	 *            The destination row.
	 * @param destCol
	 *            The destination column.
	 * @param animated
	 *            Whether or not to animate the move.
	 */
	public void move(Piece piece, int destRow, int destCol, boolean animated) {
		justMadeKing = false;
		int oldRow = piece.getRow();
		int oldCol = piece.getCol();
		pieces[oldRow][oldCol] = null;
		if (Math.abs(oldRow - destRow) == 2)
			capture(piece, destRow, destCol);
		if (animated)
			piece.animateMove(destRow, destCol);
		else
			piece.move(destRow, destCol);
		pieces[destRow][destCol] = piece;
		if (piece.shouldBeKing()) {
			piece.makeKing();
			justMadeKing = true;
		}
	}

	/**
	 * @return Whether or not the last moved piece was made a king.
	 */
	public boolean justMadeKing() {
		return justMadeKing;
	}

	/**
	 * Capture a piece.
	 * 
	 * @param piece
	 *            The piece used to capture.
	 * @param destRow
	 *            The destination row.
	 * @param destCol
	 *            The destination column.
	 */
	public void capture(Piece piece, int destRow, int destCol) {
		lastUsedToCapture = piece;
		int oldRow = piece.getRow();
		int oldCol = piece.getCol();
		int captureRow = (oldRow + destRow) / 2;
		int captureCol = (oldCol + destCol) / 2;
		pieces[captureRow][captureCol] = null;
	}

	/**
	 * Draw all the pieces on the board onto a graphics context.
	 * 
	 * @param g
	 *            A graphics context.
	 */
	public void drawPieces(Graphics g) {
		if (pieces == null)
			return;
		for (int row = 0; row < Constants.ROWS; row++) {
			for (int col = 0; col < Constants.COLS; col++) {
				if (pieces[row][col] != null)
					pieces[row][col].draw(g);
			}
		}
	}

	@Override
	public GamePosition clone() {
		Piece[][] pieces = new Piece[Constants.ROWS][Constants.COLS];
		for (int row = 0; row < Constants.ROWS; row++)
			for (int col = 0; col < Constants.COLS; col++) {
				if (this.pieces[row][col] != null)
					pieces[row][col] = this.pieces[row][col].clone();
			}
		return new GamePosition(pieces, justMadeKing, allowMultiCapture,
				currentPlayer);
	}

	/**
	 * Apply a move to this game position and return the resultant game
	 * position. Leaves this game position unchanged.
	 * 
	 * @param move
	 *            A move.
	 * @return The resultant GamePosition.
	 */
	public GamePosition applyMove(Move move) {
		GamePosition newPosition = clone();
		newPosition.move(move.clone());
		if (!newPosition.doubleCaptureAvailible()) {
			newPosition.nextTurn();
		}
		return newPosition;
	}

	/**
	 * Toggles whose turn it is and checks whether there is a valid
	 * move/capture.
	 */
	public void nextTurn() {
		lastUsedToCapture = null;
		switchPlayer();
		hasCapture = hasCapture();
		if (!hasCapture && hasLost())
			gameOver = true;
	}

	/**
	 * Checks if the current player has lost the game.
	 * 
	 * @return true if the game has been lost, false otherwise.
	 */
	public boolean hasLost() {
		for (int row = 0; row < Constants.ROWS; row++) {
			for (int col = 0; col < Constants.COLS; col++) {
				Piece p = get(row, col);
				if (p == null)
					continue;
				Player player = p.getPlayer();
				if (player == currentPlayer && hasValidMove(row, col)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Determines whether the current player has a valid capture.
	 * 
	 * @return true if the current player has a valid capture, false otherwise.
	 */
	public boolean hasCapture() {
		for (int row = 0; row < Constants.ROWS; row++) {
			for (int col = 0; col < Constants.COLS; col++) {
				if (hasValidCapture(row, col)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Determines if a double capture is available.
	 * 
	 * @return true if a double capture is available, false otherwise.
	 */
	public boolean doubleCaptureAvailible() {
		Piece lastCaptured = getLastUsedToCapture();
		if (!allowMultiCapture || lastCaptured == null || justMadeKing())
			return false;
		int row = lastCaptured.getRow();
		int col = lastCaptured.getCol();
		return hasValidCapture(row, col);
	}

	/**
	 * Returns true if the piece (if any) in this cell has a valid move.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 * @return true if the piece (if any) in this cell has a valid move, false
	 *         otherwise.
	 */
	private boolean hasValidMove(int row, int col) {
		ArrayList<Move> moves = getValidMoves(row, col);
		return moves != null && moves.size() > 0;
	}

	/**
	 * Returns all valid moves for the current player. If there is a capture
	 * available, only captures are returned.
	 * 
	 * @return All valid moves for the current player.
	 */
	public ArrayList<Move> getAllValidMoves() {
		ArrayList<Move> allMoves = getAllValidCaptures();
		if (allMoves.size() > 0)
			return allMoves;
		for (int row = 0; row < Constants.ROWS; row++) {
			for (int col = 0; col < Constants.COLS; col++) {
				ArrayList<Move> moves = getValidMoves(row, col);
				if (moves != null && moves.size() > 0)
					allMoves.addAll(moves);
			}
		}
		return allMoves;
	}

	/**
	 * Get all valid moves for the piece in a given row and column.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 * @return All valid moves for the piece in the given row and column.
	 */
	public ArrayList<Move> getValidMoves(int row, int col) {
		Piece piece = get(row, col);
		if (piece == null)
			return null;
		Player player = piece.getPlayer();
		if (player != getCurrentPlayer())
			return null;
		ArrayList<Move> captures = new ArrayList<Move>();
		if (piece.isKing() || player == Player.BELOW) {
			Cell leftAbove = new Cell(row - 1, col - 1);
			if (isCellValidAndEmpty(leftAbove))
				captures.add(new Move(piece, leftAbove));
			Cell rightAbove = new Cell(row - 1, col + 1);
			if (isCellValidAndEmpty(rightAbove))
				captures.add(new Move(piece, rightAbove));
		}
		if (piece.isKing() || player == Player.ABOVE) {
			Cell leftBelow = new Cell(row + 1, col - 1);
			if (isCellValidAndEmpty(leftBelow))
				captures.add(new Move(piece, leftBelow));
			Cell rightBelow = new Cell(row + 1, col + 1);
			if (isCellValidAndEmpty(rightBelow))
				captures.add(new Move(piece, rightBelow));
		}
		return captures;
	}

	/**
	 * Determines whether the given cell is valid and empty.
	 * 
	 * @param cell
	 *            A cell.
	 * @return true if the given cell is valid and empty, false otherwise.
	 */
	private boolean isCellValidAndEmpty(Cell cell) {
		return isCellValidAndEmpty(cell.getRow(), cell.getCol());
	}

	/**
	 * Determines whether the given cell is valid and empty.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 * @return true if the given cell is valid and empty, false otherwise.
	 */
	public boolean isCellValidAndEmpty(int row, int col) {
		return Board.contains(row, col) && get(row, col) == null;
	}

	/**
	 * Returns true if the piece (if any) in this cell has a valid capture.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 * @return true if the piece (if any) in this cell has a valid capture,
	 *         false otherwise.
	 */
	public boolean hasValidCapture(int row, int col) {
		Piece piece = get(row, col);
		if (piece == null)
			return false;
		Player player = piece.getPlayer();
		if (player != getCurrentPlayer())
			return false;
		if (piece.isKing() || player == Player.BELOW) {
			if (isCellValidAndEnemy(row - 1, col - 1)
					&& isCellValidAndEmpty(row - 2, col - 2))
				return true;
			if (isCellValidAndEnemy(row - 1, col + 1)
					&& isCellValidAndEmpty(row - 2, col + 2))
				return true;
		}
		if (piece.isKing() || player == Player.ABOVE) {
			if (isCellValidAndEnemy(row + 1, col - 1)
					&& isCellValidAndEmpty(row + 2, col - 2))
				return true;
			if (isCellValidAndEnemy(row + 1, col + 1)
					&& isCellValidAndEmpty(row + 2, col + 2))
				return true;
		}
		return false;
	}

	/**
	 * Get all valid captures.
	 * 
	 * @return All valid captures for all of the current player's pieces.
	 */
	public ArrayList<Move> getAllValidCaptures() {
		ArrayList<Move> allCaptures = new ArrayList<Move>();
		for (int row = 0; row < Constants.ROWS; row++) {
			for (int col = 0; col < Constants.COLS; col++) {
				ArrayList<Move> captures = getValidCaptures(row, col);
				if (captures != null && captures.size() > 0)
					allCaptures.addAll(captures);
			}
		}
		return allCaptures;
	}

	/**
	 * Get all valid captures for a given piece.
	 * 
	 * @return All valid captures for the given piece.
	 */
	public ArrayList<Move> getValidCaptures(Piece p) {
		return getValidCaptures(p.getRow(), p.getCol());
	}

	/**
	 * Get all valid captures for the piece in a given row and column.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 * @return All valid captures for the piece in the given position.
	 */
	public ArrayList<Move> getValidCaptures(int row, int col) {
		Piece piece = get(row, col);
		if (piece == null)
			return null;
		Player player = piece.getPlayer();
		if (player != getCurrentPlayer())
			return null;
		ArrayList<Move> captures = new ArrayList<Move>();
		if (piece.isKing() || player == Player.BELOW) {
			Cell leftAbove = new Cell(row - 2, col - 2);
			if (isCellValidAndEnemy(row - 1, col - 1)
					&& isCellValidAndEmpty(row - 2, col - 2))
				captures.add(new Move(piece, leftAbove));
			Cell rightAbove = new Cell(row - 2, col + 2);
			if (isCellValidAndEnemy(row - 1, col + 1)
					&& isCellValidAndEmpty(row - 2, col + 2))
				captures.add(new Move(piece, rightAbove));
		}
		if (piece.isKing() || player == Player.ABOVE) {
			Cell leftBelow = new Cell(row + 2, col - 2);
			if (isCellValidAndEnemy(row + 1, col - 1)
					&& isCellValidAndEmpty(row + 2, col - 2))
				captures.add(new Move(piece, leftBelow));
			Cell rightBelow = new Cell(row + 2, col + 2);
			if (isCellValidAndEnemy(row + 1, col + 1)
					&& isCellValidAndEmpty(row + 2, col + 2))
				captures.add(new Move(piece, rightBelow));
		}
		return captures;
	}

	/**
	 * Determines if a given cell is valid and whether there is an enemy piece
	 * in it.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 * @return true if a given cell is valid and whether there is an enemy piece
	 *         in it, otherwise false.
	 */
	private boolean isCellValidAndEnemy(int row, int col) {
		return isCellValidAndEnemy(row, col, getCurrentPlayer());
	}

	/**
	 * Determines if a given cell is valid and whether there is an enemy piece
	 * in it.
	 * 
	 * @param row
	 *            A row number.
	 * @param col
	 *            A column number.
	 * @param player
	 *            A player.
	 * @return true if a given cell is valid and whether there is an enemy piece
	 *         (of the given player) in it, otherwise false.
	 */
	public boolean isCellValidAndEnemy(int row, int col, Player player) {
		if (!Board.contains(row, col)) {
			return false;
		}
		Piece piece = get(row, col);
		return piece != null && piece.getPlayer() != player;
	}

	/**
	 * Checks whether user selection is a valid move Assumes destRow, destCol
	 * are not -1 (i.e. there has been a selection).
	 * 
	 * @param piece
	 *            A piece.
	 * @param cell
	 *            A cell.
	 * @return true if the given piece can move to the given cell, false
	 *         otherwise.
	 */
	public boolean isValidMove(Piece piece, Cell cell) {
		int destRow = cell.getRow();
		int destCol = cell.getCol();
		// It is not possible to move to a cell which already contains a piece.
		if (get(destRow, destCol) != null)
			return false;
		if (piece.getPlayer() != getCurrentPlayer())
			return false;
		int col = piece.getCol();
		int row = piece.getRow();
		if (piece.isKing() || getCurrentPlayer() == Player.BELOW) {
			// cell left above
			if (destRow == row - 1 && destCol == col - 1)
				return true;
			// cell right above
			if (destRow == row - 1 && destCol == col + 1)
				return true;
		}
		if (piece.isKing() || getCurrentPlayer() == Player.ABOVE) {
			// cell left below
			if (destRow == row + 1 && destCol == col - 1)
				return true;
			// cell right below
			if (destRow == row + 1 && destCol == col + 1)
				return true;
		}
		return false;
	}

	/**
	 * Checks whether user selection is a valid capture Assumes selRow, selCol
	 * are not -1 (i.e. there has been a selection).
	 * 
	 * @param piece
	 *            A piece.
	 * @param cell
	 *            A cell.
	 * @return true if the given piece can capture another cell by moving to the
	 *         given cell, false otherwise.
	 */
	public boolean isValidCapture(Piece piece, Cell cell) {
		if (doubleCaptureAvailible() && piece != getLastUsedToCapture()) {
			return false;
		}
		Player player = piece.getPlayer();
		if (getCurrentPlayer() != player)
			return false;
		int row = piece.getRow();
		int col = piece.getCol();
		int destRow = cell.getRow();
		int destCol = cell.getCol();
		if (piece.isKing() || player == Player.BELOW) {
			// cell left above
			if (destRow == row - 2 && destCol == col - 2
					&& isCellValidAndEnemy(row - 1, col - 1)
					&& isCellValidAndEmpty(row - 2, col - 2))
				return true;
			// cell right above
			if (destRow == row - 2 && destCol == col + 2
					&& isCellValidAndEnemy(row - 1, col + 1)
					&& isCellValidAndEmpty(row - 2, col + 2))
				return true;
		}
		if (piece.isKing() || player == Player.ABOVE) {
			// cell left below
			if (destRow == row + 2 && destCol == col - 2
					&& isCellValidAndEnemy(row + 1, col - 1)
					&& isCellValidAndEmpty(row + 2, col - 2))
				return true;
			// cell right below
			if (destRow == row + 2 && destCol == col + 2
					&& isCellValidAndEnemy(row + 1, col + 1)
					&& isCellValidAndEmpty(row + 2, col + 2))
				return true;
		}
		return false; // Dummy
	}

	/**
	 * Static evaluation of this GamePosition from the point of view of the
	 * current player.
	 * 
	 * @return
	 */
	public int value() {
		return value2(currentPlayer);
	}

	/**
	 * TODO Static evaluation of this GamePosition from the point of view of the
	 * given player.
	 * 
	 * @param player
	 *            A player.
	 * @return A static evaluation of this GamePosition from the point of view
	 *         of the given player, represented by an integer.
	 */
	public int value(Player player) {
		int value = 0;
		for (int row = 0; row < Constants.ROWS; row++)
			for (int col = 0; col < Constants.COLS; col++) {
				Piece p = get(row, col);
				if (p == null)
					continue;
				Player piecePlayer = p.getPlayer();
				if (piecePlayer == player) {
					value += p.getCell().value();
					if (p.isKing())
						value += 5;
					else
						value += 3;
					if (player == currentPlayer) {
						if (hasValidCapture(row, col)) {
							value += 20;
						}
					} else if (hasValidCapture(row, col)) {

						value -= 20;
					}
				} else {

				}
			}
		return value;
	}

	/**
	 * Compare the value of this game position for the given player with that of
	 * the other player.
	 * 
	 * @param player
	 *            A player.
	 * @return A static evaluation of this GamePosition from the point of view
	 *         of the given player, represented by an integer.
	 */
	public int value2(Player player) {
		return value(player) - value(player.opposite());
	}
}
