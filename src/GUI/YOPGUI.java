import java.io.IOException;

public class YOPGUI implements GUI {
    private static final String PATH = "data/GUI/YOP/";
    private static final String BACKGROUND = "data/GUI/YOP/background.txt";
    private static final int WIDTH = 55, HEIGHT = 17;

    private int x, y;
    private Player player;
    private int pos1, pos2, level;
    private boolean hasChosenBoth;

    public YOPGUI(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;

        this.pos1 = 0;
        this.pos2 = 0;
        this.level = 0;
        this.hasChosenBoth = false;
    }

    public boolean click() throws IOException {
        if (hasChosenBoth) {
            terminate();
            return true;
        }
        
        moveDown();
        return false;
    }

    public void moveUp() throws IOException {
        if (level == 1) {
            Artist.drawFromFileShallow(PATH+"dot_selected.txt", x+6+pos1*10, y+6);
            Artist.drawFromFileShallow(PATH+"dot_normal.txt", x+6+pos2*10, y+13);
            level--;
        }
    }

    public void moveDown() throws IOException {
        hasChosenBoth = true;
        
        if (level == 0) {
            Artist.drawFromFileShallow(PATH+"dot_normal.txt", x+6+pos1*10, y+6);
            Artist.drawFromFileShallow(PATH+"dot_selected.txt", x+6+pos2*10, y+13);
            level++;
        }
    }

    public void moveLeft() throws IOException {
        if (level == 0) {
            if (pos1 > 0) {
                Artist.drawColorBox(x+6+pos1*10, 
                                    y+6, 
                                    x+8+pos1*10, 
                                    y+7, 
                                    "beige");
    
                pos1--;
                Artist.drawFromFileShallow(PATH+"dot_selected.txt", x+6+pos1*10, y+6);
            }
        }
        else {
            if (pos2 > 0) {
                Artist.drawColorBox(x+6+pos2*10, 
                                    y+13, 
                                    x+8+pos2*10, 
                                    y+14, 
                                    "beige");
    
                pos2--;
                Artist.drawFromFileShallow(PATH+"dot_selected.txt", x+6+pos2*10, y+13);
            }
        }
    }

    public void moveRight() throws IOException {
        if (level == 0) {
            if (pos1 < 4) {
                Artist.drawColorBox(x+6+pos1*10, 
                                    y+6, 
                                    x+8+pos1*10, 
                                    y+7, 
                                    "beige");
    
                pos1++;
                Artist.drawFromFileShallow(PATH+"dot_selected.txt", x+6+pos1*10, y+6);
            }
        }
        else {
            if (pos2 < 4) {
                Artist.drawColorBox(x+6+pos2*10, 
                                    y+13, 
                                    x+8+pos2*10, 
                                    y+14, 
                                    "beige");
    
                pos2++;
                Artist.drawFromFileShallow(PATH+"dot_selected.txt", x+6+pos2*10, y+13);
            }
        }
    }

    public void render() throws IOException {
        Artist.drawFromFileShallow(BACKGROUND, x, y);
        Artist.drawFromFileShallow(PATH+"dot_selected.txt", x+6+pos1*10, y+6);
        Artist.drawFromFileShallow(PATH+"dot_normal.txt", x+6+pos2*10, y+13);
    }

    public void terminate() throws IOException {
        player.increaseResource(Resource.RESOURCES[pos1], 1);
        player.increaseResource(Resource.RESOURCES[pos2], 1);

        int[] resources = new int[5];
        resources[pos1]++;
        resources[pos2]++;
        ChatBox.yearOfPlenty(player, resources);

        Artist.restoreSubscreen(x, y, x+WIDTH-1, y+HEIGHT-1);

        for (Building b : Board.buildings.values()) {
            b.render();
        }

        for (Tile t : Board.tiles) {
            t.renderNumber();
        }

        Cursor.drawCursor(UI.cursorPos);
        Board.renderPlayerStats();
    }

    public boolean buttonPress(int in) throws IOException {
        return false;
    }
}
