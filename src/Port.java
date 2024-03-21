import java.io.IOException;

public class Port {
    private final String PATH = "data/materials/";

    private String spriteFile;
    private Resource resource;
    private int ratio;

    private int x;
    private int y;

    public Port(Resource resource, int x, int y) {
        this.resource = resource;
        this.ratio = 2;
        this.x = x;
        this.y = y;

        switch (this.resource) {
            case SHEEP:
                this.spriteFile = PATH + "sheep.txt";
                break;
            case WOOD:
                this.spriteFile = PATH + "wood.txt";
                break;
            case BRICK:
                this.spriteFile = PATH + "brick.txt";
                break;
            case ORE:
                this.spriteFile = PATH + "ore.txt";
                break;
            case WHEAT:
                this.spriteFile = PATH + "wheat.txt";
                break;
            default:
                this.spriteFile = PATH + "free.txt";
                this.ratio = 3;
                break;
        }
    }

    public Port(Resource resource, Point p) {
        this(resource, p.x, p.y);
    }

    public void render() throws IOException {
        Artist.drawFromFile(spriteFile, x, y);

        Terminal.setXY(x, y+3);
        Terminal.print("\u001b[48;5;153m", ratio + " --> 1");
    }

    public void connectClosestBuildings() {
        Point center = new Point(this.x+3, this.y+1);

        double closest1 = 100000.0, closest2 = 100000.0;
        Building b1 = null, b2 = null;
        for (Building b : Board.buildings.values()) {
            double dist = center.distanceSquared(b.getPoint());
            if (dist < closest1) {
                closest2 = closest1;
                b2 = b1;
                b1 = b;
                closest1 = dist;
            }
            else if (dist < closest2) {
                b2 = b;
                closest2 = dist;
            }
        }

        b1.setTrade(this.resource);
        b2.setTrade(this.resource);
    }
}
