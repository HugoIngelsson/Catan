import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;

public class Generator {
    public static List<Point> getTileSetPoints() {
        return new ArrayList<Point>(Arrays.asList(
            new Point(47, 7),
            new Point(31, 12),
            new Point(63, 12),
            new Point(15, 17),
            new Point(47, 17),
            new Point(79, 17),
            new Point(31, 22),
            new Point(63, 22),
            new Point(15, 27),
            new Point(47, 27),
            new Point(79, 27),
            new Point(31, 32),
            new Point(63, 32),
            new Point(15, 37),
            new Point(47, 37),
            new Point(79, 37),
            new Point(31, 42),
            new Point(63, 42),
            new Point(47, 47)
        ));
    }

    public static List<Point> getPortSetPoints() {
        return new ArrayList<Point>(Arrays.asList(
            new Point(34, 6),
            new Point(72, 6),
            new Point(8, 15),
            new Point(98, 15),
            new Point(5, 32),
            new Point(101, 32),
            new Point(23, 49),
            new Point(83, 49),
            new Point(53, 57)
        ));
    }

    // returns a set of all possible locations a building could be in
    public static HashSet<Point> getBuildingSetPoints() {
        return new HashSet<Point>(Arrays.asList(
                                                          new Point(50, 6),  new Point(62, 6),
                                   new Point(34, 11), new Point(46, 11), new Point(66, 11), new Point(78, 11),
            new Point(18, 16), new Point(30, 16), new Point(50, 16), new Point(62, 16), new Point(82, 16), new Point(94, 16),
            new Point(14, 21), new Point(34, 21), new Point(46, 21), new Point(66, 21), new Point(78, 21), new Point(98, 21),
            new Point(18, 26), new Point(30, 26), new Point(50, 26), new Point(62, 26), new Point(82, 26), new Point(94, 26), 
            new Point(14, 31), new Point(34, 31), new Point(46, 31), new Point(66, 31), new Point(78, 31), new Point(98, 31), 
            new Point(18, 36), new Point(30, 36), new Point(50, 36), new Point(62, 36), new Point(82, 36), new Point(94, 36),
            new Point(14, 41), new Point(34, 41), new Point(46, 41), new Point(66, 41), new Point(78, 41), new Point(98, 41),
            new Point(18, 46), new Point(30, 46), new Point(50, 46), new Point(62, 46), new Point(82, 46), new Point(94, 46),
                                   new Point(34, 51), new Point(46, 51), new Point(66, 51), new Point(78, 51),
                                                          new Point(50, 56), new Point(62, 56)
        ));
    }

    public static List<Integer> getNumbersList() {
        return new ArrayList<Integer>(Arrays.asList(
            2, 3, 3, 4, 4, 5, 5, 6, 6,
            8, 8, 9, 9,10,10,11,11,12
        ));
    }

    public static List<Resource> getTileResourceList() {
        return new ArrayList<Resource>(Arrays.asList(
            Resource.SHEEP, Resource.SHEEP, Resource.SHEEP, Resource.SHEEP,
            Resource.WOOD, Resource.WOOD, Resource.WOOD, Resource.WOOD,
            Resource.BRICK, Resource.BRICK, Resource.BRICK,
            Resource.ORE, Resource.ORE, Resource.ORE,
            Resource.WHEAT, Resource.WHEAT, Resource.WHEAT, Resource.WHEAT
        ));
    }

    public static List<Resource> getPortResourceList() {
        return new ArrayList<Resource>(Arrays.asList(
            Resource.SHEEP, 
            Resource.WOOD,
            Resource.BRICK,
            Resource.ORE,
            Resource.WHEAT,
            Resource.DESERT, Resource.DESERT, Resource.DESERT, Resource.DESERT
        ));
    }

    public static int colorEnum(String color) {
        switch (color) {
            case("red"):
                return 0;
            case("blue"):
                return 1;
            case("green"):
                return 2;
            case("white"):
                return 3;
            default:
                return -1;
        }
    }

    public static String getBackgroundColor(String color) {
        switch (color) {
            case("red"):
                return "\u001b[48;5;9m";
            case("blue"):
                return "\u001b[48;5;12m";
            case("green"):
                return "\u001b[48;5;10m";
            case("white"):
                return "\u001b[48;5;255m";
            default:
                return null;
        }
    }

    public static String getForegroundColor(String color) {
        switch (color) {
            case("red"):
                return "\u001b[38;5;9m";
            case("blue"):
                return "\u001b[38;5;12m";
            case("green"):
                return "\u001b[38;5;10m";
            case("white"):
                return "\u001b[38;5;250m";
            default:
                return null;
        }
    }

    public static List<Card> getCards() {
        return new ArrayList<Card>(Arrays.asList(
            new Knight(),
            new Knight(),
            new Knight(),
            new Knight(),
            new Knight(),
            new Knight(),
            new Knight(),
            new Knight(),
            new Knight(),
            new Knight(),
            new Knight(),
            new Knight(),
            new Knight(),
            new Knight(),
            new RoadBuilding(),
            new RoadBuilding(),
            new YearOfPlenty(),
            new YearOfPlenty(),
            new Monopoly(),
            new Monopoly(),
            new VP("   GREAT HALL   ", "great_hall.txt"),
            new VP("   UNIVERSITY   ", "university.txt"),
            new VP("     MARKET     ", "market.txt"),
            new VP("     CHAPEL     ", "chapel.txt"),
            new VP("     LIBRARY    ", "library.txt")
        ));
    }

    public static <T> int getRandomID(List<T> list) {
        return (int)(list.size() * Math.random());
    }

    public static <T> int getRandomID(T[] list) {
        return (int)(list.length * Math.random());
    }
}
