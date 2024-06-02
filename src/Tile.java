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

    private int resourcesThisTurn;
    private boolean depleted;
    private int lastTurnCollected;

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
        if (!blocked && resource != Resource.DESERT) {
            Artist.drawNumber(number, x+7, y+3);
            if (depleted)
                Artist.drawColorBoxDeep(x+4, y+4, x+14, y+4, "dark_red");
        }
        else if (blocked) {
            if (depleted)
                Artist.drawColorBoxDeep(x+4, y+4, x+14, y+4, "dark_red");
            Artist.drawFromFile(ROBBER_SPRITE, x+7, y+3);
        }
    }

    public void rerender() throws IOException {
        Artist.drawFromFile(spriteFile, x, y);
        renderNumber();

        for (Building b : Board.buildings.values()) {
            b.shallowRender();
        }

        Cursor.drawCursor(UI.cursorPos);
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
            if (number != 7) {
                Artist.drawNumber(number, x+7, y+3);
                if (depleted)
                    Artist.drawColorBoxDeep(x+4, y+4, x+14, y+4, "dark_red");
            }
            else {
                Artist.drawFromFile(PATH + "desert_patch.txt", x+7, y+3);
            }
            this.blocked = false;
        }
        else {
            Artist.drawFromFile(ROBBER_SPRITE, x+7, y+3);
            UI.robberTile = this;
            this.blocked = true;
        }
    }

    public void plusResourceThisTurn() {
        if (UI.turn == this.lastTurnCollected) {
            resourcesThisTurn++;
        }
        else {
            resourcesThisTurn = 1;
            lastTurnCollected = UI.turn;
        }
    }

    public boolean isDepleted() {
        return this.depleted;
    }

    public void flipDepleted() {
        this.depleted = !this.depleted;
        try {
            rerender();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void diminish() {
        if (this.number < 7)
            this.number--;
        else
            this.number++;
        
        try {
            rerender();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCollectedThisTurn() {
        if (UI.turn == this.lastTurnCollected)
            return this.resourcesThisTurn;
        else return 0;
    }

    public String toString() {
        switch (resource) {
            case BRICK: return "\u001b[38;5;124mBRICK";
            case ORE:   return "\u001b[38;5;250mORE";
            case WHEAT: return "\u001b[38;5;221mWHEAT";
            case WOOD:  return "\u001b[38;5;130mWOOD";
            case SHEEP: return "\u001b[38;5;82mSHEEP";
            default:    return "\u001b[38;5;202mDESERT";
        }
    }

    public void desert() {
        this.spriteFile = PATH + "desert.txt";
        this.resource = Resource.DESERT;
        this.number = 7;
        this.depleted = false;

        try {
            rerender();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
