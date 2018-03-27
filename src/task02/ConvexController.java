package task02;

public class ConvexController {
    private final int dxMax;
    private final int dxMin;
    private final int dyMax;
    private final int dyMin;
    private final Point endPoint;
    private final ConvexFunction function;

    private Point currentPoint;

    public static void main(String[] args) {
        ConvexFunction function = new ConvexFunction();
        Point initial = new Point(4,5);
        Point end = new Point(9,11);
        ConvexController controller = new ConvexController(3, 2, 5, 1,
                initial, end, function);
        controller.executeRoute();

    }

    private void executeRoute() {
        System.out.println(currentPoint);
        while (!currentPoint.equals(endPoint)) {
            int xDistance = currentPoint.getX() - endPoint.getX();
            int yDistance = currentPoint.getY() - endPoint.getY();
            Direction direction = determineDirection(xDistance, yDistance);
            if (direction == null) {
                break;
            }
            switch (direction) {
                case DOWN:
                case UP:
                    direction.move(currentPoint, calculateMoveDistance(yDistance, direction));
                    break;
                case LEFT:
                case RIGHT:
                    direction.move(currentPoint, calculateMoveDistance(xDistance, direction));
                    break;
            }
            System.out.println(currentPoint);
        }

    }

    private int calculateMoveDistance(int distance, Direction direction) {
        int absDistance = Math.abs(distance);
        int dMin;
        int dMax;
        int step;

        if (direction == Direction.DOWN || direction == Direction.UP) {
            dMin = dyMin;
            dMax = dyMax;
        } else {
            dMin = dxMin;
            dMax = dxMax;
        }
        step = dMax;

        int maxStepResult = absDistance - dMax;

        if (maxStepResult < 0) {
            step = absDistance;
        } else if (maxStepResult > 0) {
            if (maxStepResult < dMin) {
                step = dMax - (dMax - maxStepResult);
            }
        }

        return step;
    }

    private Direction determineDirection(int xDistance, int yDistance) {
        if (yDistance < 0) {
            if (function.getMaxYPoint(currentPoint.getX()).getY() >= endPoint.getY()) {
                return Direction.UP;
            }
        } else if (yDistance > 0) {
            if (function.getMinYPoint(currentPoint.getX()).getY() <= endPoint.getY()) {
                return Direction.DOWN;
            }
        }

        if (xDistance > 0) {
            if (function.getMinXPoint(currentPoint.getY()).getX() <= endPoint.getX()) {
                return Direction.LEFT;
            }
        } else if (xDistance < 0) {
            if (function.getMaxXPoint(currentPoint.getY()).getX() >= endPoint.getX()) {
                return Direction.RIGHT;
            }
        }

        return null;
    }

    enum Direction {
        UP {
            @Override
            int move(Point point, int distance) {
                point.setY(point.getY() + distance);
                return point.getY();
            }
        },
        DOWN {
            @Override
            int move(Point point, int distance) {
                point.setY(point.getY() - distance);
                return point.getY();
            }
        },
        LEFT {
            @Override
            int move(Point point, int distance) {
                point.setX(point.getX() - distance);
                return point.getX();
            }
        },
        RIGHT {
            @Override
            int move(Point point, int distance) {
                point.setX(point.getX() + distance);
                return point.getX();
            }
        };

        abstract int move(Point point, int distance);
    }

    public ConvexController(final int dxMax, final int dxMin, final int dyMax, final int dyMin,
                            final Point initialPoint, final Point endPoint, final ConvexFunction function) {
        this.function = function;
        this.dxMax = dxMax;
        this.dxMin = dxMin;
        this.dyMin = dyMin;
        this.dyMax = dyMax;
        this.endPoint = endPoint;
        currentPoint = new Point(initialPoint.getX(), initialPoint.getY());
    }
}
