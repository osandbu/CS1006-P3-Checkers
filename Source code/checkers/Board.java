package checkers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import checkers.wrapper.Cell;
import checkers.wrapper.GameStyle;
import checkers.wrapper.Move;
import checkers.wrapper.Piece;
import checkers.wrapper.Player;

/**
 * The Board class is mainly used to graphically represent a game position. It
 * also enables logging of each move for replay.
 * 
 * @author 090010514
 */
public class Board extends GamePosition {
	public static final int DEFAULT_CELL_WIDTH = 32;
	public static final int DEFAULT_CELL_HEIGHT = 32;
	public static final int DEFAULT_WIDTH = DEFAULT_CELL_WIDTH * Constants.ROWS;
	public static final int DEFAULT_HEIGHT = DEFAULT_CELL_HEIGHT * Constants.COLS;
	public static final Dimension DEFAULT_DIMENSIONS = new Dimension(
			DEFAULT_WIDTH, DEFAULT_HEIGHT);

	private CheckersApplet applet;
	private BufferedImage board;
	// BufferedImage onto which the green suggestion squares are drawn.
	private BufferedImage suggestionBoard;
	// Buffered imagine onto which the displayed board is drawn.
	private BufferedImage buffer;
	// logs the moves of a player.
	private BoardLogger logger;
	private Piece lastPicked;
	private Cell lastLocation;

	/**
	 * Create a new board to be displayed in a given CheckersApplet.
	 * 
	 * @param applet
	 *            The CheckersApplet in which this board is to be displayed.
	 */
	public Board(CheckersApplet applet) {
		super();
		this.applet = applet;
		logger = new BoardLogger();
		paintBoard();
		suggestionBoard = buffer = new BufferedImage(DEFAULT_WIDTH,
				DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Start a new game with a three move opening.
	 * 
	 * @param applet
	 *            The applet in which the opening is performed.
	 */
	public void threeMoveOpening(CheckersApplet applet) {
		newGame(applet);
		ThreeMoveOpening.doRandomOpening(this);
		setCurrentPlayer(Player.ABOVE);
		setHasCapture(hasCapture());
	}

	/**
	 * Paint the board (i.e. the cells with no pieces) onto the BufferedImage
	 * board.
	 */
	private void paintBoard() {
		board = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = board.getGraphics();
		g.setColor(Constants.BOARD_COLOR_1);
		g.fillRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		g.setColor(Constants.BOARD_COLOR_2);
		for (int row = 0; row < Constants.ROWS; row++) {
			for (int col = 0; col < Constants.COLS; col++) {
				if (isBlackCell(row, col))
					g.fillRect(col * DEFAULT_CELL_WIDTH, row
							* DEFAULT_CELL_HEIGHT, DEFAULT_CELL_WIDTH,
							DEFAULT_CELL_HEIGHT);
			}
		}
	}

	/**
	 * Determines if a cell is black.
	 * @param row A row number.
	 * @param col A column number.
	 * @return Whether or not the given coordinates is a black cell.
	 */
	public static boolean isBlackCell(int row, int col) {
		return (row + col) % 2 == 1;
	}

	/**
	 * Determines if the given row and column number is a valid cell on the board.
	 * @param row
	 * @param col
	 * @return
	 */
	public static boolean contains(int row, int col) {
		return row >= 0 && row < Constants.ROWS && col >= 0 && col < Constants.COLS;
	}

	public Cell getCellAtPoint(int x, int y) {
		return new Cell(y / DEFAULT_CELL_HEIGHT, x / DEFAULT_CELL_WIDTH);
	}

	public Piece pickUpPieceFrom(int x, int y) {
		Cell cell = getCellAtPoint(x, y);
		Piece piece = get(cell);
		if (piece != null && piece.contains(x, y))
			return piece;
		else
			return null;
	}

	public void animateMove(Move move) {
		Piece piece = move.getPiece();
		Cell dest = move.getDestination();
		int destRow = dest.getRow();
		int destCol = dest.getCol();
		move(piece, destRow, destCol, true);
	}

	@Override
	public void move(Piece piece, int destRow, int destCol, boolean animated) {
		logger.log(piece, destRow, destCol);
		super.move(piece, destRow, destCol, animated);
	}

	public BufferedImage getImage() {
		return getImage(null);
	}

	public BufferedImage getImage(Piece p) {
		return getImage(p, false);
	}

	public BufferedImage getImage(Piece p, boolean suggestMove) {
		Graphics g = buffer.getGraphics();
		if (p == null)
			g.drawImage(board, 0, 0, applet);
		else {
			if (suggestMove)
				suggestMoves(p, g);
			else {
				g.drawImage(board, 0, 0, applet);
				p.draw(g);
			}
		}
		drawPieces(g);
		return buffer;
	}

	private void suggestMoves(Piece p, Graphics g) {
		if (p == lastPicked && p.getCell() == lastLocation) {
			g.drawImage(suggestionBoard, 0, 0, applet);
			return;
		}
		Graphics suggestionG = suggestionBoard.getGraphics();
		suggestionG.drawImage(board, 0, 0, applet);
		ArrayList<Move> captures = getValidCaptures(p);
		if (captures != null && captures.size() > 0) {
			for (Move capture : captures) {
				Cell dest = capture.getDestination();
				paintCellGreen(suggestionG, dest);
			}
		} else if (!getHasCapture()) {
			ArrayList<Move> moves = getValidMoves(p.getRow(), p.getCol());
			if (moves != null && moves.size() > 0) {
				for (Move move : moves) {
					Cell dest = move.getDestination();
					paintCellGreen(suggestionG, dest);
				}
			}
		}
		lastPicked = p;
		lastLocation = p.getCell();
		g.drawImage(suggestionBoard, 0, 0, applet);
	}

	private void paintCellGreen(Graphics g, Cell dest) {
		int x = dest.getCol() * DEFAULT_CELL_WIDTH;
		int y = dest.getRow() * DEFAULT_CELL_HEIGHT;
		g.setColor(Color.GREEN);
		g.fillRect(x, y, DEFAULT_CELL_WIDTH, DEFAULT_CELL_HEIGHT);
	}

	public String getSaveFile() {
		StringBuilder sb = new StringBuilder();
		int cellNumber = 1;
		Piece piece;
		for (int row = 0; row < Constants.ROWS; row++) {
			for (int col = 0; col < Constants.COLS; col++) {
				if (Board.isBlackCell(row, col)) {
					sb.append(cellNumber);
					sb.append(' ');
					piece = get(row, col);
					if (piece == null)
						sb.append('N');
					else
						sb.append(piece);
					sb.append(Constants.LINE_SEPARATOR);
					cellNumber++;
				}
			}
		}
		sb.append(getCurrentPlayer());
		sb.append(Constants.LINE_SEPARATOR);
		sb.append(applet.getGameStyle());
		sb.append(Constants.LINE_SEPARATOR);
		sb.append(getAllowMultiCapture());
		return sb.toString();
	}

	public void saveFile(File file) throws IOException {
		FileWriter out = new FileWriter(file);
		out.append(getSaveFile());
		out.close();
	}

	public void loadFile(File file) {
		int row = 0;
		int col = 0;
		try {
			Scanner in = new Scanner(file);
			for (row = 0; row < Constants.ROWS; row++) {
				for (col = 0; col < Constants.COLS; col++) {
					if (isBlackCell(row, col)) {
						// skip integer (cell number)
						in.nextInt();
						String cur = in.next();
						if (cur.equals("N")) {
							set(row, col, null);
							continue;
						}
						Player player;
						if (cur.startsWith("A")) {
							player = Player.ABOVE;
						} else {
							player = Player.BELOW;
						}
						Piece p = new Piece(player, row, col, applet);
						set(row, col, p);
						if (cur.length() == 2)
							p.makeKing();
					}
				}
			}
			String playerStr = in.next();
			Player player = Player.fromString(playerStr);
			setCurrentPlayer(player);
			String gameStyleStr = in.next();
			GameStyle gameStyle = GameStyle.fromString(gameStyleStr);
			applet.setGameStyle(gameStyle);
			applet.setAllowMultiCapture(in.nextBoolean());
			in.close();
		} catch (IOException e) {
			applet.theFrame.reportError("Error at: " + new Cell(row, col),
					"Error");
		}
		setHasCapture(hasCapture());
		applet.updateStatusMessage();
	}

	/**
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void saveLog(File file) throws IOException {
		logger.save(file);
	}
}
