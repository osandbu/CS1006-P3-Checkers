package checkers;

import java.awt.CheckboxMenuItem;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import checkers.wrapper.Cell;
import checkers.wrapper.GameStyle;
import checkers.wrapper.Move;
import checkers.wrapper.Piece;
import checkers.wrapper.Player;

/**
 * The CheckersApplet class presents the board to the user, lets the user move
 * pieces on the board and controls the AI opponent.
 * 
 * @author 090010514
 */
public class CheckersApplet extends JApplet implements MouseMotionListener,
		MouseListener {
	private static final long serialVersionUID = 1L;
	public static final Dimension DIMENSIONS = new Dimension(
			Board.DEFAULT_WIDTH - 10, Board.DEFAULT_HEIGHT - 10);
	public Board board;
	CheckersFrame theFrame;

	// Variables relating to a piece being picked up from the board
	private Piece pickedUp;
	private int xOffset;
	private int yOffset;
	private int startX;
	private int startY;

	private GameStyle gameStyle;
	private boolean allowMultiCapture;

	/**
	 * Create a new CheckersApplet
	 * 
	 * @param theFrame
	 *            The parent CheckersFrame in which this applet will appear.
	 */
	public CheckersApplet(CheckersFrame theFrame) {
		super();
		this.theFrame = theFrame;
		gameStyle = GameStyle.PVC;
		allowMultiCapture = true;
		init();
	}

	public GameStyle getGameStyle() {
		return gameStyle;
	}

	/**
	 * Sets the game style and then invokes the AI if appropriate.
	 * 
	 * @param gameStyle
	 *            A gamestyle.
	 */
	public void setGameStyle(GameStyle gameStyle) {
		this.gameStyle = gameStyle;
		if (timeForAI())
			doAI();
	}

	/**
	 * Loads a saved game from a given File.
	 * 
	 * @param file
	 *            A file containing information about a saved game.
	 * @throws IOException
	 *             If a reading error occurs.
	 */
	public void loadGame(File file) throws IOException {
		board.loadFile(file);
	}

	/**
	 * Saves the current game to the given File.
	 * 
	 * @param file
	 *            The file to which the current game should be saved.
	 * @throws IOException
	 *             If a writing error occurs.
	 */
	public void saveGame(File file) throws IOException {
		board.saveFile(file);
	}

	/**
	 * Initialise the board, and add mouse listeners.
	 */
	public void init() {
		setPreferredSize(DIMENSIONS);
		board = new Board(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		newGame();
	}

	/**
	 * Set up the pieces in preparation for a new game to begin.
	 */
	public void newGame() {
		board.newGame(this);
		repaint();
		// set the status message
		updatePlayersTurn();
		if (timeForAI())
			doAI();
	}

	/**
	 * Set up the pieces in preparation for a new game to begin and do three
	 * random first moves.
	 */
	public void newThreeMoveGame() {
		board.threeMoveOpening(this);
		repaint();
		updatePlayersTurn();
		if (timeForAI())
			doAI();
	}

	/**
	 * Tells whether or not to allow multiple captures in a single turn.
	 * 
	 * @return true if multiple captures in a single turn is allowed, otherwise
	 *         false.
	 */
	public boolean getAllowMultiCapture() {
		return allowMultiCapture;
	}

	/**
	 * Set whether or not to allow multiple captures in a single turn.
	 * 
	 * @param allowMultiCapture
	 *            true if multiple captures in a single turn is allowed,
	 *            otherwise false.
	 */
	public void setAllowMultiCapture(boolean allowMultiCapture) {
		CheckboxMenuItem cb = theFrame.getAllowMultiCapItem();
		if (cb.getState() != allowMultiCapture) {
			cb.setState(allowMultiCapture);
		}
		board.setAllowMultiCapture(allowMultiCapture);
	}

	/**
	 * Paint the board onto the graphics context of this applet.
	 */
	public void paint(Graphics g) {
		g.drawImage(board.getImage(), 0, 0, this);
	}

	/**
	 * Repaint the board and specifically repaint the given Piece. Invoked when
	 * animating AI moves.
	 * 
	 * @param p
	 *            A piece.
	 */
	public void repaint(Piece p) {
		repaint(p, false);
	}

	/**
	 * Repaint the board and specifically repaint the given Piece. Invoked when
	 * a human player picks up a piece. Outlines the squared which this piece
	 * can move to (if any) in green if the suggestMove parameter is true.
	 * 
	 * @param p
	 *            A piece.
	 * @param suggestMove
	 *            Whether or not to outline the squares this piece can move to
	 *            in green.
	 */
	public void repaint(Piece p, boolean suggestMove) {
		Graphics g = getGraphics();
		if (g == null)
			return;
		g.drawImage(board.getImage(p, suggestMove), 0, 0, this);
	}

	/**
	 * If the user presses the mouse within the applet, check if the mouse is on
	 * a piece, and if it is, pick up that piece.
	 */
	public void mousePressed(MouseEvent evt) {
		// do not process right mouse button
		if (evt.isMetaDown() || board.isGameOver())
			return;
		int x = evt.getX();
		int y = evt.getY();
		pickedUp = board.pickUpPieceFrom(x, y);
		if (pickedUp != null) {
			startX = pickedUp.getX();
			startY = pickedUp.getY();
			xOffset = startX - x;
			yOffset = startY - y;
			repaint(pickedUp, true);
		}
	}

	/**
	 * If the user has picked up a piece from the board, moves the piece to the
	 * current position of the mouse and repaints.
	 */
	public void mouseDragged(MouseEvent evt) {
		if (pickedUp == null)
			return;
		int x = evt.getX();
		int y = evt.getY();
		pickedUp.setX(x + xOffset);
		pickedUp.setY(y + yOffset);
		repaint(pickedUp, true);
	}

	/**
	 * If the player has picked up a piece, moves the piece to the cell at which
	 * the mouse is released if that is a valid move for the selected piece.
	 */
	public void mouseReleased(MouseEvent evt) {
		if (pickedUp == null)
			return;
		int x = evt.getX();
		int y = evt.getY();
		Cell cell = board.getCellAtPoint(x, y);
		if (!board.getHasCapture() && board.isValidMove(pickedUp, cell)) {
			board.move(pickedUp, cell);
			nextTurn();
		} else if (board.isValidCapture(pickedUp, cell)) {
			board.move(pickedUp, cell);
			if (!board.doubleCaptureAvailible())
				nextTurn();
		} else {
			resetPickedUp();
		}
		repaint();
		pickedUp = null;
	}

	/**
	 * Make the picked up piece return to its original position (i.e. where it
	 * was before it was picked up). This is invoked if the player attempts to
	 * move a piece invalidly.
	 */
	public void resetPickedUp() {
		if (pickedUp != null) {
			pickedUp.setX(startX);
			pickedUp.setY(startY);
			repaint();
			pickedUp = null;
		}
	}

	/**
	 * This method is called once the game is over. It updates the status bar
	 * indicating who has won the game and prompts the user about whether or not
	 * to save the game for replay.
	 */
	public void gameOver() {
		theFrame.setMsg(board.getCurrentPlayer().opposite()
				+ " has won the game!");
		// if game is loaded, do not ask to save replay
		if (theFrame.isGameLoaded())
			return;
		int option = JOptionPane.showConfirmDialog(theFrame,
				"Do you want to save this game for replay?", "Save replay?",
				JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			FileChooser fc = new FileChooser(".rpl", "Replay file (.rpl)");
			option = fc.showSaveDialog(theFrame);
			if (option == FileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					board.saveLog(file);
				} catch (IOException e) {
					theFrame
							.reportError(
									"An error occured while attempting to write the replay file.",
									"Writing error");
				}
			}
		}
	}

	/**
	 * Replays a saved game from the given file.
	 * 
	 * @param file
	 *            A file containing replay information.
	 * @throws IOException
	 *             If a file reading error occurs.
	 */
	public void replay(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte[] bytes = new byte[in.available()];
		in.read(bytes);
		String[] moves = new String(bytes).split(" ");
		board.newGame(this);
		for (String move : moves) {
			board.animateMove(move);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Determines if it is time for the AI to be called.
	 * 
	 * @return true if it is time for the AI to be called , false otherwise.
	 */
	public boolean timeForAI() {
		Player p = board.getCurrentPlayer();
		return gameStyle == GameStyle.CVC
				|| (gameStyle == GameStyle.PVC && p == Player.ABOVE)
				|| (gameStyle == GameStyle.CVP && p == Player.BELOW);
	}

	/**
	 * Updates whose turn it is in the status bar.
	 */
	public void updatePlayersTurn() {
		theFrame.setMsg(board.getCurrentPlayer() + " to Move");
	}

	/**
	 * Determines which move is the best for the current player and executes
	 * that move.
	 */
	public void doAI() {
		/*
		 * Need to run in thread for the game to be interruptable when playing
		 * computer vs computer.
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ArrayList<Move> allMoves = board.getAllValidMoves();
				if (allMoves.size() == 0) {
					gameOver();
					return;
				}
				Move selected = chooseMove(allMoves);
				board.animateMove(selected);
				if (board.doubleCaptureAvailible()) {
					doAI();
				} else {
					nextTurn();
				}
			}
		});
	}

	/**
	 * Switches the current player for the opposite player, updates the status
	 * field and calls the AI if appropriate.
	 */
	public void nextTurn() {
		board.nextTurn();
		updateStatusMessage();
		if (!board.isGameOver() && getGameStyle() != GameStyle.PVP) {
			if (timeForAI())
				doAI();
		}
	}

	/**
	 * Update the status message indicating if the game is over, or whose turn
	 * it is and whether there is a capture.
	 */
	public void updateStatusMessage() {
		if (board.getHasCapture()) {
			theFrame.setMsg(board.getCurrentPlayer()
					+ " to Move (capture availible)");
		} else if (board.hasLost()) {
			gameOver();
		} else {
			updatePlayersTurn();
		}
	}

	/**
	 * Choose a move. Precondition: the list of moves is not empty.
	 * 
	 * @param moves
	 *            An array of all possible moves.
	 * @return The chosen move.
	 */
	private Move chooseMove(ArrayList<Move> moves) {
		int[] posValues = valuateMoves(moves);
		int max = getMax(posValues);
		ArrayList<Move> bestMoves = new ArrayList<Move>();
		for (int i = 0; i < posValues.length; i++) {
			if (posValues[i] == max) {
				bestMoves.add(moves.get(i));
			}
		}
		return bestMoves.get(random(bestMoves.size()));
	}

	/**
	 * Assign values to moves in an ArrayList of possible moves.
	 * 
	 * @param moves
	 *            An ArrayList of possible moves for the current player.
	 * @return An array of integers containing a value of each move in the
	 *         ArrayList.
	 */
	private int[] valuateMoves(ArrayList<Move> moves) {
		int[] moveValues = new int[moves.size()];
		for (int i = 0; i < moveValues.length; i++) {
			moveValues[i] = valueMove(moves.get(i));
		}
		return moveValues;
	}

	/**
	 * Evaluate a given move.
	 * 
	 * @param move
	 *            A possible move for the current player.
	 * @return A integer value for the move.
	 */
	private int valueMove(Move move) {
		GamePosition game = board.applyMove(move);
		Player player = board.getCurrentPlayer();
		return game.value2(player) - board.value2(player);
	}

	/**
	 * Returns the highest value in an array of integers.
	 * 
	 * @param posValues
	 *            An array of integers.
	 * @return The highest value in the array.
	 */
	private int getMax(int[] posValues) {
		int max = posValues[0];
		for (int i = 1; i < posValues.length; i++) {
			if (posValues[i] > max) {
				max = posValues[i];
			}
		}
		return max;
	}

	/**
	 * Returns a random integer between 0 and max - 1.
	 * 
	 * @param max
	 *            The maximum integer returned, plus one.
	 * @return A random number between 0 and max - 1.
	 */
	private int random(int max) {
		return random(0, max);
	}

	/**
	 * Returns a random integer between min and max - 1.
	 * 
	 * @param min
	 *            The minimum integer which can be returned.
	 * @param max
	 *            The maximum integer returned, plus one.
	 * @return A random number between 0 and max - 1.
	 */
	private int random(int min, int max) {
		return (int) (Math.random() * (max - min)) + min;
	}

	// unused interface methods
	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}
