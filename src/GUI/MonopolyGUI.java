import java.io.IOException;

public class MonopolyGUI implements GUI {
    private static final String PATH = "data/GUI/monopoly/";
    private static final String BACKGROUND = "data/GUI/monopoly/background.txt";
    private static final int WIDTH = 55, HEIGHT = 10;

    private int x, y;
    private Player player;
    private int pos;

    public MonopolyGUI(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.pos = 0;
    }

    public boolean click() throws IOException {
        terminate();
        return true;
    }

    public void moveUp() throws IOException {
        // do nothing
    }

    public void moveDown() throws IOException {
        // do nothing
    }

    public void moveLeft() throws IOException {
        if (pos > 0) {
            Artist.drawColorBox(x+6+pos*10, 
                                y+6, 
                                x+8+pos*10, 
                                y+7, 
                                "beige");

            pos--;
            Artist.drawFromFileShallow(PATH+"dot_selected.txt", x+6+pos*10, y+6);
        }
    }

    public void moveRight() throws IOException {
        if (pos < 4) {
            Artist.drawColorBox(x+6+pos*10, 
                                y+6, 
                                x+8+pos*10, 
                                y+7, 
                                "beige");

            pos++;
            Artist.drawFromFileShallow(PATH+"dot_selected.txt", x+6+pos*10, y+6);
        }
    }

    public void render() throws IOException {
        Artist.drawFromFileShallow(BACKGROUND, x, y);
        Artist.drawFromFileShallow(PATH+"dot_selected.txt", x+6+pos*10, y+6);
    }

    public void terminate() throws IOException {
        for (int i=0; i<Catan.PLAYERS; i++) {
            Player p = Board.nextPlayer();
            int resourceNum = p.getResource(pos);

            p.decreaseResource(Resource.RESOURCES[pos], resourceNum);
            player.increaseResource(Resource.RESOURCES[pos], resourceNum);
        }

        ChatBox.monopoly(player, Resource.RESOURCES[pos]);
        
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
