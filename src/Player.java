import java.util.ArrayList;

public class Player {
    static Player longestRoadPlayer = null;
    static int longestRoadLength = 4;
    static Player largestArmyPlayer = null;
    static int largestArmySize = 2;

    private String color;
    private String name;

    private int brick, ore, wheat, wood, sheep;
    private int armyCount, roadLength;
    private int roads;
    private int[] trades;

    private int numTowns;
    private int numCities;
    private boolean hasLongestRoad;
    private boolean hasLargestArmy;
    private int numVPCards;

    private ArrayList<Card> cards;

    private int[] resourcesThisTurn;

    public Player(String color, String name) {
        this.color = color;
        this.name = name;

        this.brick = 0;
        this.ore = 0;
        this.wheat = 0;
        this.wood = 0;
        this.sheep = 0;

        this.numTowns = 0;
        this.numCities = 0;
        this.hasLongestRoad = false;
        this.hasLargestArmy = false;
        this.numVPCards = 0;
        this.armyCount = 0;

        this.trades = new int[5];
        for (int i=0; i<5; i++) trades[i] = 4;

        this.cards = new ArrayList<>();
        this.resourcesThisTurn = new int[5];
    }

    public String getColor() {
        return this.color;
    }

    public int getBrick() {
        return this.brick;
    }

    public int getOre() {
        return this.ore;
    }

    public int getWheat() {
        return this.wheat;
    }

    public int getWood() {
        return this.wood;
    }

    public int getSheep() {
        return this.sheep;
    }

    public int getResource(int id) {
        switch (id) {
            case 0: return this.brick;
            case 1: return this.ore;
            case 2: return this.wheat;
            case 3: return this.wood;
            case 4: return this.sheep;
            default: return -1;
        }
    }

    public int getTotalResources() {
        return this.brick + this.ore + this.wheat + this.wood + this.sheep;
    }

    public void flipLongestRoad() {
        this.hasLongestRoad = !this.hasLongestRoad;
    }

    public void flipLargestArmy() {
        this.hasLargestArmy = !this.hasLargestArmy;
    }

    public int getRoadLength() {
        return this.roadLength;
    }

    public int getArmySize() {
        return this.armyCount;
    }

    public int getVP() {
        return this.numTowns + this.numCities*2 + this.numVPCards +
            (this.hasLongestRoad ? 2 : 0) + (this.hasLargestArmy ? 2 : 0);
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public void increaseResource(Resource resource, int amount) {
        switch (resource) {
            case BRICK:
                this.brick += amount;
                this.brick = Math.max(0, brick);
                if (amount > 0) resourcesThisTurn[0] += amount;
                break;
            case ORE:
                this.ore += amount;
                this.ore = Math.max(0, ore);
                if (amount > 0) resourcesThisTurn[1] += amount;
                break;
            case WHEAT:
                this.wheat += amount;
                this.wheat = Math.max(0, wheat);
                if (amount > 0) resourcesThisTurn[2] += amount;
                break;
            case WOOD:
                this.wood += amount;
                this.wood = Math.max(0, wood);
                if (amount > 0) resourcesThisTurn[3] += amount;
                break;
            case SHEEP:
                this.sheep += amount;
                this.sheep = Math.max(0, sheep);
                if (amount > 0) resourcesThisTurn[4] += amount;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void decreaseResource(Resource resource, int count) {
        increaseResource(resource, -count);
    }

    public void buyRoad() {
        roads++;

        decreaseResource(Resource.BRICK, 1);
        decreaseResource(Resource.WOOD, 1);
    }

    public boolean hasLongestRoad() {
        return this.hasLongestRoad;
    }

    public boolean hasLargestArmy() {
        return this.hasLargestArmy;
    }

    public void incrementTowns() {
        this.numTowns++;
    }

    public void decrementTowns() {
        this.numTowns--;
    }

    public void incrementCities() {
        this.numCities++;
    }

    public boolean canAffordTown() {
        return this.brick >= 1 &&
                this.wood >= 1 &&
                this.sheep >= 1 &&
                this.wheat >= 1;
    }

    public boolean canAffordCity() {
        return this.ore >= 2 &&
                this.wheat >= 3;
    }

    public boolean canAffordRoad() {
        return this.brick >= 1 &&
                this.wood >= 1;
    }

    public boolean canAffordCard() {
        return this.ore >= 1 &&
                this.sheep >= 1 &&
                this.wheat >= 1;
    }

    public void setLongestRoad(int length) {
        this.roadLength = length;

        if (this.roadLength > longestRoadLength) {
            if (!hasLongestRoad) ChatBox.buildLongestRoad(this);
            else ChatBox.buildRoad(this);

            if (longestRoadPlayer != null) longestRoadPlayer.hasLongestRoad = false;

            this.hasLongestRoad = true;
            longestRoadLength = this.roadLength;
        }
        else if (roads > 2) ChatBox.buildRoad(this);
    }

    public void addTrade(Resource resource) {
        switch (resource) {
            case DESERT:
                trades[0] = Math.min(trades[0], 3);
                trades[1] = Math.min(trades[1], 3);
                trades[2] = Math.min(trades[2], 3);
                trades[3] = Math.min(trades[3], 3);
                trades[4] = Math.min(trades[4], 3);
                break;
            case BRICK:
                trades[0] = 2;
                break;
            case ORE:
                trades[1] = 2;
                break;
            case WHEAT:
                trades[2] = 2;
                break;
            case WOOD:
                trades[3] = 2;
                break;
            case SHEEP:
                trades[4] = 2;
                break;
        }
    }

    public int[] getTrades() {
        return this.trades;
    }

    public boolean canAffordTrade(int[] demand) {
        return this.brick  >= demand[0] &&
                this.ore   >= demand[1] &&
                this.wheat >= demand[2] &&
                this.wood  >= demand[3] &&
                this.sheep >= demand[4];
    }

    public int getNumTowns() {
        return this.numTowns;
    }

    public int getNumCities() {
        return this.numCities;
    }

    public int getRoads() {
        return this.roads;
    }

    @Override
    public String toString() {
        String color = Generator.getForegroundColor(this.color);
        String ret = color;
        for (int i=0; i<name.length(); i++) {
            ret += name.charAt(i);
            if (name.charAt(i) == ' ') ret += color;
        }

        return ret;
    }

    public Resource getRandomResource() {
        int rand = (int)(Math.random()*getTotalResources());

        if (rand > brick) rand -= brick;
        else if (brick > 0) return Resource.BRICK;

        if (rand > ore) rand -= ore;
        else if (ore > 0) return Resource.ORE;

        if (rand > wheat) rand -= wheat;
        else if (wheat > 0) return Resource.WHEAT;

        if (rand > wood) rand -= wood;
        else if (wood > 0) return Resource.WOOD;

        return Resource.SHEEP;
    }

    public void buyCard() {
        cards.add(Board.cardDeck.poll());
        cards.get(cards.size()-1).setTurn();
        if (cards.get(cards.size()-1).getRepresentation() == 'V')
            this.numVPCards++;

        decreaseResource(Resource.WHEAT, 1);
        decreaseResource(Resource.ORE, 1);
        decreaseResource(Resource.SHEEP, 1);
    }

    public void incrementKnights() {
        this.armyCount++;

        if (this.armyCount > largestArmySize) {
            if (!hasLargestArmy)
                ChatBox.hasLargestArmy(this);

            if (largestArmyPlayer != null)
                largestArmyPlayer.hasLargestArmy = false;

            this.hasLargestArmy = true;
            largestArmyPlayer = this;
            largestArmySize = this.armyCount;
        }
    }

    public void resetResourcesThisTurn() {
        resourcesThisTurn = new int[5];
    }

    public int[] getResourcesThisTurn() {
        return this.resourcesThisTurn;
    }
}
