package checkers.wrapper;

/**
 * Enumeration class which describes the four different types of games: PVP
 * (Player-vs-Player), PVC (Player-vs-Computer, i.e. player starts), CVP
 * (Computer-vs-Player, i.e. computer starts), CVC (Computer-vs-Computer).
 * 
 * @author 090010514
 */
public enum GameStyle {
	PVP, PVC, CVP, CVC;

	private static final String PVP_STRING = "Player-vs-Player";
	private static final String PVC_STRING = "Player-vs-Computer";
	private static final String CVP_STRING = "Computer-vs-Player";
	private static final String CVC_STRING = "Computer-vs-Computer";

	/**
	 * Convert this GameStyle object to a String representation of it.
	 */
	public String toString() {
		switch (this) {
		case PVP:
			return PVP_STRING;
		case PVC:
			return PVC_STRING;
		case CVP:
			return CVP_STRING;
		case CVC:
			return CVC_STRING;
		default:
			return null;
		}
	}

	/**
	 * Create a GameStyle object from a String.
	 * 
	 * @param str
	 *            A string.
	 * @return The GameStyle object corresponding with the String.
	 */
	public static GameStyle fromString(String str) {
		if (str.equals(PVP_STRING))
			return PVP;
		else if (str.equals(PVC_STRING))
			return PVC;
		else if (str.equals(PVC_STRING))
			return PVC;
		else if (str.equals(CVP_STRING))
			return CVP;
		else if (str.equals(CVC_STRING))
			return CVC;
		else
			return null;
	}
}