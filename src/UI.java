import java.io.IOException;
import java.util.*;

public class UI {
    public static Point cursorPos;
    public static Tile robberTile;
    public static Point robberCursor;

    /*
     0 = building place mode
     1 = road place mode
     2 = robber mode
     3 = lose resources from robber
     4 = trade
     5 = steal
     6 = dev card GUI / dev card itself
     7 = GUI's triggered by dev card
    */
    public static int mode = 0;

    public static boolean diceHaveBeenThrown = false;
    public static int roadDir = 0;
    public static boolean cardHasBeenUsed = false;
    public static int turn = 0;
    public static int freeRoads = 0;

    public static GUI curGUI;
    public static Queue<GUI> guiQueue = new ArrayDeque<>();

    public static void resetCursorPos() {
        cursorPos = new Point(46, 31);
    }

    public static void drawCursor() {
        Cursor.drawCursor(cursorPos);
    }

    public static void removeCursor() {
        Cursor.removeCursor(cursorPos);
    }

    public static void moveCursorLeft() {
        if (mode == 0) {
            if (Board.isBuildingInDir(cursorPos, 3)) {
                removeCursor();
                cursorPos = Board.getPointInDir(cursorPos, 3);
                drawCursor();
            }
        }
        else if (mode == 1) {
            Cursor.removeRoad(cursorPos.x, cursorPos.y, roadDir);

            if (Board.roadLegalInDir(cursorPos, 3)) {
                roadDir = 3;
            }

            Cursor.drawRoad(cursorPos.x, cursorPos.y, roadDir);
            Cursor.drawCursor(cursorPos);
        }
        else if (mode == 2) {
            Point p = Board.tileInDir(robberCursor, 3);
            Point p2 = Board.tileInDir(robberCursor, 4);
            if (p != null) {
                Cursor.removeRobberCursor(robberCursor);
                robberCursor = p;
                Cursor.drawRobberCursor(robberCursor);
            }
            else if (p2 != null) {
                Cursor.removeRobberCursor(robberCursor);
                robberCursor = p2;
                Cursor.drawRobberCursor(robberCursor);
            }
        }
        else {
            try {
                curGUI.moveLeft();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void moveCursorRight() {
        if (mode == 0) {
            if (Board.isBuildingInDir(cursorPos, 0)) {
                removeCursor();
                cursorPos = Board.getPointInDir(cursorPos, 0);
                drawCursor();
            }
        }
        else if (mode == 1) {
            Cursor.removeRoad(cursorPos.x, cursorPos.y, roadDir);

            if (Board.roadLegalInDir(cursorPos, 0)) {
                roadDir = 0;
            }

            Cursor.drawRoad(cursorPos.x, cursorPos.y, roadDir);
            Cursor.drawCursor(cursorPos);
        }
        else if (mode == 2) {
            Point p = Board.tileInDir(robberCursor, 0);
            Point p2 = Board.tileInDir(robberCursor, 1);
            if (p != null) {
                Cursor.removeRobberCursor(robberCursor);
                robberCursor = p;
                Cursor.drawRobberCursor(robberCursor);
            }
            else if (p2 != null) {
                Cursor.removeRobberCursor(robberCursor);
                robberCursor = p2;
                Cursor.drawRobberCursor(robberCursor);
            }
        }
        else {
            try {
                curGUI.moveRight();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void moveCursorUp() {
        if (mode == 0) {
            if (Board.isBuildingInDir(cursorPos, 4)) {
                removeCursor();
                cursorPos = Board.getPointInDir(cursorPos, 4);
                drawCursor();
            }
            else if (Board.isBuildingInDir(cursorPos, 5)) {
                removeCursor();
                cursorPos = Board.getPointInDir(cursorPos, 5);
                drawCursor();
            }
        }
        else if (mode == 1) {
            Cursor.removeRoad(cursorPos.x, cursorPos.y, roadDir);

            if (Board.roadLegalInDir(cursorPos, 4)) {
                roadDir = 4;
            }
            else if (Board.roadLegalInDir(cursorPos, 5)) {
                roadDir = 5;
            }

            Cursor.drawRoad(cursorPos.x, cursorPos.y, roadDir);
            Cursor.drawCursor(cursorPos);
        }
        else if (mode == 2) {
            Point p = Board.tileInDir(robberCursor, 2);
            if (p != null) {
                Cursor.removeRobberCursor(robberCursor);
                robberCursor = p;
                Cursor.drawRobberCursor(robberCursor);
            }
        }
        else {
            try {
                curGUI.moveUp();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void moveCursorDown() {
        if (mode == 0) {
            if (Board.isBuildingInDir(cursorPos, 1)) {
                removeCursor();
                cursorPos = Board.getPointInDir(cursorPos, 1);
                drawCursor();
            }
            else if (Board.isBuildingInDir(cursorPos, 2)) {
                removeCursor();
                cursorPos = Board.getPointInDir(cursorPos, 2);
                drawCursor();
            }
        }
        else if (mode == 1) {
            Cursor.removeRoad(cursorPos.x, cursorPos.y, roadDir);

            if (Board.roadLegalInDir(cursorPos, 1)) {
                roadDir = 1;
            }
            else if (Board.roadLegalInDir(cursorPos, 2)) {
                roadDir = 2;
            }

            Cursor.drawRoad(cursorPos.x, cursorPos.y, roadDir);
            Cursor.drawCursor(cursorPos);
        }
        else if (mode == 2) {
            Point p = Board.tileInDir(robberCursor, 5);
            if (p != null) {
                Cursor.removeRobberCursor(robberCursor);
                robberCursor = p;
                Cursor.drawRobberCursor(robberCursor);
            }
        }
        else {
            try {
                curGUI.moveDown();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean clearBuffer(int[] buffer) {
        // for (int i=0; i<100; i++)
        //     if (buffer[i] != 0) System.err.println(buffer[i]);
        //     else break;

        int bufferID = 0;
        while (buffer[bufferID] != 0) {
            if (buffer[bufferID] == 3) // control-c
                return true;
            else if (buffer[bufferID] == 10 || buffer[bufferID] == 13) {
                if (diceHaveBeenThrown && mode < 2 && freeRoads == 0) {
                    diceHaveBeenThrown = false;

                    if (Board.peekPlayer().getVP() >= Catan.VP_REQUIREMENT) {
                        ChatBox.victory(Board.peekPlayer());
                        return true;
                    }

                    Board.nextPlayer();
                    Board.renderPlayerStats();
                    DiceRender.clearDice();

                    try {
                        Artist.drawPlayerEmblem(Board.peekPlayer().getColor());
                    }
                    catch (Exception e) {
                        System.err.println("Something went wrong...");
                    }
                }
            }
            else if (buffer[bufferID] == 27) {
                buffer[bufferID++] = 0;
                if (buffer[bufferID] == 91) {
                    buffer[bufferID++] = 0;

                    if (buffer[bufferID] == 49) { // shift or option held
                        buffer[bufferID++] = 0;
                        buffer[bufferID++] = 0;
                        buffer[bufferID++] = 0;
                    }

                    switch (buffer[bufferID]) {
                        case (65): moveCursorUp(); break; // UP
                        case (66): moveCursorDown(); break; // DOWN
                        case (67): moveCursorRight(); break; // RIGHT
                        case (68): moveCursorLeft(); break; // LEFT
                    }
                }
                else if (buffer[bufferID] == 0) { // pressed escape
                    buffer[--bufferID] = 27;

                    try {
                        if (curGUI != null) {
                            if (curGUI.buttonPress(27)) {
                                curGUI = null;
                                mode = 0;
                            }
                        }
                        else if (mode == 0) {
                            curGUI = new MenuGUI(22, 10);
                            curGUI.render();
                            mode = 6;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (buffer[bufferID] == 127) { // delete
                try {
                    Board.peekPlayer().increaseResource(Resource.BRICK, 1);
                    Board.peekPlayer().increaseResource(Resource.ORE  , 1);
                    Board.peekPlayer().increaseResource(Resource.WHEAT, 1);
                    Board.peekPlayer().increaseResource(Resource.WOOD , 1);
                    Board.peekPlayer().increaseResource(Resource.SHEEP, 1);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (buffer[bufferID] == ' ' && diceHaveBeenThrown) {
                try {
                    if (mode == 0) {
                        if (Board.canPlaceTown(cursorPos) && freeRoads == 0) {
                            Board.getBuildingAt(cursorPos).build(Board.peekPlayer());

                            ChatBox.buildTown(Board.peekPlayer());
                            Board.renderPlayerStats();
                            if (Catan.CLIMATE) {
                                if (Climate.increment())
                                    return true;
                            }
                        }
                        else if (Board.canPlaceCity(cursorPos) && freeRoads == 0) {
                            Board.getBuildingAt(cursorPos).upgrade();
                            Board.renderPlayerStats();
                            if (Catan.CLIMATE) {
                                if (Climate.increment())
                                    return true;
                            }
                        }
                    }
                    else if (mode == 1) {
                        if (Board.canPlaceRoad(cursorPos, roadDir) || (freeRoads > 0 && Board.canPlaceFreeRoad(cursorPos, roadDir))) {
                            if (freeRoads > 0) {
                                freeRoads--;
                                Board.peekPlayer().increaseResource(Resource.BRICK, 1);
                                Board.peekPlayer().increaseResource(Resource.WOOD, 1);
                            }

                            Board.buildRoad(cursorPos, roadDir, Board.peekPlayer());
                            if (freeRoads > 0 && !Board.existsPlaceableRoad())
                                    freeRoads = 0;
                            
                            Cursor.removeRoad(cursorPos.x, cursorPos.y, roadDir);
                            Cursor.removeCursor(cursorPos);

                            mode = 0;
                            roadDir = -1;
                            Cursor.swapSprite();
                            Cursor.drawCursor(cursorPos);

                            Board.peekPlayer().setLongestRoad(Board.longestRoad(Board.peekPlayer()));
                            Board.renderPlayerStats();
                        }
                    }
                    else if (mode == 2) {
                        if (!robberCursor.equals(robberTile.corner())) {
                            mode = 0;
                            Cursor.drawCursor(cursorPos);
                            Cursor.removeRobberCursor(robberCursor);

                            robberTile.flipBlocked();
                            for (Tile t : Board.tiles) {
                                if (t.corner().equals(robberCursor)) {
                                    t.flipBlocked();
                                    robberTile = t;

                                    if (Catan.CLIMATE && t.getResource() == Resource.DESERT) {
                                        ChatBox.desertRobber(Board.peekPlayer());
                                        Climate.decrement();
                                    }

                                    curGUI = new StealGUI(Board.getPlayersOnTile(t), 34, 45);
                                    if (!curGUI.buttonPress(-1)) {
                                        mode = 5;
                                        curGUI.render();
                                    }
                                    else {
                                        curGUI.terminate();
                                        curGUI = null;
                                    }
                                
                                    break;
                                }
                            }

                            robberCursor = null;
                        }
                    }
                    else {
                        if (curGUI.click()) {
                            if (guiQueue.isEmpty()) {
                                if (mode == 3) {
                                    mode = 2;
                                    robberCursor = robberTile.corner();
                                    Cursor.removeCursor(cursorPos);
                                    Cursor.drawRobberCursor(robberCursor);
                                }
                                else if (mode > 2 && mode != 7) {
                                    mode = 0;
                                }

                                if (mode != 7)
                                    curGUI = null;
                                else mode = 6;
                            }
                            else {
                                Terminal.flush();
                                Thread.sleep(500);
                                
                                curGUI = guiQueue.poll();
                                curGUI.render();
                            }
                        }
                    }
                }
                catch (Exception e) {
                    System.err.println("Something went wrong");
                }
            }
            else if (buffer[bufferID] == ' ' && curGUI != null) {
                try {
                    if (curGUI.click()) {
                        curGUI = null;
                        mode = 0;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (buffer[bufferID] == 'r' && !diceHaveBeenThrown && mode < 2) {
                try {
                    if (mode == 1) {
                        Cursor.removeRoad(cursorPos.x, cursorPos.y, roadDir);
                        Cursor.removeCursor(cursorPos);
                        Cursor.swapSprite();

                        Cursor.drawCursor(cursorPos);
                        mode = 0;
                        roadDir = -1;
                    }

                    turn++;
                    cardHasBeenUsed = false;
                    int roll = DiceRender.animateRolling();
                    if (roll == 7) {
                        if (Catan.CLIMATE)
                            Board.spreadDesert();

                        for (int i=0; i<Catan.PLAYERS; i++) {
                            Player p = Board.nextPlayer();
                            if (p.getTotalResources() > 7)
                                guiQueue.add(new RobberGUI(29, 20, p));
                        }

                        if (!guiQueue.isEmpty()) {
                            mode = 3;
                            curGUI = guiQueue.poll();
                            curGUI.render();
                        }
                        else {
                            mode = 2;
                            robberCursor = robberTile.corner();
                            Cursor.removeCursor(cursorPos);
                            Cursor.drawRobberCursor(robberCursor);
                        }
                    }
                    else {
                        Board.distributeResources(roll);
                        Board.renderPlayerStats();
                    }
                    
                    diceHaveBeenThrown = true;
                }
                catch (Exception e) {
                    System.err.println("Something went wrong");
                }
            }
            else if (buffer[bufferID] == '\t' && diceHaveBeenThrown && mode < 2) {
                if (roadDir != -1) Cursor.removeRoad(cursorPos.x, cursorPos.y, roadDir);
                Cursor.removeCursor(cursorPos);

                if (mode == 0) {
                    roadDir = Board.firstLegalRoad(cursorPos);
                    if (roadDir != -1) {
                        Cursor.swapSprite();
                        mode = 1;
                    }
                }
                else {
                    Cursor.swapSprite();
                    mode = 0;
                    roadDir = -1;
                }

                if (roadDir != -1) Cursor.drawRoad(cursorPos.x, cursorPos.y, roadDir);
                Cursor.drawCursor(cursorPos);
            }
            else {
                // something else
                try {
                    if (curGUI != null) {
                        if (curGUI.buttonPress(buffer[bufferID])) {
                            curGUI = null;
                            mode = 0;
                        }
                    }
                    else if (freeRoads > 0 || mode > 0) {
                        // do nothing
                    }
                    else if (buffer[bufferID] == 't' && diceHaveBeenThrown) {
                        mode = 4;
                        curGUI = new TradeGUI(24, 16);
                        curGUI.render();
                    }
                    else if (buffer[bufferID] == 'd' && diceHaveBeenThrown) {
                        curGUI = new DevGUI(33, 19, Board.peekPlayer());
                        curGUI.render();
                        mode = 6;
                    }
                    else if (buffer[bufferID] == 'a' && diceHaveBeenThrown) {
                        if (Board.peekPlayer().canAffordCard() && Board.cardDeck.size() > 0) {
                            Board.peekPlayer().buyCard();
                            ChatBox.buyDevCard(Board.peekPlayer(), 
                                Board.peekPlayer().getCards().get(
                                    Board.peekPlayer().getCards().size()-1));
                            curGUI = new CardGUI(45, 21, 
                                Board.peekPlayer().getCards().get(Board.peekPlayer().getCards().size()-1));
                            curGUI.render();
                            mode = 6;

                            Board.renderPlayerStats();
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            buffer[bufferID++] = 0;
        }

        return false;
    }

    static Building lastBuilt = null;
    public static boolean clearBufferAtStart(int[] buffer) {
        int bufferID = 0;
        while (buffer[bufferID] != 0) {
            if (buffer[bufferID] == 3) // control-c
                return true;
            else if (buffer[bufferID] == 10 || buffer[bufferID] == 13) {
                // enter
            }
            else if (buffer[bufferID] == 27) {
                buffer[bufferID++] = 0;
                if (buffer[bufferID] == 91) {
                    buffer[bufferID++] = 0;

                    if (buffer[bufferID] == 49) { // shift or option held
                        buffer[bufferID++] = 0;
                        buffer[bufferID++] = 0;
                        buffer[bufferID++] = 0;
                    }

                    switch (buffer[bufferID]) {
                        case (65): moveCursorUp(); break; // UP
                        case (66): moveCursorDown(); break; // DOWN
                        case (67): moveCursorRight(); break; // RIGHT
                        case (68): moveCursorLeft(); break; // LEFT
                    }
                }
                else if (buffer[bufferID] == 0) { // pressed escape
                    buffer[--bufferID] = 27;

                    try {
                        if (curGUI != null) {
                            if (curGUI.buttonPress(27)) {
                                curGUI = null;
                                mode = 0;
                            }
                        }
                        else if (mode == 0) {
                            curGUI = new MenuGUI(22, 10);
                            curGUI.render();
                            mode = 6;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (buffer[bufferID] == 127) {
                // delete
            }
            else if (buffer[bufferID] == ' ') {
                try {
                    if (mode == 0) {
                        if (Board.getBuildingAt(cursorPos).isBuildable()) {
                            Board.getBuildingAt(cursorPos).build(Board.peekPlayer());
                            lastBuilt = Board.getBuildingAt(cursorPos);
                            if (Board.peekPlayer().getRoads() == 1) {
                                for (int i=2; i<=12; i++) lastBuilt.gatherResource(i);
                                ChatBox.buildSecondTown(Board.peekPlayer());
                            }
                            else ChatBox.buildTown(Board.peekPlayer());

                            Cursor.removeCursor(cursorPos);
                            Board.renderPlayerStats();

                            mode = 1;
                            roadDir = Board.firstLegalRoad(cursorPos);

                            Cursor.swapSprite();
                            Cursor.drawRoad(cursorPos.x, cursorPos.y, roadDir);
                            Cursor.drawCursor(cursorPos);
                        }
                    }
                    else if (mode == 1) {
                        Board.peekPlayer().increaseResource(Resource.BRICK, 1);
                        Board.peekPlayer().increaseResource(Resource.WOOD, 1);
                        Board.buildRoad(cursorPos, roadDir, Board.peekPlayer());

                        Cursor.removeRoad(cursorPos.x, cursorPos.y, roadDir);
                        Cursor.removeCursor(cursorPos);

                        mode = 0;
                        roadDir = -1;
                        Cursor.swapSprite();
                        Cursor.drawCursor(cursorPos);

                        Board.peekPlayer().setLongestRoad(Board.longestRoad(Board.peekPlayer()));
                        Board.renderPlayerStats();
                        Catan.starter++;
                    }
                    else if (curGUI != null) {
                        if (curGUI.click()) {
                            curGUI = null;
                            mode = 0;
                        }
                    }
                }
                catch (Exception e) {
                    System.err.println("Something went wrong");
                }
            }
            else {
                // something else
            }

            buffer[bufferID++] = 0;
        }

        return false;
    }

    public static boolean clearMenuBuffer(int[] buffer) throws IOException {
        int bufferID = 0;
        while (buffer[bufferID] != 0) {
            if (buffer[bufferID] == 3) // control-c
                return true;
            else if (buffer[bufferID] == 10 || buffer[bufferID] == 13) {
                if (curGUI == null) {
                    curGUI = new MenuGUI(45, 10);
                    curGUI.render();
                    mode = 6;
                }
                else if (curGUI.buttonPress(buffer[bufferID])) {
                    curGUI = null;
                    mode = 0;
                }
            }
            else if (buffer[bufferID] == 27) {
                buffer[bufferID++] = 0;
                if (buffer[bufferID] == 91) {
                    buffer[bufferID++] = 0;

                    if (buffer[bufferID] == 49) { // shift or option held
                        buffer[bufferID++] = 0;
                        buffer[bufferID++] = 0;
                        buffer[bufferID++] = 0;
                    }

                    if (curGUI != null) switch (buffer[bufferID]) {
                        case (65): moveCursorUp(); break; // UP
                        case (66): moveCursorDown(); break; // DOWN
                        case (67): moveCursorRight(); break; // RIGHT
                        case (68): moveCursorLeft(); break; // LEFT
                    }
                    else {
                        curGUI = new MenuGUI(45, 10);
                        curGUI.render();
                        mode = 6;
                    }
                }
                else if (buffer[bufferID] == 0) { // pressed escape
                    buffer[--bufferID] = 27;

                    if (curGUI != null) {
                        if (curGUI.buttonPress(27)) {
                            curGUI = null;
                            mode = 0;
                        }
                    }
                    else {
                        curGUI = new MenuGUI(45, 10);
                        curGUI.render();
                        mode = 6;
                    }
                }
            }
            else if (buffer[bufferID] == 127) {
                if (curGUI != null && curGUI.buttonPress(buffer[bufferID])) {
                    curGUI = null;
                    mode = 0;
                }
            }
            else if (buffer[bufferID] == ' ') {
                if (curGUI == null) {
                    curGUI = new MenuGUI(45, 10);
                    curGUI.render();
                    mode = 6;
                }
                else if (curGUI.click()) {
                    curGUI = null;
                    mode = 0;
                }
            }
            else {
                if (curGUI == null) {
                    curGUI = new MenuGUI(45, 10);
                    curGUI.render();
                    mode = 6;
                }
                else if (curGUI.buttonPress(buffer[bufferID])) {
                    curGUI = null;
                    mode = 0;
                }
            }

            buffer[bufferID++] = 0;
        }

        return false;
    }

    public static void setStartState() {
        lastBuilt = null;
        mode = 0;

        diceHaveBeenThrown = false;
        roadDir = 0;
        cardHasBeenUsed = false;
        turn = 0;
        freeRoads = 0;

        curGUI = null;
        guiQueue = new ArrayDeque<>();
    }
}