import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class StealGUI implements GUI {
    private static final String PATH = "data/GUI/steal/";
    private String background;

    private GUIElement[] elements;
    private GUIElement curSelected;

    private int x, y;
    private int WIDTH, HEIGHT;

    private ArrayList<Player> targets;
    private Player player;
    private int target = 0;

    public StealGUI(HashSet<Player> possibleTargets, int x, int y) throws IOException {
        this.targets = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.player = Board.peekPlayer();

        for (Player p : possibleTargets) {
            if (p != Board.peekPlayer() && p.getTotalResources() > 0)
                targets.add(p);
        }

        if (this.targets.size() == 2) {
            this.WIDTH = 45;
            this.HEIGHT = 9;

            this.background = PATH + "background2.txt";

            GUIElement[] elements = {
                new GUIElement(0, x+ 4, y+2, PATH + "player_normal.txt", PATH + "player_selected.txt"),
                new GUIElement(1, x+24, y+2, PATH + "player_normal.txt", PATH + "player_selected.txt")
            };

            elements[0].setRight(elements[1]);
            elements[1].setLeft(elements[0]);

            this.curSelected = elements[0];
            this.elements = elements;
        }
        else if (this.targets.size() == 3) {
            this.x -= 10; x -= 10;
            this.WIDTH = 65;
            this.HEIGHT = 9;

            this.background = PATH + "background3.txt";

            GUIElement[] elements = {
                new GUIElement(0, x+ 4, y+2, PATH + "player_normal.txt", PATH + "player_selected.txt"),
                new GUIElement(1, x+24, y+2, PATH + "player_normal.txt", PATH + "player_selected.txt"),
                new GUIElement(2, x+44, y+2, PATH + "player_normal.txt", PATH + "player_selected.txt")
            };

            elements[0].setRight(elements[1]);
            elements[1].setRight(elements[2]);
            elements[1].setLeft(elements[0]);
            elements[2].setLeft(elements[1]);

            this.curSelected = elements[0];
            this.elements = elements;
        }
        else {
            this.WIDTH = -1;
            this.HEIGHT = -1;

            this.curSelected = null;
        }
    }

    public boolean click() throws IOException {
        terminate();

        return true;
    }

    public boolean buttonPress(int in) throws IOException {
        return curSelected == null;
    }

    public void moveUp() throws IOException {
        GUIElement next = curSelected.getUp();

        if (next != null) {
            curSelected.drawNormal();
            next.drawSelected();

            curSelected = next;
        }
    }

    public void moveDown() throws IOException {
        GUIElement next = curSelected.getDown();

        if (next != null) {
            curSelected.drawNormal();
            next.drawSelected();

            curSelected = next;
        }
    }

    public void moveLeft() throws IOException {
        GUIElement next = curSelected.getLeft();

        if (next != null) {
            curSelected.drawNormal();
            next.drawSelected();

            Artist.drawColorBox(curSelected.getX(), curSelected.getY(), 
                    curSelected.getX()+16, curSelected.getY()+4, 
                    targets.get(target).getColor());

            curSelected = next;
            target--;
        }
    }

    public void moveRight() throws IOException {
        GUIElement next = curSelected.getRight();

        if (next != null) {
            curSelected.drawNormal();
            next.drawSelected();

            Artist.drawColorBox(curSelected.getX(), curSelected.getY(), 
                    curSelected.getX()+16, curSelected.getY()+4, 
                    targets.get(target).getColor());

            curSelected = next;
            target++;
        }
    }

    public void render() throws IOException {
        Artist.drawFromFileShallow(background, x, y);

        for (int i=0; i<targets.size(); i++) {
            Artist.drawColorBox(x+4+20*i, y+2, x+20+20*i, y+6, targets.get(i).getColor());
        }

        for (GUIElement e : elements) {
            e.drawNormal();
        }

        curSelected.drawSelected();
    }

    public void terminate() throws IOException {
        Artist.restoreSubscreen(x, y, x+this.WIDTH-1, y+this.HEIGHT-1);

        for (Building b : Board.buildings.values()) {
            b.render();
        }

        for (Tile t : Board.tiles) {
            t.renderNumber();
        }

        Cursor.drawCursor(UI.cursorPos);

        if (this.targets.size() == 0) return;
        
        Resource steal = targets.get(target).getRandomResource();
        player.increaseResource(steal, 1);
        targets.get(target).decreaseResource(steal, 1);
        ChatBox.stealResources(player, targets.get(target), steal);

        Board.renderPlayerStats();
    }
}
