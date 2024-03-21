import java.io.IOException;
import java.util.ArrayList;

public class PlayerSelectGUI implements GUI {
    private static final int WIDTH = 97, HEIGHT = 48;
    private static final String PATH = "data/GUI/player_select/";
    private static final String BACKGROUND = PATH + "background.txt";
    private static final String PLAYER_BACKGROUND = PATH + "player_background.txt";
    private static final String[] COLORS = {
        "blue", "red", "green", "white"
    };

    private int x, y;
    private boolean[] exists;
    private String[] names;
    private int[] assignedTo;
    private int[] toAssigned;
    private GUIElement[] elements;
    private int curID;
    private int VP;

    public PlayerSelectGUI(int x, int y) throws IOException {
        this.x = x;
        this.y = y;

        exists = new boolean[4];
        names = new String[4];
        assignedTo = new int[4];
        toAssigned = new int[4];
        curID = 0;
        VP = 10;

        GUIElement[] elements = {
            new GUIElement(1, x+18, y+5 , PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(2, x+63, y+5 , PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(3, x+18, y+21, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(4, x+63, y+21, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(5, x+39, y+35, PATH+"small_plus_normal.txt", PATH+"small_plus_selected.txt"),
            new GUIElement(6, x+53, y+36, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(7, x+46, y+35, PATH+"num_normal.txt", null, PATH+"num/10.txt", "\u001b[38;5;0m")
        };
        this.elements = elements;
    }

    public boolean click() throws IOException {
        if (curID == 6) {
            if (gameReadyToStart()) {
                terminate();
                return true;
            }
        }
        else if (curID == 4) {
            if (VP == 15) return false;

            VP++;
            elements[6].setMask(PATH+"num/"+VP+".txt");
            elements[6].drawNormal();
        }
        else if (curID == 5) {
            if (VP == 5) return false;

            VP--;
            elements[6].setMask(PATH+"num/"+VP+".txt");
            elements[6].drawNormal();
        }
        else if (!exists[curID]) {
            exists[curID] = true;
            Artist.drawFromFileShallow(PLAYER_BACKGROUND, 
                            elements[curID].getX()-13, 
                            elements[curID].getY()-2);

            int nextOpen = getNextOpenColorID(0);
            assignedTo[nextOpen] = curID+1;
            toAssigned[curID] = nextOpen;

            names[curID] = "";
            nameBox(curID);

            Artist.drawFromFileShallow(PATH+"dot_selected.txt", 
                            elements[curID].getX()-7+9*nextOpen, 
                            elements[curID].getY()+8);
        }
        else return buttonPress(' ');

        return false;
    }

    public void moveUp() throws IOException {
        if (curID == 6) {
            Artist.drawFromFileShallow(PATH+"done_normal.txt", x+37, y+40);
            curID = 4;
            select(curID);
        }
        else if (curID > 1) {
            unselect(curID);
            curID -= 2;
            select(curID);
        }
    }

    public void moveDown() throws IOException {
        if (curID < 4) {
            unselect(curID);
            curID += 2;
            select(curID);
        }
        else if (curID < 6) {
            unselect(curID);
            curID = 6;
            Artist.drawFromFileShallow(PATH+"done_selected.txt", x+37, y+40);
        }
    }

    public void moveLeft() throws IOException {
        if (curID == 6) {
            Artist.drawFromFileShallow(PATH+"done_normal.txt", x+37, y+40);
            curID = 4;
            select(curID);
        }
        else if (curID > 3) {
            if (curID == 5) {
                unselect(curID);
                curID = 4;
                select(curID);
            }
        }
        else if (exists[curID]) {
            int prev = getPreviousOpenColorID(toAssigned[curID]-1);
            if (prev != -1) {
                eraseMyDot(curID);
                assignedTo[prev] = curID+1;
                assignedTo[toAssigned[curID]] = 0;
                toAssigned[curID] = prev;
                select(curID);
            }
            else if (curID % 2 == 1) {
                unselect(curID);
                curID -= 1;
                select(curID);
            }
        }
        else if (curID % 2 == 1) {
            unselect(curID);
            curID -= 1;
            select(curID);
        }
    }

    public void moveRight() throws IOException {
        if (curID == 6) {
            Artist.drawFromFileShallow(PATH+"done_normal.txt", x+37, y+40);
            curID = 5;
            select(curID);
        }
        else if (curID > 3) {
            if (curID == 4) {
                unselect(curID);
                curID = 5;
                select(curID);
            }
        }
        else if (exists[curID]) {
            int next = getNextOpenColorID(toAssigned[curID]+1);
            if (next != -1) {
                eraseMyDot(curID);
                assignedTo[next] = curID+1;
                assignedTo[toAssigned[curID]] = 0;
                toAssigned[curID] = next;
                select(curID);
            }
            else if (curID % 2 == 0) {
                unselect(curID);
                curID += 1;
                select(curID);
            }
        }
        else if (curID % 2 == 0) {
            unselect(curID);
            curID += 1;
            select(curID);
        }
    }

    public void render() throws IOException {
        Artist.drawFromFileShallow(BACKGROUND, x, y);

        for (int i=0; i<4; i++) {
            if (exists[i]) {
                Artist.drawFromFileShallow(PLAYER_BACKGROUND, 
                            elements[i].getX()-13, 
                            elements[i].getY()-2);
                nameBox(i);
            }
            else {
                if (i == curID) elements[i].drawSelected();
                else elements[i].drawNormal();
            }
        }

        elements[4].drawNormal();
        elements[5].drawNormal();
        elements[6].drawNormal();

        for (int i=0; i<4; i++) {
            if (assignedTo[i] != 0) {
                String sprite = PATH + (i == curID ? "dot_normal.txt" : "dot_selected.txt");

                Artist.drawFromFileShallow(sprite, 
                            elements[assignedTo[i]-1].getX()-7+9*i, 
                            elements[assignedTo[i]-1].getY()+8);
            }
        }

        if (curID == 4)
            Artist.drawFromFileShallow(PATH+"done_selected.txt", x+37, y+40);
        else
            Artist.drawFromFileShallow(PATH+"done_normal.txt", x+37, y+40);
    }

    public void terminate() throws IOException {
        Artist.restoreSubscreen(x, y, x+WIDTH-1, y+HEIGHT-1);
        if (curID == -1) return;

        Catan.inGame = true;
        Catan.starter = 0;
        Catan.VP_REQUIREMENT = VP;

        setPlayers();
        transferPlayers();

        UI.setStartState();
        Board.initialize();

        UI.resetCursorPos();
        UI.drawCursor();

        Artist.drawPlayerEmblem(Board.peekPlayer().getColor());
    }

    public boolean buttonPress(int in) throws IOException {
        if (in == 27) {
            curID = -1;
            terminate();

            UI.curGUI = new MenuGUI(45, 10);
            UI.curGUI.render();
        }
        else if (in == '\t') {
            if (curID != 4) {
                if (curID % 2 == 0) {
                    unselect(curID);
                    curID += 1;
                    select(curID);
                }
                else {
                    unselect(curID);
                    curID -= 1;
                    select(curID);
                }
            }
        }
        else if (in == 10 || in == 13) {
            if (curID != 4 && exists[curID]) {
                Artist.drawFromFileShallow(PATH+"patch_over_player.txt", 
                        elements[curID].getX()-13, 
                        elements[curID].getY()-2);

                exists[curID] = false;
                assignedTo[toAssigned[curID]] = 0;
                names[curID] = null;

                select(curID);
            }
        }
        else if (in == 127) {
            if (exists[curID] && names[curID].length() > 0) {
                names[curID] = names[curID].substring(0, names[curID].length()-1);
                nameBox(curID);
            }
        }
        else {
            if (exists[curID] && names[curID].length() < 16) {
                names[curID] += (char)in;
                nameBox(curID);
            }
        }

        return false;
    }

    private int getNextOpenColorID(int id) {
        for (int i=id; i<4; i++) {
            if (assignedTo[i] == 0) {
                return i;
            }
        }

        return -1;
    }

    private int getPreviousOpenColorID(int id) {
        for (int i=id; i>=0; i--) {
            if (assignedTo[i] == 0) {
                return i;
            }
        }

        return -1;
    }

    private void unselect(int i) throws IOException {
        if (i > 3) {
            elements[i].drawNormal();
        }
        else if (exists[i]) {
            Artist.drawFromFileShallow(PATH+"dot_normal.txt", 
                            elements[i].getX()-7+9*toAssigned[i], 
                            elements[i].getY()+8);
        }
        else {
            elements[i].drawNormal();
        }
    }

    private void select(int i) throws IOException {
        if (i > 3) {
            elements[i].drawSelected();
        }
        else if (exists[i]) {
            Artist.drawFromFileShallow(PATH+"dot_selected.txt", 
                            elements[i].getX()-7+9*toAssigned[i], 
                            elements[i].getY()+8);
        }
        else {
            elements[i].drawSelected();
        }
    }

    private void eraseMyDot(int i) throws IOException {
        Artist.drawFromFileShallow(PATH+"dot_erase.txt", 
                            elements[i].getX()-7+9*toAssigned[i], 
                            elements[i].getY()+8);
    }

    private void nameBox(int i) {
        Terminal.setXY(elements[i].getX()-9, elements[i].getY());
        Terminal.printShallow("\u001b[48;5;230m", "NAME: ");
        Terminal.printShallow("\u001b[48;5;0m", "                ");

        Terminal.setXY(elements[i].getX()-3, elements[i].getY());
        Terminal.printShallow("\u001b[48;5;0m\u001b[38;5;255m", names[i]);
    }

    private boolean gameReadyToStart() {
        int numPlayers = 0;

        for (int i=0; i<4; i++) {
            if (exists[i]) {
                numPlayers++;
                if (names[i].length() == 0)
                    return false;

                for (int j=i+1; j<4; j++) {
                    if (names[i].equals(names[j]))
                        return false;
                }
            }
        }

        return numPlayers >= 2;
    }

    private void setPlayers() {
        int numPlayers = 0;

        for (int i=0; i<4; i++) {
            if (exists[i]) {
                numPlayers++;
            }
        }

        Catan.PLAYERS = numPlayers;
    }

    private void transferPlayers() {
        Board.transferPlayer = new ArrayList<>();
        for (int i=0; i<4; i++) {
            if (exists[i]) {
                Player p = new Player(COLORS[toAssigned[i]], names[i]);
                Board.transferPlayer.add(p);
            }
        }
    }
}
