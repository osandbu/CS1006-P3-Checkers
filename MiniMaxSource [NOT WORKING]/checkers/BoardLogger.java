package checkers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import checkers.wrapper.Cell;
import checkers.wrapper.Move;
import checkers.wrapper.Piece;

/**
 * Logs the moves of a game.
 * 
 * @author 090010514
 */
public class BoardLogger {
	private StringBuilder log;

	public BoardLogger() {
		log = new StringBuilder();
	}

	public void log(Piece piece, int destRow, int destCol) {
		log(piece, new Cell(destRow, destCol));
	}

	public void log(Piece piece, Cell dest) {
		log(new Move(piece, dest));
	}

	private void log(Move m) {
		log.append(m);
		log.append(' ');
	}

	public void save(File file) throws IOException {
		FileWriter out;
		try {
			out = new FileWriter(file);
			out.append(log);
			out.close();
		} catch (IOException e) {
			throw e;
		} finally {
			log = new StringBuilder();
		}
	}
}
