package task02;

class ConvexFunction {

    Point getMaxXPoint(int y) {
        return new Point(10,5);
    }

    Point getMaxYPoint(int x) {
        if (x == 4) {
            return new Point(4, 6);
        } else {
            return new Point(4, 11);
        }
    }

    Point getMinXPoint(int y) {
        return new Point(2,5);
    }

    Point getMinYPoint(int x) {
        return new Point(3, 4);
    }
}
