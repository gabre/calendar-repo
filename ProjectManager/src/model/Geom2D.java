package model;

import javafx.geometry.Point2D;

public class Geom2D {
	public static Point2D add(Point2D a, Point2D b) {
		return new Point2D(a.getX() + b.getX(), a.getY() + b.getY());
	}
	
	public static Point2D sub(Point2D a, Point2D b) {
		return new Point2D(a.getX() - b.getX(), a.getY() - b.getY());
	}
	
	public static Point2D scalar(double l, Point2D a) {
		return new Point2D(l * a.getX(), l * a.getY());
	}
	
	public static double dot(Point2D a, Point2D b) {
		return a.getX() * b.getX() + a.getY() * b.getY();
	}
	
	public static Point2D rotate(double alpha, Point2D a) {
		double s = Math.sin(alpha);
		double c = Math.cos(alpha);
		return new Point2D(a.getX() * c - a.getY() * s, a.getX() * s + a.getY() * c);
	}

	public static double length(Point2D p) {
		return Math.sqrt(Math.pow(p.getX(), 2) + Math.pow(p.getY(), 2));
	}

	public static Point2D normalize(Point2D p) {
		return scalar(1/length(p), p);
	}

	public static double distance(Point2D a, Point2D b) {
		return  Math.sqrt(Math.pow(a.getX() - b.getX(), 2) +
						  Math.pow(a.getY() - b.getY(), 2));
	}
	
	public static double distance(Point2D p, Point2D s0, Point2D s1) {
		 Point2D v = sub(s1, s0);
		 Point2D w = sub(p,  s0);
		
		 double c1 = dot(w, v);
		 if ( c1 <= 0 ) {
			 return distance(p, s0);
		 }
		
		 double c2 = dot(v, v);
		 if ( c2 <= c1 ) {
			 return distance(p, s1);
		 }
		
		 double b = c1 / c2;
		 Point2D pb = add(s0, scalar(b, v));
		 return distance(p, pb);
	}
}
