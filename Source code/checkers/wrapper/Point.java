package checkers.wrapper;

/**
 * The Point class is used to represents points on a surface.
 * 
 * @author 090010514
 */
public class Point {
	private int x;
	private int y;

	/**
	 * Create a new Point.
	 * 
	 * @param x
	 *            An x-coordinate.
	 * @param y
	 *            A y-coordinate.
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Get the distance between this point and another point. This method is
	 * used to find the distance between the centre of a circle and the location
	 * of a mouse click in order to determine if the mouse click was within the
	 * circle (and thus whether or not it should be picked up by a mouse-drag).
	 * 
	 * @param other
	 *            A point.
	 * @return The distance between this point and the other point.
	 */
	public double distanceTo(Point other) {
		int dx = this.x - other.x;
		int dy = this.y - other.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Returns a String representation of this point in the format "(x,y)".
	 */
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
