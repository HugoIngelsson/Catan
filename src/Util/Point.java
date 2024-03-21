import java.util.Objects;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point add(int[] xy) {
        return new Point(x+xy[0], y+xy[1]);
    }

    public double distanceSquared(Point other) {
        return Math.pow(this.x-other.x, 2) + Math.pow((this.y-other.y)*1.7, 2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != Point.class)
            return false;

        Point other = (Point)o;
        return other.x == this.x && other.y == this.y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
