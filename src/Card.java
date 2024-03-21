import java.io.IOException;

public abstract class Card {
    public static final String PATH = "data/GUI/dev/";
    public static final String BEIGE = "\u001b[48;5;229m";

    String artPath;
    String title;
    String[] description;
    int turnTaken = -1;

    public Card(String artPath, String title, String[] description) {
        this.artPath = artPath;
        this.title = title;
        this.description = description;
    }

    public abstract void play();
    public abstract char getRepresentation();

    public void setTurn() {
        this.turnTaken = UI.turn;
    }

    public boolean canPlay() {
        if (this.turnTaken == UI.turn || UI.cardHasBeenUsed)
            return false;
        
        return true;
    }

    public void renderFront(int x, int y) throws IOException {
        Artist.drawFromFileShallow(PATH+"background.txt", x, y);
        Artist.drawFromFileShallow(artPath, x+2, y+1);

        Terminal.setXY(x+4, y);
        Terminal.printShallow(BEIGE, title);

        for (int i=0; i<description.length; i++) {
            Terminal.setXY(x+2, y+11+i);
            Terminal.printShallow(BEIGE, description[i]);
        }
    }

    public void renderBack(int x, int y) throws IOException {
        Artist.drawFromFileShallow(PATH+"small_background.txt", x, y);
        Artist.drawFromFileShallow(artPath, x+2, y+1);
    }
}

class Knight extends Card {
    private static final String NAME = "     KNIGHT     ";
    private static final String ART_NAME = "knight.txt";
    private static final String[] DESCRIPTION = {
        "  Move the robber.  ",
        "  Steal 1 resource  ",
        "   from the owner   ",
        "      of a town     ",
        "  or city adjacent  ",
        "  to the  robber's  ",
        "      new hex.      "
    };

    public Knight() {
        super(PATH + ART_NAME, NAME, DESCRIPTION);
    }

    public void play() {
        ChatBox.addMessage(Board.peekPlayer().toString() + ChatBox.RESET + " played a " + 
                    toString() + ChatBox.RESET + "!");
        Board.peekPlayer().incrementKnights();

        UI.mode = 2;
        UI.robberCursor = UI.robberTile.corner();
        Cursor.removeCursor(UI.cursorPos);
        Cursor.drawRobberCursor(UI.robberCursor);
    }

    public char getRepresentation() {
        return 'K';
    }

    public String toString() {
        return "\u001b[38;5;214mKnight";
    }
}

class RoadBuilding extends Card {
    private static final String NAME = " ROAD BUILDING ";
    private static final String ART_NAME = "road_building.txt";
    private static final String[] DESCRIPTION = {
        " Place 2  new roads ",
        "   as if you  had   ",
        "  just built them.  "
    };

    public RoadBuilding() {
        super(PATH + ART_NAME, NAME, DESCRIPTION);
    }

    public void play() {
        ChatBox.addMessage(Board.peekPlayer().toString() + ChatBox.RESET + " played a " + 
                    toString() + ChatBox.RESET + "!");
        UI.mode = 0;

        if (Board.existsPlaceableRoad())
            UI.freeRoads = 2;
    }

    public char getRepresentation() {
        return 'R';
    }

    public String toString() {
        return "\u001b[38;5;214mRoad \u001b[38;5;214mBuilding";
    }
}

class Monopoly extends Card {
    private static final String NAME = "    MONOPOLY    ";
    private static final String ART_NAME = "monopoly.txt";
    private static final String[] DESCRIPTION = {
        " When you play this ",
        "  card, announce 1  ",
        " type of  resource. ",
        " All  other players ",
        "   must give  you   ",
        "    all of their    ",
        " resources  of that ",
        "        type.       "
    };

    public Monopoly() {
        super(PATH + ART_NAME, NAME, DESCRIPTION);
    }

    public void play() {
        ChatBox.addMessage(Board.peekPlayer().toString() + ChatBox.RESET + " played a " + 
                    toString() + ChatBox.RESET + "!");
        UI.mode = 7;
        UI.curGUI = new MonopolyGUI(29, 24, Board.peekPlayer());
        
        try { UI.curGUI.render(); }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public char getRepresentation() {
        return 'M';
    }

    public String toString() {
        return "\u001b[38;5;214mMonopoly";
    }
}

class YearOfPlenty extends Card {
    private static final String NAME = " YEAR OF PLENTY ";
    private static final String ART_NAME = "year_of_plenty.txt";
    private static final String[] DESCRIPTION = {
        "Take any 2 resources",
        "   from the bank.   ",
        "  Add them to your  ",
        " hand. They  can be ",
        "   2 of  the same   ",
        "   resource  or 2   ",
        "different resources."
    };

    public YearOfPlenty() {
        super(PATH + ART_NAME, NAME, DESCRIPTION);
    }

    public void play() {
        ChatBox.addMessage(Board.peekPlayer().toString() + ChatBox.RESET + " played a " + 
                    toString() + ChatBox.RESET + "!");
        UI.mode = 7;
        UI.curGUI = new YOPGUI(29, 21, Board.peekPlayer());
        
        try { UI.curGUI.render(); }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public char getRepresentation() {
        return 'Y';
    }

    public String toString() {
        return "\u001b[38;5;214mYear \u001b[38;5;214mof \u001b[38;5;214mPlenty";
    }
}

class VP extends Card {
    private static final String[] DESCRIPTION = {
        "  1 VICTORY POINT!  ",
        "                    ",
        "Reveal this  card on",
        "   your turn  if,   ",
        " with it, you reach ",
        "   the number  of   ",
        "points  required for",
        "      victory.      "
    };

    public VP(String name, String artName) {
        super(PATH + artName, name, DESCRIPTION);
    }

    public void play() {
        // do nothing
        ChatBox.addMessage(Board.peekPlayer().toString() + ChatBox.RESET + " played a... " + 
                    toString() + ChatBox.RESET + "? This should't be possible...");
    }

    @Override
    public boolean canPlay() {
        return false;
    }

    public char getRepresentation() {
        return 'V';
    }

    public String toString() {
        return "\u001b[38;5;214mVictory \u001b[38;5;214mPoint";
    }
}