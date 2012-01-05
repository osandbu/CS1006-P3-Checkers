package checkers;

/**
 * The CheckersFrame class controls the main window and menus.
 * 
 * @author 090010514
 */
import javax.swing.*;

import checkers.wrapper.GameStyle;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public final class CheckersFrame extends JFrame implements WindowListener,
		ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	// Contains the board
	private CheckersApplet applet;
	// To communicate with user(s)
	private JTextField msgField;

	// File-menu items
	private MenuItem quitItem;
	private MenuItem newGameItem;
	private MenuItem threeMoveItem;
	private MenuItem loadItem;
	private MenuItem saveItem;
	private MenuItem replayItem;

	// Settings-menu Items
	private MenuItem gameStyleItem;
	private CheckboxMenuItem allowMultiCapItem;

	// Specifies if a game has been loaded. if that is the case, replay cannot
	// be saved.
	private boolean gameLoaded = false;

	/**
	 * Constructor which creates a new CheckersFrame. Makes the graphical user
	 * interface, adds window listeners, sets the position of the window and
	 * makes it so that the user cannot change its size.
	 */
	public CheckersFrame() {
		super(Constants.GAME_NAME);
		makeGUI();
		addWindowListener(this);
		setLocationRelativeTo(null);
		setResizable(false);
	} // end of CheckersFrame constructor

	/*
	 * ======================================================================
	 * makeGUI Might want to add a text box to say who to move.
	 * ======================================================================
	 */
	private void makeGUI() {
		msgField = new JTextField("File -> New Game");
		msgField.setEditable(false);
		add(msgField, BorderLayout.SOUTH);
		applet = new CheckersApplet(this);
		add(applet, BorderLayout.CENTER);

		makeMenus();
		pack();
	} // end of makeGUI()

	/**
	 * Initialise and set menu-bar, file and settings menus and menu items.
	 */
	private void makeMenus() {
		Menu file = new Menu("File");
		newGameItem = new MenuItem("New game", new MenuShortcut(KeyEvent.VK_N));
		file.add(newGameItem);
		threeMoveItem = new MenuItem("New 3-move game", new MenuShortcut(
				KeyEvent.VK_3));
		file.add(threeMoveItem);
		loadItem = new MenuItem("Load game", new MenuShortcut(KeyEvent.VK_O));
		file.add(loadItem);
		saveItem = new MenuItem("Save game", new MenuShortcut(KeyEvent.VK_S));
		file.add(saveItem);
		replayItem = new MenuItem("Replay game");
		file.add(replayItem);
		file.addSeparator();
		quitItem = new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_Q));
		file.add(quitItem);

		Menu settings = new Menu("Settings");
		gameStyleItem = new MenuItem("Set game style");
		settings.add(gameStyleItem);
		allowMultiCapItem = new CheckboxMenuItem("Allow multi-capture", true);
		allowMultiCapItem.addItemListener(this);
		settings.add(allowMultiCapItem);

		addActionListeners();

		MenuBar menuBar = new MenuBar();
		menuBar.add(file);
		menuBar.add(settings);
		setMenuBar(menuBar);
	}

	/**
	 * Add action listeners to each of the menu items.
	 */
	private void addActionListeners() {
		MenuItem[] items = { quitItem, newGameItem, threeMoveItem, loadItem,
				saveItem, gameStyleItem, replayItem };
		for (MenuItem item : items) {
			item.addActionListener(this);
		}
	}

	/*
	 * ======================================================================
	 * setMsg Called from nextTurn in Checkers
	 * ======================================================================
	 */
	public void setMsg(String msg) {
		msgField.setText(msg);
	}

	/**
	 * Listens for menu-item-clicks.
	 */
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == quitItem) {
			exit();
		} else if (source == newGameItem) {
			applet.newGame();
			gameLoaded = false;
		} else if (source == threeMoveItem) {
			applet.newThreeMoveGame();
		} else if (source == loadItem) {
			loadGame();
		} else if (source == saveItem) {
			saveGame();
		} else if (source == replayItem) {
			replayGame();
		} else if (source == gameStyleItem) {
			selectGameStyle();
		}
	}

	/**
	 * Open a window which lets the user choose a game to load, then, once a
	 * file is chosen, load the game information from that file.
	 */
	private void loadGame() {
		FileChooser fc = new FileChooser(".game", "Saved game (.game)");
		int option = fc.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				applet.loadGame(file);
				gameLoaded = true;
			} catch (IOException e) {
				reportError("A reading error occured: " + e.getMessage(),
						"Reading error");
			}
		}
	}

	/**
	 * Checks if a game has been loaded from a file.
	 * 
	 * @return Whether or not the current game has been loaded from a file.
	 */
	public boolean isGameLoaded() {
		return gameLoaded;
	}

	/**
	 * Open a window which lets the user choose a file to save the current game
	 * position to, then, once a file is chosen, save the game information to
	 * that file.
	 */
	private void saveGame() {
		FileChooser fc = new FileChooser(".game", "Saved game (.game)");
		int option = fc.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				applet.saveGame(file);
			} catch (IOException e) {
				reportError("A writing error occured: " + e.getMessage(),
						"Writing error");
			}
		}
	}

	/**
	 * Open a window which lets the user choose a game to replay, then, once a
	 * file is chosen, replay the game in that file.
	 */
	private void replayGame() {
		FileChooser fc = new FileChooser(".rpl", "Replay (.rpl)");
		int option = fc.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				applet.replay(file);
			} catch (IOException e) {
				reportError("A reading error occured: " + e.getMessage(),
						"Reading error");
			}
		}
	}

	/**
	 * Allows the user to choose the game style.
	 */
	private void selectGameStyle() {
		JList list = new JList(GameStyle.values());
		list.setSelectedValue(applet.getGameStyle(), false);
		// set the JList as the message of the dialog.
		int option = JOptionPane.showConfirmDialog(this, list,
				"Select game-style", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			GameStyle selection = (GameStyle) list.getSelectedValue();
			applet.setGameStyle(selection);
		}
	}

	public CheckboxMenuItem getAllowMultiCapItem() {
		return allowMultiCapItem;
	}

	/**
	 * Item state listener for the allowMultiCapItem (which is a
	 * CheckboxMenuItem).
	 */
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();
		if (source instanceof CheckboxMenuItem) {
			CheckboxMenuItem item = (CheckboxMenuItem) source;
			if (item == allowMultiCapItem) {
				applet.setAllowMultiCapture(allowMultiCapItem.getState());
			}
		}
	}

	// ----------------- window listener methods ----------------------------
	public void windowActivated(WindowEvent e) {
	}

	/**
	 * If the window loses focus, reset the picked up piece.
	 */
	public void windowDeactivated(WindowEvent e) {
		applet.resetPickedUp();
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	/**
	 * If the window is closing, shut down the Java run-time environment.
	 */
	public void windowClosing(WindowEvent e) {
		exit();
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	public void exit() {
		System.exit(0);
	}

	/**
	 * Report an error to the user through a popup dialog.
	 * 
	 * @param message
	 *            An error message.
	 * @param title
	 *            The title of the dialog.
	 */
	public void reportError(String message, String title) {
		JOptionPane.showConfirmDialog(this, message, title,
				JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}

	/*
	 * ======================================================================
	 * main method
	 * ======================================================================
	 */
	public static void main(String args[]) {
		CheckersFrame frame = new CheckersFrame();
		frame.setVisible(true);
	} // end of main method
} // end of CheckersFrame class