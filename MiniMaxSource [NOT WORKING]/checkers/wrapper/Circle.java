package checkers.wrapper;

import java.awt.Graphics;

/**
 * The Circle class is used to determine if a mouse click is on a Checkers
 * piece. It extends Point because a circle has a position.
 * 
 * @author 090010514
 */
public class Circle extends Point {
	private final int radius;

	/**
	 * Create a new Circle object with a given x- and y-coordinate and radius.
	 * The x-coordinate is the x-coordinate of the left-most point on the circle
	 * while the y-coordinate is the y-coordinate of the uppermost point on the
	 * circle.
	 * 
	 * @param x
	 *            An x-coordinate.
	 * @param y
	 *            A y-coordinate.
	 * @param radius
	 *            The radius of the circle.
	 */
	public Circle(int x, int y, int radius) {
		super(x, y);
		this.radius = radius;
	}

	/**
	 * Draw this circle onto a graphics context.
	 * 
	 * @param g
	 *            A graphics context.
	 */
	public void draw(Graphics g) {
		g.fillOval(getX(), getY(), radius * 2, radius * 2);
	}

	/**
	 * Determines whether this circle contains a given point.
	 * 
	 * @param x
	 *            An x-coordinate.
	 * @param y
	 *            A y-coordinate.
	 * @return true if this circle contains the point of the given coordinates,
	 *         false otherwise.
	 */
	public boolean contains(int x, int y) {
		return contains(new Point(x, y));
	}

	/**
	 * Determines whether this circle contains a given point.
	 * 
	 * @param p
	 *            A point.
	 * @return true if this circle contains the given point, false otherwise.
	 */
	public boolean contains(Point p) {
		return p.distanceTo(getCenter()) <= radius;
	}

	/**
	 * Get the coordinates of the centre of this circle.
	 * 
	 * @return The centre of this circle.
	 */
	public Point getCenter() {
		return new Point(getX() + radius, getY() + radius);
	}
}