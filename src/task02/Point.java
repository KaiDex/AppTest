package task02;

public class Point implements Comparable {
    private int x;
    private int y;

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

    @Override
    public String toString() {
        return "task02.Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Point o1 = (Point) o;
        if (o1.getX() == this.getX() && o1.getY() == this.getY())
            return 0;
        return 1;
    }
}
