import java.io.IOException;

public class Tile {
    private final String PATH = "data/hexagons/";
    private final String ROBBER_SPRITE = "data/robber.txt";

    private String spriteFile;
    private Resource resource;
    private int number;

    private int x;
    private int y;

    private boolean blocked;

    public Tile(Resource resource, int number, int x, int y) {
        this.resource = resource;
        this.number = number;
        this.x = x;
        this.y = y;
        this.blocked = false;

        switch (this.resource) {
            case SHEEP:
                this.spriteFile = PATH + "sheep_field.txt";
                break;
            case WOOD:
                this.spriteFile = PATH + "forest.txt";
                break;
            case BRICK:
                this.spriteFile = PATH + "clay_quarry.txt";
                break;
            case ORE:
                this.spriteFile = PATH + "ore_quarry.txt";
                break;
            case WHEAT:
                this.spriteFile = PATH + "wheat_field.txt";
                break;
            default:
                this.spriteFile = PATH + "desert.txt";
                break;
        }
    }

    public Tile(Resource resource, int number, Point p) {
        this(resource, number, p.x, p.y);
    }

    public void render() throws IOException {
        Artist.drawFromFile(spriteFile, x, y);

        renderNumber();
    }

    public void renderNumber() throws IOException {
        if (!blocked && resource != Resource.DESERT)
            Artist.drawNumber(number, x+7, y+3);
        else if (blocked)
            Artist.drawFromFile(ROBBER_SPRITE, x+7, y+3);
    }

    public Point center() {
        return new Point(x+9, y+4);
    }

    public Point corner() {
        return new Point(x, y);
    }

    public int getNumber() {
        return this.number;
    }

    public Resource getResource() {
        return this.resource;
    }

    public boolean getIsBlocked() {
        return this.blocked;
    }

    public void flipBlocked() throws IOException {
        if (blocked) {
            if (number != 7)
                Artist.drawNumber(number, x+7, y+3);
            else
                Artist.drawFromFile(PATH + "desert_patch.txt", x+7, y+3);
            this.blocked = false;
        }
        else {
            Artist.drawFromFile(ROBBER_SPRITE, x+7, y+3);
            UI.robberTile = this;
            this.blocked = true;
        }
    }
}
