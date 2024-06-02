import java.io.IOException;
import java.util.*;

public class Board {
    private static final int[][] BUILDING_DELTAS = {
        {12, 0}, {4, 5}, {-4, 5}, {-12, 0}, {-4, -5}, {4, -5}
    };

    private static final int[][] TILE_DELTAS = {
        {10, 0}, {6, 5}, {-6, 5}, {-10, 0}, {-6, -5}, {6, -5}
    };

    private static final int[][] TILE_TO_TILE = {
        {16, 5}, {16, -5}, {0, -10}, {-16, -5}, {-16, 5}, {0, 10}
    };

    private static final int[][] ROAD_MIDS = {
        {-6, 0}, {-2, -2}, {2, -2}, {6, 0}, {2, 3}, {-2, 3}
    };

    public static HashSet<Tile> tiles;
    public static HashSet<Port> ports;
    public static HashMap<Point, Building> buildings;
    public static Queue<Player> players;
    public static ArrayList<PlayerRender> playerRenders;
    public static Queue<Card> cardDeck;
    public static boolean[][] waves;

    public static ArrayList<Player> transferPlayer;

    public static void initialize() throws IOException {
        waves = new boolean[62][112];
        for (int i=0; i<=111/8; i++) {
            for (int j=0; j<=61/4; j++) {
                int x = i*8+(int)(Math.random()*7)+1;
                int y = j*4+(int)(Math.random()*4)+1;

                if (x <= 111 && y <= 61) {
                    waves[y][x] = true;
                }
            }
        }

        tiles = new HashSet<Tile>();
        List<Point> points = Generator.getTileSetPoints();
        Tile desert = new Tile(Resource.DESERT, 7, 
                            points.remove(Generator.getRandomID(points)));
        desert.flipBlocked();
        tiles.add(desert);

        List<Integer> nums = Generator.getNumbersList();
        List<Resource> rsrcs = Generator.getTileResourceList();
        while (points.size() > 0) {
            int ptID = Generator.getRandomID(points);
            int numID = Generator.getRandomID(nums);
            int rscID = Generator.getRandomID(rsrcs);

            tiles.add(new Tile(rsrcs.remove(rscID),
                                nums.remove(numID),
                                points.remove(ptID)));
        }

        ports = new HashSet<>();
        points = Generator.getPortSetPoints();
        rsrcs = Generator.getPortResourceList();

        while (points.size() > 0) {
            int ptID = Generator.getRandomID(points);
            int rscID = Generator.getRandomID(rsrcs);

            ports.add(new Port(rsrcs.remove(rscID),
                                points.remove(ptID)));
        }

        HashSet<Point> validLocs = Generator.getBuildingSetPoints();
        buildings = new HashMap<>();

        for (Point p : validLocs) {
            buildings.put(p, new Building(p));
        }

        for (Port port : ports) {
            port.connectClosestBuildings();
        }

        playerRenders = new ArrayList<>();
        players = new ArrayDeque<>();
        int y = 2;
        for (int i=0; i<Catan.PLAYERS; i++) {
            int id = Generator.getRandomID(transferPlayer);

            Player p = transferPlayer.remove(id);
            players.add(p);
            playerRenders.add(new PlayerRender(p, 114, y+=8));
        }

        ChatBox.initialize(115, 10+8*Catan.PLAYERS, 44, 51-8*Catan.PLAYERS);

        cardDeck = new ArrayDeque<>();
        List<Card> cards = Generator.getCards();
        while (cards.size() > 0) {
            int id = Generator.getRandomID(cards);

            cardDeck.add(cards.remove(id));
        }

        Board.renderAll();
        Terminal.flush();
    }

    public static void renderPlayerStats() {
        for (PlayerRender pr : playerRenders) {
            pr.render();
        }
    }

    public static Player nextPlayer() {
        Player next = players.remove();
        players.add(next);

        return next;
    }

    public static Player peekPlayer() {
        return players.peek();
    }

    public static HashSet<Building> getAdjacentBuildings(Point p) {
        HashSet<Building> ret = new HashSet<>();

        for (int i=0; i<6; i++) {
            Point adjLoc = new Point(p.x+BUILDING_DELTAS[i][0], p.y+BUILDING_DELTAS[i][1]);
            if (buildings.containsKey(adjLoc)) {
                ret.add(buildings.get(adjLoc));
            }
        }

        return ret;
    }

    public static HashSet<Tile> getAdjacentTiles(Point p) {
        HashSet<Tile> ret = new HashSet<>();
        
        for (Tile t : tiles) {
            for (int i=0; i<6; i++) {
                Point adjLoc = new Point(p.x+TILE_DELTAS[i][0], p.y+TILE_DELTAS[i][1]);
                if (t.center().equals(adjLoc)) {
                    ret.add(t);
                }
            }
        }

        return ret;
    }

    public static HashSet<Building> getBuildingsNextToTile(Tile t) {
        HashSet<Building> ret = new HashSet<>();

        Point center = t.center();
        for (int i=0; i<6; i++) {
            if (buildings.containsKey(center.add(TILE_DELTAS[i])))
                ret.add(buildings.get(center.add(TILE_DELTAS[i])));
        }

        return ret;
    }

    public static HashSet<Player> getPlayersOnTile(Tile t) {
        HashSet<Player> players = new HashSet<>();

        for (Building b : getBuildingsNextToTile(t)) {
            if (b.getPlayer() != null)
                players.add(b.getPlayer());
        }

        return players;
    }

    public static Building getBuildingAt(Point p) {
        return buildings.get(p);
    }

    public static Point getPointInDir(Point p, int dir) {
        return p.add(BUILDING_DELTAS[dir]);
    }

    public static boolean isBuildingInDir(Point p, int dir) {
        return buildings.containsKey(p.add(BUILDING_DELTAS[dir]));
    }

    public static Building getBuildingInDir(Point p, int dir) {
        return getBuildingAt(p.add(BUILDING_DELTAS[dir]));
    }

    public static int firstLegalRoad(Point p) {
        for (int i=0; i<6; i++) {
            if (roadLegalInDir(p, i))
                return i;
        }

        return -1;
    }

    public static boolean roadLegalInDir(Point p, int dir) {
        return isBuildingInDir(p, dir) && buildings.get(p).getRoads()[dir] == null;
    }

    public static void buildRoad(Point p, int dir, Player player) {
        Building origin = buildings.get(p);
        Building dest = buildings.get(p.add(BUILDING_DELTAS[dir]));
        player.buyRoad();

        origin.addRoad(dir, player);
        dest.addRoad((dir+3)%6, player);
    }

    public static void distributeResources(int n) {
        for (int i=0; i<Catan.PLAYERS; i++)
            nextPlayer().resetResourcesThisTurn();

        for (Building b : buildings.values()) {
            b.gatherResource(n);
        }

        for (int i=0; i<Catan.PLAYERS; i++)
            ChatBox.receiveDiceRoll(nextPlayer());

        if (Catan.CLIMATE && Climate.isScarce()) {
            for (Tile t : tiles) {
                if (t.isDepleted() && t.getCollectedThisTurn() > 0) {
                    t.flipDepleted();
                }
                else if (t.getResource() == Resource.SHEEP || 
                            t.getResource() == Resource.WHEAT || 
                            t.getResource() == Resource.WOOD) {
                    if (t.getCollectedThisTurn() >= 4) {
                        t.flipDepleted();
                        ChatBox.overuse(t);
                    }
                }
                else if (t.getResource() == Resource.ORE || 
                            t.getResource() == Resource.BRICK) {
                    int dist = Math.abs(7 - t.getNumber());
                    if (t.getCollectedThisTurn() >= dist+2) {
                        t.diminish();
                        ChatBox.depletion(t);
                    }
                }
            }
        }
    }

    public static void spreadDesert() {
        ArrayList<Tile> deserts = new ArrayList<>();
        ArrayList<Tile> depleted = new ArrayList<>();
        for (Tile t : tiles) {
            if (t.getResource() == Resource.DESERT)
                deserts.add(t);

            if (t.isDepleted())
                depleted.add(t);
        }
        
        for (Tile t2 : depleted) {
            loop:
            for (Tile t1 : deserts) {
                for (int i=0; i<6; i++) {
                    if (t1.corner().equals(t2.corner().add(TILE_TO_TILE[i]))) {
                        ChatBox.desertSpread(t2);
                        t2.desert();
                        break loop;
                    }
                }
            }
        }
    }

    public static boolean canPlaceRoad(Point p, int dir) {
        if (getBuildingInDir(p, dir) == null) return false;

        Player curPlayer = peekPlayer();
        if (!curPlayer.canAffordRoad() || curPlayer.getRoads() >= 15)
            return false;

        if (curPlayer.equals(buildings.get(p).getPlayer()))
            return true;

        if (curPlayer.equals(getBuildingInDir(p, dir).getPlayer()))
            return true;

        if (buildings.get(p).getPlayer() == null) {
            for (int i=0; i<6; i++) {
                if (buildings.get(p).getRoads()[i] == curPlayer) 
                    return true;
            }
        }

        if (getBuildingInDir(p, dir).getPlayer() == null) {
            for (int i=0; i<6; i++) {
                if (getBuildingInDir(p, dir).getRoads()[i] == curPlayer) 
                    return true;
            }
        }

        return false;
    }

    public static boolean canPlaceFreeRoad(Point p, int dir) {
        if (getBuildingInDir(p, dir) == null || buildings.get(p).getRoads()[dir] != null) 
            return false;

        Player curPlayer = peekPlayer();
        if (curPlayer.getRoads() >= 15)
            return false;

        if (curPlayer.equals(buildings.get(p).getPlayer()))
            return true;

        if (curPlayer.equals(getBuildingInDir(p, dir).getPlayer()))
            return true;

        if (buildings.get(p).getPlayer() == null) {
            for (int i=0; i<6; i++) {
                if (buildings.get(p).getRoads()[i] == curPlayer) 
                    return true;
            }
        }

        if (getBuildingInDir(p, dir).getPlayer() == null) {
            for (int i=0; i<6; i++) {
                if (getBuildingInDir(p, dir).getRoads()[i] == curPlayer) 
                    return true;
            }
        }

        return false;
    }

    public static boolean existsPlaceableRoad() {
        
        for (int i=0; i<6; i++) {
            for (Point po : buildings.keySet()) {
                if (canPlaceFreeRoad(po, i)) {
                    return true;
                }
            }
        }

        Player curPlayer = peekPlayer();
        if (curPlayer.getRoads() == 15)
            ChatBox.addMessage(curPlayer.toString() + ChatBox.RESET + " has reached the limit of 15 roads " +
                    "and can thus place no more.");
        else 
            ChatBox.addMessage("Uh oh! Seems like there aren't any more available spots to " + 
                    "build a road for " + curPlayer.toString());
        return false;
    }

    public static boolean canPlaceTown(Point p) {
        Player curPlayer = peekPlayer();
        if (!getBuildingAt(p).isBuildable() || !curPlayer.canAffordTown() || curPlayer.getNumTowns() >= 5)
            return false;

        for (int i=0; i<6; i++) {
            if (curPlayer.equals(buildings.get(p).getRoads()[i]))
                return true;
        }

        return false;
    }
    
    public static boolean canPlaceCity(Point p) {
        return getBuildingAt(p).isUpgradable() && peekPlayer().canAffordCity() && 
                    getBuildingAt(p).getPlayer().equals(peekPlayer()) && peekPlayer().getNumCities() < 4;
    }

    public static int longestRoad(Player p) {
        int max = 0;
        HashSet<Point> visited = new HashSet<>();

        for (Building b : buildings.values()) {
            max = Math.max(max, recursiveDeepen(p, visited, b));
        }

        return max;
    }

    private static int recursiveDeepen(Player p, HashSet<Point> visited, Building cur) {
        if (cur.getPlayer() != null && cur.getPlayer() != p && visited.size() > 0) return 0;

        int max = 0;

        for (int i=0; i<6; i++) {
            int[] delta = {ROAD_MIDS[i][0], ROAD_MIDS[i][1]};
            Building inDir = getBuildingInDir(cur.getPoint(), i);

            if (inDir == null) continue;
            Point pt = inDir.getPoint().add(delta);

            if (cur.getRoads()[i] == p && !visited.contains(pt)) {
                visited.add(pt);
                max = Math.max(max, 1 + recursiveDeepen(p, visited, inDir));
                visited.remove(pt);
            }
        }

        return max;
    }

    public static Point tileInDir(Point p, int dir) {
        Point p2 = p.add(TILE_TO_TILE[dir]);

        for (Tile t : tiles) {
            if (t.corner().equals(p2))
                return p2;
        }

        return null;
    }

    public static void renderAll() throws IOException {
        Artist.drawFromFile("data/background.txt", 1, 1);
        for (int i=0; i<=111; i++) {
            for (int j=0; j<=61; j++) {
                if (waves[j][i]) {
                    Artist.drawWave(i, j);
                }
            }
        }

        for (Tile t : tiles) {
            t.render();
        }

        for (Building b : buildings.values()) {
            b.render();
        }

        for (Port p : ports) {
            p.render();
        }

        renderPlayerStats();

        ChatBox.clear(1);
        ChatBox.displayMessages();

        if (Catan.CLIMATE) {
            Climate.initClimate();
        }

        Terminal.flush();
    }
}
