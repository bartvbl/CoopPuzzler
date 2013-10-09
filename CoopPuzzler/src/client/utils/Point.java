package client.utils;

public class Point {
	public final double x;
	public final double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double distanceTo(Point point) {
		double dx = point.x - x;
		double dy = point.y - y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public String toString() {
		return "("+x+", "+y+")";
	}

	public Point negate() {
		return new Point(-x, -y);
	}

	public Point delta(Point point) {
		return new Point(point.x - x, point.y - y);
	}

	public Point displaceBy(Point point) {
		return new Point(x + point.x, y + point.y);
	}
	
	public double magnitude() {
		return Math.sqrt(x*x + y*y);
	}
}