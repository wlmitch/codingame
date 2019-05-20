import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

class Point {
    int x;
    int y;

    Point() {
        this(0, 0);
    }

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    boolean inArea(Point point, int range) {
        return abs(x - point.x) <= range && abs(y - point.y) <= range;
    }

    int distance(Point point) {
        return abs(x - point.x) + abs(x - point.y);
    }
}

class Option {
    String action;
    int giantsCount;
    int distance;
    Point target;

    Option(String direction, int giantsCount, Point target, int distance) {
        this.action = direction;
        this.giantsCount = giantsCount;
        this.target = target;
        this.distance = distance;
    }

    Option(Point target) {
        this("STRIKE", 0, target, 0);
    }

    boolean isBetterThan(Option option) {
        return giantsCount > option.giantsCount
                || giantsCount == option.giantsCount
                && distance > option.distance;
    }

    Option best(Option option) {
        return option.isBetterThan(this) ? option : this;
    }

}

class Player {
    static int WIDTH = 40;
    static int HEIGHT = 18;
    static int DANGER_SIZE = 1;
    static int STRIKE_SIZE = 4;
    static List<Point> AVAILABLE_MOVEMENTS = Arrays.asList(
            new Point(0,-1 ),
            new Point(1,-1 ),
            new Point(-1,1 ),
            new Point(0,1 ),
            new Point(-1,0 ),
            new Point(1,0 ),
            new Point(-1,-1),
            new Point(1,1 )
    );

    public static void main(String... args) {
        Player player = new Player();
        player.updateThor();
        while (true) {
            player.updateGiants();
            player.play();
        }
    }

    Scanner in = new Scanner(System.in);
    Point thor = new Point();
    int n = 0;
    List<Point> giants = new ArrayList<>();

    void updateThor() {
        this.thor = new Point(in.nextInt(), in.nextInt());
    }

    void updateGiants() {
        in.nextInt();
        this.giants = range(0, in.nextInt())
                .mapToObj(giantIndex -> new Point(in.nextByte(), in.nextByte()))
                .collect(toList());
    }

    void play() {
        if (canKillEveryone()) {
            strike();
        } else {
            doBestMove();
        }
    }

    boolean canKillEveryone() {
        return this.giants.size() == this.giantsInStrikeArea(this.thor).size();
    }

    List<Point> giantsInStrikeArea(Point point) {
        return giants.stream()
                .filter(giant -> giant.inArea(point, STRIKE_SIZE))
                .collect(toList());
    }

    void strike() {
        System.out.println("STRIKE");
    }

    void doBestMove() {
        if (this.isSafe(this.thor)) {
            this.moveToCenter();
        } else {
            this.flee();
        }
    }

    boolean isSafe(Point point) {
        return giants.stream()
                .noneMatch(giant -> giant.inArea(point, DANGER_SIZE));
    }

    void moveToCenter() {
        System.out.println(direction(thor, center(), true));
    }

    Point center() {
        Point center = new Point();
        for (Point giant : this.giants) {
            center.x += giant.x;
            center.y += giant.y;
        }
        center.x /= this.giants.size();
        center.y /= this.giants.size();
        return center;
    }

    String direction(Point point, Point target, boolean modification) {
        String direction = "";
        if (point.y < target.y) {
            direction += "S";
            if (modification) {
                point.y++;
            }
        } else if (point.y > target.y) {
            direction += "N";
            if (modification) {
                point.y--;
            }
        }
        if (point.x < target.x) {
            direction += "E";
            if (modification) {
                point.x++;
            }
        } else if (point.x > target.x) {
            direction += "W";
            if (modification) {
                point.x--;
            }
        }
        if (!direction.isEmpty()) {
            return direction;
        } else {
            return "WAIT";
        }
    }

    void flee() {
        Point center = this.center();
        Option bestOption = AVAILABLE_MOVEMENTS.stream()
                .map(this::toTarget)
                .filter(this::inTheMap)
                .filter(this::isSafe)
                .map(target -> toOption(target, center))
                .reduce(new Option(this.thor), Option::best);
        this.thor = bestOption.target;
        System.out.println(bestOption.action);
    }

    Point toTarget(Point movement) {
        return new Point(this.thor.x + movement.x, this.thor.y + movement.y);
    }

    boolean inTheMap(Point point) {
        return point.x >= 0 && point.x <= WIDTH && point.y >= 0 && point.y <= HEIGHT;
    }

    Option toOption(Point target, Point center) {
        return new Option(
                direction(thor, target, false),
                giantsInStrikeArea(target).size(),
                target,
                target.distance(center));
    }

}
