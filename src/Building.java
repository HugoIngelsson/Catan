import java.io.IOException;
import java.util.HashSet;

public class Building {
    private final String TOWN = "_town.txt";
    private final String CITY = "_city.txt";
    private final String PATH = "data/buildings/";
    private final String OVERLAY_TOWN = "overlay/town.txt";
    private final String OVERLAY_CITY = "overlay/city.txt";

    private int yield;
    private Player[] roads;
    private HashSet<Tile> adjacentTiles;
    private Resource portTrade;

    private String spriteFile;
    private String overlayFile;
    private String color;
    private Player player;

    private int x;
    private int y;
    private boolean buildable;
    private boolean upgradable;

    public Building(int x, int y) {
        this.color = null;
        this.spriteFile = null;
        this.overlayFile = null;

        this.yield = 0;
        this.roads = new Player[6];
        this.adjacentTiles = new HashSet<>();

        this.x = x;
        this.y = y;
        this.buildable = true;
        this.upgradable = false;
    }

    public Building(Point p) {
        this(p.x, p.y);
    }

    public void build(Player p) throws IOException {
        this.player = p;
        this.color = p.getColor();
        this.spriteFile = PATH + color + TOWN;
        this.overlayFile = PATH + OVERLAY_TOWN;
        this.upgradable = true;
        this.buildable = false;
        this.yield = 1;

        for (Building b : Board.getAdjacentBuildings(new Point(x, y))) {
            b.buildable = false;
            b.render();
        }

        adjacentTiles = Board.getAdjacentTiles(new Point(x, y));
        render();

        this.player.incrementTowns();
        this.player.decreaseResource(Resource.BRICK, 1);
        this.player.decreaseResource(Resource.WOOD, 1);
        this.player.decreaseResource(Resource.WHEAT, 1);
        this.player.decreaseResource(Resource.SHEEP, 1);

        if (portTrade != null && (!Catan.CLIMATE || !Climate.hasFlooded())) 
            this.player.addTrade(portTrade);
    }

    public void upgrade() throws IOException {
        this.yield = 2;
        this.spriteFile = PATH + color + CITY;
        this.overlayFile = PATH + OVERLAY_CITY;
        this.upgradable = false;

        render();

        this.player.incrementCities();
        this.player.decrementTowns();
        this.player.decreaseResource(Resource.WHEAT, 3);
        this.player.decreaseResource(Resource.ORE, 2);

        ChatBox.buildCity(this.player);
    }

    public void addRoad(int dir, Player player) {
        roads[dir] = player;
    }

    public int getYield() {
        return this.yield;
    }

    public void render() throws IOException {
        for (int i=0; i<6; i++) {
            if (roads[i] != null)
                RoadRender.renderRoad(roads[i].getColor(), i, x, y);
        }

        for (Building b : Board.getAdjacentBuildings(new Point(x, y))) {
            b.shallowRender();
        }

        if (spriteFile == null) {
            Terminal.setXY(x, y);
            if (buildable) 
                Terminal.setPixel(Terminal.pixelHistory[x][y], '*');
            else
                Terminal.setPixel(Terminal.pixelHistory[x][y], ' ');
            return;
        }

        Artist.drawFromFile(spriteFile, overlayFile, x-2, y-1);
    }

    public void shallowRender() throws IOException {
        if (spriteFile == null) {
            Terminal.setXY(x, y);
            if (buildable) 
                Terminal.setPixel(Terminal.pixelHistory[x][y], '*');
            else
                Terminal.setPixel(Terminal.pixelHistory[x][y], ' ');
            return;
        }

        Artist.drawFromFile(spriteFile, overlayFile, x-2, y-1);
    }

    public boolean isBuildable() {
        return this.buildable;
    }

    public boolean isUpgradable() {
        return this.upgradable;
    }

    public HashSet<Tile> getAdjacentTiles() {
        return this.adjacentTiles;
    }

    public void gatherResource(int n) {
        if (player == null)
            return;

        for (Tile t : adjacentTiles) {
            if (!t.getIsBlocked() && t.getNumber() == n) {
                if (!Climate.isScarce() || !t.isDepleted())
                    player.increaseResource(t.getResource(), this.yield);

                t.plusResourceThisTurn();
                if (this.yield == 2) t.plusResourceThisTurn();
            }
        }
    }

    public Player[] getRoads() {
        return this.roads;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {
        return "("+x+","+y+"): "+spriteFile+" "+yield;
    }

    public Point getPoint() {
        return new Point(x, y);
    }

    public void setTrade(Resource resource) {
        this.portTrade = resource;
    }
}
